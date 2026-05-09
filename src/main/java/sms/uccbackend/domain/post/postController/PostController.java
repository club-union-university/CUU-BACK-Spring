package sms.uccbackend.domain.post.postController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.post.postDto.*;
import sms.uccbackend.domain.post.postEntity.PostCategory;
import sms.uccbackend.domain.post.postService.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // POST /events/{eventId}/posts - 게시글 작성
    @PostMapping("/api/events/{eventId}/posts")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long eventId,
            @RequestBody PostCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(userId, eventId, request));
    }

    // GET /events/{eventId}/posts - 게시글 목록
    @GetMapping("/api/events/{eventId}/posts")
    public ResponseEntity<List<PostResponse>> getPosts(
            @PathVariable Long eventId,
            @RequestParam(required = false) PostCategory category
    ) {
        return ResponseEntity.ok(postService.getPosts(eventId, category));
    }

    // GET /posts/{postId} - 게시글 단건 조회
    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // PATCH /posts/{postId} - 게시글 수정
    @PatchMapping("/api/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postService.updatePost(userId, postId, request));
    }

    // DELETE /posts/{postId} - 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }

    // POST /posts/{postId}/comments - 댓글 작성
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createComment(userId, postId, request));
    }

    // GET /posts/{postId}/comments - 댓글 목록
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getComments(postId));
    }

    // DELETE /comments/{commentId} - 댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long commentId
    ) {
        postService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }

}
