package com.cloudnrg.api.iam.domain.model.aggregates;

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

}
