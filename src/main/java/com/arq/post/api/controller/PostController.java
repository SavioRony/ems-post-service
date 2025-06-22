package com.arq.post.api.controller;

import com.arq.post.api.model.PostInput;
import com.arq.post.api.model.PostOutput;
import com.arq.post.api.model.PostSummaryOutput;
import com.arq.post.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostOutput> createPost(@RequestBody @Valid PostInput postInput) {
        postService.createPost(postInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostOutput> getPostById(@PathVariable String postId) {
        Optional<PostOutput> post = postService.findById(postId);
        return post.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<PostSummaryOutput>> listPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostSummaryOutput> posts = postService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(posts);
    }
}
