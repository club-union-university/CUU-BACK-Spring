package sms.uccbackend.domain.post.postDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.post.postEntity.BoardType;
import sms.uccbackend.domain.post.postEntity.Post;
import sms.uccbackend.domain.post.postEntity.PostCategory;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostResponse {
    private Long id;
    private Long authorId;
    private BoardType boardType;
    private Long targetId;
    private PostCategory category;
    private Boolean isOfficialNotice;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .boardType(post.getBoardType())
                .targetId(post.getTargetId())
                .category(post.getCategory())
                .isOfficialNotice(post.getIsOfficialNotice())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
