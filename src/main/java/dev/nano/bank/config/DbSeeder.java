package dev.nano.bank.config;

import com.github.javafaker.Faker;
import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.Deposit;
import dev.nano.bank.domain.Transfer;
import dev.nano.bank.repository.AccountRepository;
import dev.nano.bank.repository.TransferRepository;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.Role;
import dev.nano.bank.repository.DepositRepository;
import dev.nano.bank.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Date;

@Configuration
public class DbSeeder {

    private static final Faker faker = new Faker();
    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransferRepository transferRepository,
            DepositRepository depositRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        return args -> {

            String superAdminRole = Role.ROLE_SUPER_ADMIN.toString();

            User superAdmin = new User(
                    "nano23",
                    passwordEncoder.encode("nano23"),
                    "na",
                    "no",
                    "MALE",
                    new Date(),
                    superAdminRole,
                    getRoleEnumName(superAdminRole).getAuthorities(),
                    true
            );

            userRepository.save(superAdmin);



            for (int i = 0; i < 3; i++) {
                String username = faker.name().username().substring(0, 9);
                String password = faker.internet().password();
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String gender = "MALE";
                Date birthDate = faker.date().birthday();
                String userRole = Role.ROLE_USER.toString();

                User user = new User(
                        username,
                        passwordEncoder.encode(password),
                        firstName,
                        lastName,
                        gender,
                        birthDate,
                        userRole,
                        getRoleEnumName(userRole).getAuthorities(),
                        true
                );

                userRepository.save(user);
            }

            for (User user : userRepository.findAll()) {
                for (int i = 0; i < 2; i++) {
                    String accountNumber = faker.number().digits(16);
                    String rib = faker.finance().iban();
                    BigDecimal balance = new BigDecimal(faker.random().nextInt(100, 10_000));

                    Account account = new Account(
                            accountNumber,
                            rib,
                            balance,
                            user
                    );
                    accountRepository.save(account);
                }
            }

            /* make a transfer */
            BigDecimal transferAmount = new BigDecimal(faker.random().nextInt(100, 10_000));
            Date dateExecution = new Date();
            Account senderAccount = accountRepository.findAll().get(0);
            Account receiverAccount = accountRepository.findAll().get(1);
            String transferReason = faker.lorem().sentence();

            Transfer transfer = new Transfer(
                    transferAmount,
                    dateExecution,
                    senderAccount,
                    receiverAccount,
                    transferReason
            );

            transferRepository.save(transfer);


            /* deposit an amount */
            BigDecimal depositAmount = new BigDecimal(faker.random().nextInt(100, 10_000));
            Date depositDateExecution = new Date();
            Account depositReceiverAccount = accountRepository.findAll().get(0);
            String depositReason = faker.lorem().sentence();
            Deposit deposit = new Deposit(
                    depositAmount,
                    depositDateExecution,
                    depositReason,
                    depositReceiverAccount,
                    "nano23"
            );

            depositRepository.save(deposit);
        };
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
