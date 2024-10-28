package com.wook.online_store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "rold_id")
    private Long roleId;

    @Column(nullable = false)
    private String name;
}
