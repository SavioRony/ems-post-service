package com.arq.post.common;

import com.arq.post.api.model.PostInput;
import com.arq.post.api.model.PostOutput;
import com.arq.post.api.model.PostSummaryOutput;
import com.arq.post.api.model.PostTextProcessorInput;
import com.arq.post.domain.model.Post;

public class PostMapper {

    private PostMapper() {
    }

    public static Post toEntity(PostInput input) {
        if (input == null) return null;

        Post post = new Post();
        post.setTitle(input.getTitle());
        post.setBody(input.getBody());
        post.setAuthor(input.getAuthor());
        return post;
    }

    public static PostOutput toOutput(Post post) {
        if (post == null) return null;

        return new PostOutput(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getAuthor()
        );
    }


    public static PostOutput toOutputComplete(Post post) {
        if (post == null) return null;

        return new PostOutput(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getAuthor(),
                post.getWordCount(),
                post.getCalculatedValue()
        );
    }


    public static PostTextProcessorInput toTextProcessorInput(PostOutput post) {
        if (post == null) return null;

        return new PostTextProcessorInput(
                post.getId(),
                post.getBody()
        );
    }

    public static PostSummaryOutput toOutputSumary(Post post) {
        if (post == null) return null;

        return new PostSummaryOutput(
                post.getId(),
                post.getTitle(),
                summarize(post.getBody()),
                post.getAuthor()
        );
    }

    private static String summarize(String text) {
        if (text == null || text.isBlank()) return "";

        // Considera tanto \n (Unix) quanto \r\n (Windows)
        String[] lines = text.split("\\r?\\n");

        int limit = Math.min(lines.length, 3);
        return String.join("\n", java.util.Arrays.copyOfRange(lines, 0, limit));
    }

}
