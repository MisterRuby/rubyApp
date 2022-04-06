package ruby.rubyapp.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;

public interface BoardRepositoryCustom {

    /**
     * 게시판 목록 조회
     * @param searchType    검색 종류
     * @param searchWord    검색어
     * @param pageable      페이징 객체
     * @return
     */
    Page<Board> getBoardList (SearchType searchType, String searchWord, Pageable pageable);
}
