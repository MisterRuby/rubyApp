package ruby.rubyapp.board.service;

import ruby.rubyapp.board.entity.Board;

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
    Board registerBoard(String title, String content, String accountEmail);

    /**
     * 게시글 단건 조회
     * @param boardId
     * @return
     */
    Optional<Board> getBoard(Long boardId);
}
