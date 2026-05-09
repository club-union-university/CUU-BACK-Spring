package sms.uccbackend.domain.post.postRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.post.postEntity.BoardType;
import sms.uccbackend.domain.post.postEntity.Post;
import sms.uccbackend.domain.post.postEntity.PostCategory;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoardTypeAndTargetIdOrderByCreatedAtDesc(BoardType boardType, Long targetId);

    List<Post> findByBoardTypeAndTargetIdAndCategoryOrderByCreatedAtDesc(BoardType boardType, Long targetId, PostCategory category);

    List<Post> findByAuthorId(Long authorId);
}
