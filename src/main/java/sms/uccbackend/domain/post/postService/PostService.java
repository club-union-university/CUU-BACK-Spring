package sms.uccbackend.domain.post.postService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.event.eventEntity.EventParticipant;
import sms.uccbackend.domain.event.eventEntity.ParticipantStatus;
import sms.uccbackend.domain.event.eventRepository.EventParticipantRepository;
import sms.uccbackend.domain.event.eventRepository.EventRepository;
import sms.uccbackend.domain.notification.notificationEntity.NotificationType;
import sms.uccbackend.domain.notification.notificationService.NotificationService;
import sms.uccbackend.domain.post.postDto.*;
import sms.uccbackend.domain.post.postEntity.BoardType;
import sms.uccbackend.domain.post.postEntity.Comment;
import sms.uccbackend.domain.post.postEntity.Post;
import sms.uccbackend.domain.post.postEntity.PostCategory;
import sms.uccbackend.domain.post.postRepository.CommentRepository;
import sms.uccbackend.domain.post.postRepository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final NotificationService notificationService;

    // 게시글 작성
    @Transactional
    public PostResponse createPost(Long userId, BoardType boardType, Long targetId, PostCreateRequest request) {
        Post post = Post.builder()
                .authorId(userId)
                .boardType(boardType)
                .targetId(targetId)
                .category(request.getCategory())
                .title(request.getTitle())
                .content(request.getContent())
                .isOfficialNotice(false)
                .build();

        postRepository.save(post);

        // 행사 게시판에 NOTICE 카테고리 글이 올라가면 참여자(작성자 제외)에게 알림
        if (boardType == BoardType.EVENT && request.getCategory() == PostCategory.NOTICE) {
            notifyEventParticipantsOfNotice(targetId, userId, post);
        }

        return PostResponse.from(post);
    }

    private void notifyEventParticipantsOfNotice(Long eventId, Long authorId, Post post) {
        String eventTitle = eventRepository.findById(eventId)
                .map(e -> e.getTitle())
                .orElse("행사");

        List<EventParticipant> participants = eventParticipantRepository
                .findByEventIdAndStatus(eventId, ParticipantStatus.APPROVED);

        for (EventParticipant p : participants) {
            if (p.getUserId().equals(authorId)) continue;
            notificationService.create(
                    p.getUserId(),
                    NotificationType.POST_NOTICE,
                    "새 공지가 등록되었습니다",
                    String.format("'%s' 행사에 공지가 올라왔습니다: %s", eventTitle, post.getTitle()),
                    "/events/" + eventId + "/posts/" + post.getId()
            );
        }
    }

    // 게시글 목록 조회
    public List<PostResponse> getPosts(BoardType boardType, Long targetId, PostCategory category) {
        List<Post> posts;
        if (category != null) {
            posts = postRepository.findByBoardTypeAndTargetIdAndCategoryOrderByCreatedAtDesc(
                    boardType, targetId, category);
        } else {
            posts = postRepository.findByBoardTypeAndTargetIdOrderByCreatedAtDesc(
                    boardType, targetId);
        }
        return posts.stream().map(PostResponse::from).collect(Collectors.toList());
    }

    // 게시글 단건 조회
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return PostResponse.from(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponse updatePost(Long userId, Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getContent() != null) post.setContent(request.getContent());
        if (request.getCategory() != null) post.setCategory(request.getCategory());

        return PostResponse.from(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }

        commentRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }

    // 댓글 작성
    @Transactional
    public CommentResponse createComment(Long userId, Long postId, CommentCreateRequest request) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment comment = Comment.builder()
                .postId(postId)
                .authorId(userId)
                .content(request.getContent())
                .build();

        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    // 댓글 목록 조회
    public List<CommentResponse> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
