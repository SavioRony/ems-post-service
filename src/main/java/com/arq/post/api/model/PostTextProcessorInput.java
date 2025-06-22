package com.arq.post.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTextProcessorInput {
    private String postId;
    private String postBody;
}
