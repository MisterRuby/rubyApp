package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.BoardFileRecord;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFileRecord, Long>, BoardFileRepositoryCustom {

    /**
     * 게시글에 관련된 모든 파일목록 조회
     * @param boardId       게시글 id
     * @return
     */
    List<BoardFileRecord> findByBoardId(Long boardId);
}
