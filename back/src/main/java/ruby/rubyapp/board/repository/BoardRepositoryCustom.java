package ruby.rubyapp.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.BoardSearchType;

import java.util.Optional;

public interface BoardRepositoryCustom {

    /**
     * 게시판 목록 조회
     * @param boardSearchType    검색 종류
     * @param searchWord    검색어
     * @param pageable      페이징 객체
     * @return
     */
    Page<Board> getBoardList (BoardSearchType boardSearchType, String searchWord, Pageable pageable);

    /**
     * 게시글 단건 조회
     * @param boardId       게시글 id
     * @return
     */
    Optional<Board> getBoard (Long boardId);
}
