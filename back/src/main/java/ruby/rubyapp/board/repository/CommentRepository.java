package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.Comment;

/**
 * 댓글 Repository
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
