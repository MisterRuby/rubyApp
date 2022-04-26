package ruby.rubyapp.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import ruby.rubyapp.board.BoardBaseTest;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.entity.BoardSearchType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 게시글 테스트
 */
class BoardServiceImplTest extends BoardBaseTest {

    @Test
    @DisplayName("제목, 내용, 사용자 정보가 모두 있을 때 게시글 등록 성공")
    public void successRegisterBoard() throws IOException {
        System.out.println("BoardServiceImplTest.successRegisterBoard");
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";
        String email = "test1@naver.com";

        // 게시글 저장
        Board savedBoard = boardService.addBoard(title, content, email, new ArrayList<>());

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(savedBoard.getTitle()).isEqualTo(title);
        assertThat(savedBoard.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("제목이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByTitle() throws IOException {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "  ";
        String content = "테스트 내용 등록입니다.";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.addBoard(title, content, email, new ArrayList<>());

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("내용이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByContent() throws IOException {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "            ";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.addBoard(title, content, email, new ArrayList<>());

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("이메일이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByEmail() throws IOException {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";
        String email = "   ";

        // 게시글 저장
        Board board = boardService.addBoard(title, content, email, new ArrayList<>());

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("게시글 제목으로 검색")
    public void getBoardListByTitle() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글11";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(4);        // 11, 110, 111, 112
        assertThat(boardList.getContent()).extracting("title")
                .containsExactly("게시글112", "게시글111", "게시글110" , "게시글11");
        assertThat(pageNum).isEqualTo(boardList.getNumber());
    }

    @Test
    @DisplayName("없는 게시글 제목으로 검색")
    public void getBoardListByNoneTitle() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글1000";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 내용, 페이지 번호로 검색")
    public void getBoardListByContent() {
        BoardSearchType boardSearchType = BoardSearchType.CONTENT;
        String searchWord = "7의 본문";
        int pageNum = 1;

        // 7, 17, 27, 37, 47, 57, 67, 77, 87, 97, 107
        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(11);
        assertThat(boardList.getContent().size()).isEqualTo(1);
        assertThat(boardList.getContent()).extracting("title")
                .containsExactly("게시글7");
        assertThat(pageNum).isEqualTo(boardList.getNumber());

        pageNum = 0;
        boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getContent().size()).isEqualTo(10);
        assertThat(boardList.getContent()).extracting("title")
                .containsExactly("게시글107", "게시글97", "게시글87", "게시글77", "게시글67",
                        "게시글57","게시글47","게시글37","게시글27","게시글17");
        assertThat(pageNum).isEqualTo(boardList.getNumber());
    }

    @Test
    @DisplayName("없는 게시글 내용, 페이지 번호로 검색")
    public void getBoardListByNoneContent() {
        BoardSearchType boardSearchType = BoardSearchType.CONTENT;
        String searchWord = "?????";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(0);

        searchWord = "7의 본문";
        pageNum = 2;

        boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(11);
        assertThat(boardList.getContent().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자이름으로 검색")
    public void getBoardListByUsername() {
        BoardSearchType boardSearchType = BoardSearchType.USERNAME;
        String searchWord = "test9";
        int pageNum = 0;

        // 9, 90~99
        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(11);
        assertThat(boardList.getContent().size()).isEqualTo(10);
        assertThat(boardList.getContent()).extracting("account.name")
                .containsExactly("test99", "test98", "test97", "test96", "test95",
                                "test94", "test93", "test92", "test91", "test90");
        assertThat(pageNum).isEqualTo(boardList.getNumber());

        pageNum = 1;
        boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getContent().size()).isEqualTo(1);
        assertThat(boardList.getContent()).extracting("account.name")
                .containsExactly("test9");
        assertThat(pageNum).isEqualTo(boardList.getNumber());
    }

    @Test
    @DisplayName("없는 사용자이름으로 검색")
    public void getBoardListByNoneUsername() {
        BoardSearchType boardSearchType = BoardSearchType.USERNAME;
        String searchWord = "test7213";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 단건 조회")
    public void getBoard() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);
        Long boardId = boardList.getContent().get(0).getId();

        System.out.println("===========조회 시작==============");

        Board board = boardService.getBoard(boardId).get();
        assertThat(board.getId()).isEqualTo(boardId);
        assertThat(board.getCommentList().size()).isEqualTo(5);
        assertThat(board.getCommentList().get(3).getAccount().getName()).isEqualTo("test4");
        assertThat(board.getAccount().getName()).isEqualTo("test110");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 ID로 조회")
    public void failGetBoard() {
        Long boardId = 123124515L;
        Optional<Board> boardOptional = boardService.getBoard(boardId);

        assertThat(boardOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("게시글 수정")
    public void updateBoard() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Long boardId = board.getId();
        String email = "test110@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========수정 시작==============");

        String title = "변경된 제목";
        String content = "변경된 내용입니다.";

        boardService.updateBoard(boardId, title, content, email);

        // 저장되어 조회가 가능한지 확인
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board savedBoard = optionalBoard.get();

        // 저장된 데이터가 일치하는지 확인
        assertThat(savedBoard.getId()).isEqualTo(boardId);
        assertThat(savedBoard.getTitle()).isEqualTo(title);
        assertThat(savedBoard.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("게시글 작성자와 사용자가 달라 수정 실패")
    public void failUpdateBoardByWrongAccount() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Long boardId = board.getId();
        String email = "test5@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========수정 시작==============");

        String title = "변경된 제목";
        String content = "변경된 내용입니다.";

        Optional<Board> optionalBoard = boardService.updateBoard(boardId, title, content, email);
        assertThat(optionalBoard.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("제목이 빈 값일때 수정 실패")
    public void failUpdateBoardByBlankTitle() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Long boardId = board.getId();
        String email = "test110@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========수정 시작==============");

        String title = "   ";
        String content = "변경된 내용입니다.";

        Optional<Board> optionalBoard = boardService.updateBoard(boardId, title, content, email);
        assertThat(optionalBoard.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 시도")
    public void failUpdateBoard() {
        Long boardId = 123124515L;
        String title = "변경된 제목";
        String content = "변경된 내용입니다.";
        String email = "test110@naver.com";

        Optional<Board> optionalBoard = boardService.updateBoard(boardId, title, content, email);

        assertThat(optionalBoard.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("게시글 및 관련 댓글 모두 삭제")
    public void deleteBoard() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);
        Board board = boardService.getBoard(boardList.getContent().get(0).getId()).get();
        Long boardId = board.getId();
        Long commentId = board.getCommentList().get(0).getId();
        String email = board.getAccount().getEmail();
        em.clear();

        assertThat(board).isNotNull();

        System.out.println("===========삭제 시작==============");

        boardService.deleteBoard(boardId, email);

        em.flush();
        em.clear();

        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        assertThat(optionalBoard.isEmpty()).isTrue();
        assertThat(optionalComment.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("게시글 작성자와 사용자가 다르다면 삭제 실패")
    public void failDeleteBoardWrongAccount() {
        BoardSearchType boardSearchType = BoardSearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(boardSearchType, searchWord, pageNum);
        Board board = boardService.getBoard(boardList.getContent().get(0).getId()).get();
        Long boardId = board.getId();
        Long commentId = board.getCommentList().get(0).getId();
        String email = "wrong@naver.com";
        em.clear();

        assertThat(board).isNotNull();

        System.out.println("===========삭제 시작==============");

        boardService.deleteBoard(boardId, email);

        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        assertThat(optionalBoard.isEmpty()).isFalse();
        assertThat(optionalComment.isEmpty()).isFalse();
    }

}