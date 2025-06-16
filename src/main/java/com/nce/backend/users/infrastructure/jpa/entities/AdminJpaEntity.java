package com.nce.backend.users.infrastructure.jpa.entities;

import com.nce.backend.users.domain.entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "app_admin")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "admin_id")
public class AdminJpaEntity extends UserJpaEntity {

    private String fallbackEmail;

}
