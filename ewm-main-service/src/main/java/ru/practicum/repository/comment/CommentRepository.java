package ru.practicum.repository.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEvent_IdOrderById(Long eventId, Pageable pageable);

    @Query(value = "select user_id from comments where id = ?1", nativeQuery = true)
    Long findCommentAuthorId(Long commentId);

    List<Comment> findAllByUser_IdOrderById(Long userId);
}
