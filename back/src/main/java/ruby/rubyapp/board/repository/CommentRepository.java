package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.Comment;

import java.util.Optional;

/**
 * 댓글 Repository
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    /**
     * 댓글 수정을 위한 조회
     * @param commentId     댓글 id
     * @param boardId       게시글 id
     * @param email         사용자 email
     */
    Optional<Comment> findByIdAndBoardIdAndAccountEmail(Long commentId, Long boardId, String email);
}
