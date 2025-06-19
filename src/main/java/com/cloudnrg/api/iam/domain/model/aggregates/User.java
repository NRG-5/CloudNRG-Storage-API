package com.cloudnrg.api.iam.domain.model.aggregates;

import com.cloudnrg.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotNull
    String username;

    @Email
    @NotNull
    String email;

    @NotNull
    String password;

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.password = passwordHash;
    }

}
