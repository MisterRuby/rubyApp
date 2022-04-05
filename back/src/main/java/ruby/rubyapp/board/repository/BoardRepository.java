package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.Board;

/**
 * 게시글 Repository
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
}
