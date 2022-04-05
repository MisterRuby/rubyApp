package ruby.rubyapp.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ruby.rubyapp.BoardBaseTest;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;

import java.util.List;

/**
 * 댓글 테스트
 */
class CommentServiceImplTest extends BoardBaseTest {

    @Test
    @DisplayName("내용, 게시글 정보, 사용자 정보가 모두 있을 때 등록 성공")
    public void successRegisterComment() {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        String content = "테스트 댓글 내용 등록입니다.";
        String email = "test1@naver.com";

        // 댓글 저장
        Comment savedComment = commentService.registerComment(content, email, board.getId());
        // 저장된 댓글 조회
        Comment searchComment = commentRepository.findById(savedComment.getId()).get();

        Assertions.assertThat(savedComment).isEqualTo(searchComment);
        Assertions.assertThat(savedComment.getBoard()).isEqualTo(board);
        Assertions.assertThat(searchComment.getBoard()).isEqualTo(board);
    }

    @Test
    @DisplayName("제목이 빈 값일 경우 등록 실패")
    public void failRegisterCommentByContent() {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        String content = "    ";
        String email = "test1@naver.com";

        // 댓글 저장
        Comment comment = commentService.registerComment(content, email, board.getId());

        // 저장된 댓글 조회
        Assertions.assertThat(comment.getId()).isNull();
    }

    @Test
    @DisplayName("이메일 값이 없을 경우 등록 실패")
    public void failRegisterCommentByEmail() {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        String content = "테스트 댓글 내용 등록입니다.";
        String email = null;

        // 댓글 저장
        Comment comment = commentService.registerComment(content, email, board.getId());

        // 저장된 댓글 조회
        Assertions.assertThat(comment.getId()).isNull();
    }


    @Test
    @DisplayName("게시글 id가 없을 경우 등록 실패")
    public void failRegisterCommentByBoardId() {
        Board board = new Board();
        String content = "테스트 댓글 내용 등록입니다.";
        String email = "test1@naver.com";

        // 댓글 저장
        Comment comment = commentService.registerComment(content, email, board.getId());

        // 저장된 댓글 조회
        Assertions.assertThat(comment.getId()).isNull();
    }
}