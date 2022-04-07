package ruby.rubyapp.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import ruby.rubyapp.BoardBaseTest;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        Board savedBoard = boardService.addBoard(title, content, email);

        // 저장한 게시글의 id로 조회
//        Board searchBoard = boardService.getBoard(savedBoard.getId()).get();

        Board searchBoard = boardRepository.findById(savedBoard.getId()).get();

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(savedBoard).isEqualTo(searchBoard);
    }

    @Test
    @DisplayName("제목이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByTitle() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "  ";
        String content = "테스트 내용 등록입니다.";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.addBoard(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("내용이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByContent() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "            ";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.addBoard(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("이메일이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByEmail() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";
        String email = "   ";

        // 게시글 저장
        Board board = boardService.addBoard(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("게시글 제목으로 검색")
    public void getBoardListByTitle() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글11";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(4);        // 11, 110, 111, 112
        assertThat(boardList.getContent()).extracting("title")
                .containsExactly("게시글112", "게시글111", "게시글110" , "게시글11");
        assertThat(pageNum).isEqualTo(boardList.getNumber());
    }

    @Test
    @DisplayName("없는 게시글 제목으로 검색")
    public void getBoardListByNoneTitle() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글1000";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 내용, 페이지 번호로 검색")
    public void getBoardListByContent() {
        SearchType searchType = SearchType.CONTENT;
        String searchWord = "7의 본문";
        int pageNum = 1;

        // 7, 17, 27, 37, 47, 57, 67, 77, 87, 97, 107
        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(11);
        assertThat(boardList.getContent().size()).isEqualTo(1);
        assertThat(boardList.getContent()).extracting("title")
                .containsExactly("게시글7");
        assertThat(pageNum).isEqualTo(boardList.getNumber());

        pageNum = 0;
        boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getContent().size()).isEqualTo(10);
        assertThat(boardList.getContent()).extracting("title")
                .containsExactly("게시글107", "게시글97", "게시글87", "게시글77", "게시글67",
                        "게시글57","게시글47","게시글37","게시글27","게시글17");
        assertThat(pageNum).isEqualTo(boardList.getNumber());
    }

    @Test
    @DisplayName("없는 게시글 내용, 페이지 번호로 검색")
    public void getBoardListByNoneContent() {
        SearchType searchType = SearchType.CONTENT;
        String searchWord = "?????";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(0);

        searchWord = "7의 본문";
        pageNum = 2;

        boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(11);
        assertThat(boardList.getContent().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자이름으로 검색")
    public void getBoardListByUsername() {
        SearchType searchType = SearchType.USERNAME;
        String searchWord = "test9";
        int pageNum = 0;

        // 9, 90~99
        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(11);
        assertThat(boardList.getContent().size()).isEqualTo(10);
        assertThat(boardList.getContent()).extracting("account.name")
                .containsExactly("test99", "test98", "test97", "test96", "test95",
                                "test94", "test93", "test92", "test91", "test90");
        assertThat(pageNum).isEqualTo(boardList.getNumber());

        pageNum = 1;
        boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getContent().size()).isEqualTo(1);
        assertThat(boardList.getContent()).extracting("account.name")
                .containsExactly("test9");
        assertThat(pageNum).isEqualTo(boardList.getNumber());
    }

    @Test
    @DisplayName("사용자이름으로 검색")
    public void getBoardListByNoneUsername() {
        SearchType searchType = SearchType.USERNAME;
        String searchWord = "test7213";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);

        assertThat(boardList.getTotalElements()).isEqualTo(0);
    }

    // TODO - 게시글 단건 조회 구현

    @Test
    @DisplayName("게시글 단건 조회")
    public void getBoard() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Long boardId = boardList.getContent().get(0).getId();

        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========조회 시작==============");

        Board board = boardService.getBoard(boardId).get();
        assertThat(board.getId()).isEqualTo(boardId);
        assertThat(board.getCommentList().size()).isEqualTo(5);
        assertThat(board.getCommentList().get(3).getWriter()).isEqualTo("test4");
        assertThat(board.getAccount().getName()).isEqualTo("test110");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 ID로 조회")
    public void failGetBoard() {
        Long boardId = 123124515L;
        Optional<Board> boardOptional = boardService.getBoard(boardId);

        assertThat(boardOptional.isEmpty()).isTrue();
    }
}