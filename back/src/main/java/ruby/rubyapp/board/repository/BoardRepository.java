package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
