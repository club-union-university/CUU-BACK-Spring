package sms.uccbackend.domain.post.postDto;

import lombok.Getter;
import sms.uccbackend.domain.post.postEntity.PostCategory;

@Getter
public class PostUpdateRequest {
    private String title;
    private String content;
    private PostCategory category;
}
