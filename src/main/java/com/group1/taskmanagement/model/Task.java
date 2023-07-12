package com.group1.taskmanagement.model;

import com.group1.taskmanagement.dto.TaskDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedDate;

    @Column
    private LocalDateTime dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .userId(task.getUser().getId())
                .createdDate(task.getCreatedDate())
                .updatedDate(task.getUpdatedDate())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
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

        if (task.getDueDate() != null) {
            builder.dueDate(task.getDueDate());
        }

        if (task.getStatus() != null) {
            builder.status(task.getStatus());
        }

        if (user != null) {
            builder.user(user);
        }

        return builder.build();
    }
}
