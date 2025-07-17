package com.example.ecommerce.model;

import com.example.ecommerce.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name="role_name", length = 20)
    private Roles roleName;

    public Role(Roles roleName) {this.roleName = roleName;
    }
}
