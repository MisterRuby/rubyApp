package ruby.rubyapp.board.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;

import java.io.IOException;
import java.util.List;
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
     * @param files         업로드 파일 목록
     * @return              등록된 게시글
     */
    Board addBoard(String title, String content, String accountEmail, List<MultipartFile> files) throws IOException;

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

    /**
     * 게시글 수정
     * @param boardId       게시글 id
     * @param title         게시글 제목
     * @param content       게시글 내용
     * @param email         작성자 email
     * @return
     */
    Optional<Board> updateBoard(Long boardId, String title, String content, String email);

    /**
     * 게시글 삭제
     * @param boardId       게시글 id
     * @param email         작성자 email
     * @return
     */
    Long deleteBoard(Long boardId, String email);
}
