package com.group1.taskmanagement.model;

import com.group1.taskmanagement.dto.UserDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // DB Model
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    // FetchType.EAGER will grab the roles for the User automatically
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .tasks(user.getTasks().stream()
                        .map(task -> Task.toDto(task))
                        .collect(Collectors.toList()))
                .roles(user.getRoles().stream()
                        .map(role -> Role.toDto(role))
                        .collect(Collectors.toList()))
                .build();
    }

    public static User fromDto(UserDto user) {
        User.UserBuilder builder = User.builder();

        if (user.getUsername() != null) {
            builder.username(user.getUsername());
        }

        if (user.getEmail() != null) {
            builder.email(user.getEmail());
        }

        return builder.build();
    }
}