package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;

import java.util.Optional;

/**
 * 댓글 Repository
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    /**
     * 댓글 수정을 위한 조회
     * @param commentId     댓글 id
     * @param email         사용자 email
     */
    Optional<Comment> findByIdAndAccountEmail(Long commentId, String email);

    /**
     * 게시글에 관련된 댓글 삭제
     * @param boardId     게시글 id
     */
    void deleteByBoard(Long boardId);
}
