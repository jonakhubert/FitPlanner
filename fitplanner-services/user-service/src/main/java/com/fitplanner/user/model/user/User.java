package com.fitplanner.user.model.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "users")
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled;

    public User(String firstName, String lastName, String email, String password, Role role, Boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    // getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public Boolean getEnabled() { return enabled; }

    // setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
