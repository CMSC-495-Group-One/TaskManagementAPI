package com.group1.taskmanagement.model;

import com.group1.taskmanagement.dto.RoleDto;
import com.group1.taskmanagement.dto.TaskDto;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // DB Model
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public static RoleDto toDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Role fromDto(RoleDto role) {
        Role.RoleBuilder builder = Role.builder();

        builder.name(role.getName());

        return builder.build();
    }
}
