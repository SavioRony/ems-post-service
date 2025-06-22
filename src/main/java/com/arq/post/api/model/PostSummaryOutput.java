package com.arq.post.api.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryOutput {
    private String id;
    private String title;
    private String summary;
    private String author;
}
