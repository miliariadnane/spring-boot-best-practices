package dev.nano.bank.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.UserPrincipal;
import dev.nano.bank.dto.UserDto;
import dev.nano.bank.exception.domain.UserNotFoundException;
import dev.nano.bank.mapper.UserMapper;
import dev.nano.bank.repository.UserRepository;
import dev.nano.bank.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.nano.bank.service.Impl.constant.UserServiceImplConstant.FOUND_USER_BY_USERNAME;
import static dev.nano.bank.service.Impl.constant.UserServiceImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() throws UserNotFoundException {
        log.info("Get all users");
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            log.info("No user found");
            throw new UserNotFoundException("No user found");
        } else {
            return userMapper.toListDto(userList);
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            log.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }

        userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        log.info(FOUND_USER_BY_USERNAME + username);
        return userPrincipal;
    }
}
