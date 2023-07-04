package com.group1.taskmanagement.model;

import com.group1.taskmanagement.dto.TaskDto;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // DB Model
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .userId(task.getUser().getId())
                .build();
    }

    public static Task fromDto(TaskDto task, User user) {
        Task.TaskBuilder builder = Task.builder();

        if (task.getTitle() != null) {
            builder.title(task.getTitle());
        }

        if (task.getDescription() != null) {
            builder.description(task.getDescription());
        }

        if (user != null) {
            builder.user(user);
        }

        return builder.build();
    }
}
