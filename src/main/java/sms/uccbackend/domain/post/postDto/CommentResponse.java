package sms.uccbackend.domain.post.postDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.post.postEntity.Comment;

import java.time.LocalDateTime;

@Builder
@Getter
public class CommentResponse {
    private Long id;
    private Long postId;
    private Long authorId;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .authorId(comment.getAuthorId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
