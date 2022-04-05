package ruby.rubyapp.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ruby.rubyapp.BoardBaseTest;
import ruby.rubyapp.board.entity.Board;

/**
 * 게시글 테스트
 */
class BoardServiceImplTest extends BoardBaseTest {

    @Test
    @DisplayName("제목, 내용, 사용자 정보가 모두 있을 때 게시글 등록 성공")
    public void successRegisterBoard() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";
        String email = "test1@naver.com";

        // 게시글 저장
        Board savedBoard = boardService.registerBoard(title, content, email);

        // 저장한 게시글의 id로 조회
        Board searchBoard = boardService.getBoard(savedBoard.getId()).get();

        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(savedBoard).isEqualTo(searchBoard);
    }

    @Test
    @DisplayName("제목이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByTitle() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "  ";
        String content = "테스트 내용 등록입니다.";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.registerBoard(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("내용이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByContent() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "            ";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.registerBoard(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("이메일이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByEmail() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";
        String email = "   ";

        // 게시글 저장
        Board board = boardService.registerBoard(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(board.getId()).isNull();
    }
}