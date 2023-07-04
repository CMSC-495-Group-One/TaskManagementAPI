package com.group1.taskmanagement.dto;

import lombok.*;

@Getter @Setter
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private Long userId;

}
