package ruby.rubyapp.board.service;

import org.springframework.data.domain.Page;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;

import java.util.Optional;

/**
 * 게시글 서비스
 */
public interface BoardService {

    /**
     * 게시글 등록
     * @param title         게시글 제목
     * @param content       게시글 내용
     * @param accountEmail  작성자 email
     * @return              등록된 게시글
     */
    Board addBoard(String title, String content, String accountEmail);

    /**
     * 게시글 단건, 연관된 댓글목록 조회
     * @param boardId       게시글 id
     * @return
     */
    Optional<Board> getBoard(Long boardId);

    /**
     * 게시판 목록 조회
     * @param searchType    검색 종류
     * @param searchWord    검색어
     * @param pageNum       페이지 번호
     * @return
     */
    Page<Board> getBoardList(SearchType searchType, String searchWord, int pageNum);



}
