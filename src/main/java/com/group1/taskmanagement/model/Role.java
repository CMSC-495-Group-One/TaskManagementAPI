package com.group1.taskmanagement.model;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.dto.TaskDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // DB Model
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public static RoleDto toDto(Role role) {
        return RoleDto.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .build();
    }

    public static Role fromDto(RoleDto role) {
        Role.RoleBuilder builder = Role.builder();

        builder.name(role.getName());

        return builder.build();
    }
}
