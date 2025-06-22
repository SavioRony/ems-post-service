package com.arq.post.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOutput {
    private String id;
    private String title;
    private String body;
    private String author;
    private int wordCount;
    private double calculatedValue;

    public PostOutput(String id, String title, String body, String author) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
    }
}
