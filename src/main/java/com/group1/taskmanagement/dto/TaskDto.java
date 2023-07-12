package com.group1.taskmanagement.dto;

import com.group1.taskmanagement.model.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private Long userId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime dueDate;
    private Status status;
}
