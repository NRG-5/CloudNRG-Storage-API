package com.cloudnrg.api.storage.domain.model.aggregates;

import com.cloudnrg.api.iam.domain.model.aggregates.User;
import com.cloudnrg.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Folder extends AuditableAbstractAggregateRoot<Folder> {

    @NotNull
    String name;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    Folder parentFolder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
