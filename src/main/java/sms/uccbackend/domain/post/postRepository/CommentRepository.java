package sms.uccbackend.domain.post.postRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.post.postEntity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    void deleteByPostId(Long postId);
}
