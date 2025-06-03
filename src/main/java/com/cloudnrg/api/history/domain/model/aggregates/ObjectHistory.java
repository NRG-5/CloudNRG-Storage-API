package com.cloudnrg.api.history.domain.model.aggregates;

import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.valueobjects.Action;
import com.cloudnrg.api.iam.domain.model.aggregates.User;
import com.cloudnrg.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ObjectHistory extends AuditableAbstractAggregateRoot<ObjectHistory> {
    @ManyToOne
    @JoinColumn(name = "file_id")
    private CloudFile file;

    @NotNull
    private Action action;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ObjectHistory(CloudFile file, User user, String action) {
        this.file = file;
        this.action = Action.valueOf(action.toUpperCase());
        this.user = user;
    }

    public UUID getFileId() {
        return this.file.getId();
    }

    public UUID getUserId() {
        return this.user.getId();
    }
}
