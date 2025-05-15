package com.cloudnrg.api.iam.domain.model.aggregates;

import com.cloudnrg.api.iam.domain.model.events.UserCreationEvent;
import com.cloudnrg.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotNull
    String username;

    @NotNull
    String email;

    @NotNull
    String passwordHash;

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public void userCreation(){
        this.registerEvent(new UserCreationEvent(this, this.getId()));
    }

}
