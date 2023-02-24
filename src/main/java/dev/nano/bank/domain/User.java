package dev.nano.bank.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
@Data @AllArgsConstructor @NoArgsConstructor
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false)
  private Long id;

  @Column(length = 10, nullable = false, unique = true)
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotNull(message = "Password is required")
  @Column(nullable=false)
  private String password;

  @Column(length = 10, nullable = false)
  private String gender;

  @NotNull(message="last name is required")
  @Column(length = 60, nullable = false)
  private String lastname;

  @NotNull(message="first name is required")
  @Column(length = 60, nullable = false)
  private String firstname;

  @Temporal(TemporalType.DATE)
  private Date birthdate;

  @Column(nullable=false)
  private String role;

  @Column(nullable=false)
  private String[] authorities;

  private boolean enable;

  public User(String username, String password, String gender, String lastname, String firstname, Date birthdate, String role, String[] authorities, boolean enable) {
    this.username = username;
    this.password = password;
    this.gender = gender;
    this.lastname = lastname;
    this.firstname = firstname;
    this.birthdate = birthdate;
    this.role = role;
    this.authorities = authorities;
    this.enable = enable;
  }
}
