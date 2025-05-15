package com.cloudnrg.api.storage.domain.model.aggregates;

import com.cloudnrg.api.iam.domain.model.aggregates.User;
import com.cloudnrg.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
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
public class CloudFile extends AuditableAbstractAggregateRoot<CloudFile> {

    @NotNull
    String filename;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    Folder folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @NotNull
    Long size;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String mimeType;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String md5;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String path;

    public CloudFile(
            String filename,
            Folder folder,
            User user,
            Long size,
            String mimeType,
            String md5,
            String path
    ){
        this.filename = filename;
        this.folder = folder;
        this.user = user;
        this.size = size;
        this.mimeType = mimeType;
        this.md5 = md5;
        this.path = path;
    }

}
