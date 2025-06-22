package com.arq.post.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTextProcessorResult {
    private String postId;
    private Integer wordCount;
    private Double calculatedValue;
}
