package ruby.rubyapp.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.board.entity.BoardFileRecord;

public interface BoardFileRepository extends JpaRepository<BoardFileRecord, Long>, BoardFileRepositoryCustom {

    // 파일 등록
    // 파일 삭제
    // 게시글에 해당하는 모든 파일 삭제 -
}
