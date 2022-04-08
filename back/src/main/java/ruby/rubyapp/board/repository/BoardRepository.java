package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.Board;

import java.util.Optional;

/**
 * 게시글 Repository
 */
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    /**
     * 게시글 수정을 위한 단건 조회
     * @param boardId       게시글 id
     * @param email         사용자 email
     */
    Optional<Board> findByIdAndAccountEmail(Long boardId, String email);
}
