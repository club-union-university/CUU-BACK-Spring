package sms.uccbackend.domain.post.postController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.post.postDto.*;
import sms.uccbackend.domain.post.postEntity.BoardType;
import sms.uccbackend.domain.post.postEntity.PostCategory;
import sms.uccbackend.domain.post.postService.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // POST /events/{eventId}/posts - 행사 게시판: 게시글 작성
    @PostMapping("/api/events/{eventId}/posts")
    public ResponseEntity<PostResponse> createEventPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long eventId,
            @RequestBody PostCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(userId, BoardType.EVENT, eventId, request));
    }

    // GET /events/{eventId}/posts - 행사 게시판: 게시글 목록
    @GetMapping("/api/events/{eventId}/posts")
    public ResponseEntity<List<PostResponse>> getEventPosts(
            @PathVariable Long eventId,
            @RequestParam(required = false) PostCategory category
    ) {
        return ResponseEntity.ok(postService.getPosts(BoardType.EVENT, eventId, category));
    }

    // POST /schools/{schoolId}/posts - 학교 게시판: 게시글 작성
    @PostMapping("/api/schools/{schoolId}/posts")
    public ResponseEntity<PostResponse> createSchoolPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long schoolId,
            @RequestBody PostCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(userId, BoardType.SCHOOL, schoolId, request));
    }

    // GET /schools/{schoolId}/posts - 학교 게시판: 게시글 목록
    @GetMapping("/api/schools/{schoolId}/posts")
    public ResponseEntity<List<PostResponse>> getSchoolPosts(
            @PathVariable Long schoolId,
            @RequestParam(required = false) PostCategory category
    ) {
        return ResponseEntity.ok(postService.getPosts(BoardType.SCHOOL, schoolId, category));
    }

    // POST /clubs/{clubId}/posts - 동아리 게시판: 게시글 작성
    @PostMapping("/api/clubs/{clubId}/posts")
    public ResponseEntity<PostResponse> createClubPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long clubId,
            @RequestBody PostCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(userId, BoardType.CLUB, clubId, request));
    }

    // GET /clubs/{clubId}/posts - 동아리 게시판: 게시글 목록
    @GetMapping("/api/clubs/{clubId}/posts")
    public ResponseEntity<List<PostResponse>> getClubPosts(
            @PathVariable Long clubId,
            @RequestParam(required = false) PostCategory category
    ) {
        return ResponseEntity.ok(postService.getPosts(BoardType.CLUB, clubId, category));
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
