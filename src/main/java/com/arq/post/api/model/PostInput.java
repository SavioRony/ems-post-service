package com.arq.post.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostInput {
    @NotBlank(message = "O título é obrigatório.")
    private String title;

    @NotBlank(message = "O corpo do post é obrigatório.")
    private String body;

    @NotBlank(message = "O autor é obrigatório.")
    private String author;
}