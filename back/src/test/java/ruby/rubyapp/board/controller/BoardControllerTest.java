package ruby.rubyapp.board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import ruby.rubyapp.board.BoardControllerBaseTest;
import ruby.rubyapp.board.dto.BoardDto;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class BoardControllerTest extends BoardControllerBaseTest {

    @Test
    @DisplayName("게시글 목록 조회")
    public void getBoards() throws Exception {
        String searchType = "TITLE";
        String searchWord = "게시글";
        String pageNum = "11";

        mockMvc.perform(
                get("/boards")
                .with(oauth2Login())
                .param("searchType", searchType)
                .param("searchWord", searchWord)
                .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(12))
                .andExpect(jsonPath("pageNum").value(11))
                .andExpect(jsonPath("boardList.length()").value(2));
    }

    @Test
    @DisplayName("게시글 목록 조회")
    public void getBoardsBySearchWord() throws Exception {
        String searchType = "TITLE";
        String searchWord = "게시글1";
        String pageNum = "1";

        mockMvc.perform(
                get("/boards")
                        .with(oauth2Login())
                        .param("searchType", searchType)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(3))
                .andExpect(jsonPath("pageNum").value(1))
                .andExpect(jsonPath("boardList.length()").value(10));
    }

    @Test
    @DisplayName("존재하지 않는 검색어로 검색")
    public void getBoardsByWrongSearchWord() throws Exception {
        String searchType = "TITLE";
        String searchWord = "게시글123123";
        String pageNum = "0";

        mockMvc.perform(
                get("/boards")
                        .with(oauth2Login())
                        .param("searchType", searchType)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum))
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(0))
                .andExpect(jsonPath("pageNum").value(0))
                .andExpect(jsonPath("boardList.length()").value(0));
    }

    @Test
    @DisplayName("두 글자 미만으로 검색")
    public void getBoardsByBlankWord() throws Exception {
        String searchType = "TITLE";
        String searchWord = "글";
        String pageNum = "0";

        mockMvc.perform(
                get("/boards")
                        .with(oauth2Login())
                        .param("searchType", searchType)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum))
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(0))
                .andExpect(jsonPath("pageNum").value(0))
                .andExpect(jsonPath("boardList").doesNotExist());
    }


    @Test
    @DisplayName("게시글 단건 조회")
    public void getBoard() throws Exception {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Long boardId = boardList.getContent().get(0).getId();

        mockMvc.perform(
                get("/boards/{id}", boardId)
                        .with(oauth2Login())
        )
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("content").exists())
                .andExpect(jsonPath("commentList.length()").value(5));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 번호 조회")
    public void getBoardByWrongBoardId() throws Exception {
        mockMvc.perform(
                get("/boards/{id}", 2131235)
                        .with(oauth2Login())
        )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist())
                .andExpect(jsonPath("title").doesNotExist())
                .andExpect(jsonPath("content").doesNotExist())
                .andExpect(jsonPath("commentList").doesNotExist());
    }

    @Test
    @DisplayName("제목, 내용이 모두 정상일 때 등록 성공")
    public void addBoard() throws Exception {
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(title);
        boardDto.setContent(content);

        mockMvc.perform(
                post("/boards")
                .session(mockHttpSession)
                .with(oauth2Login())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").exists());
        
        // 등록 성공시 등록된 게시글 id 를 반환하여 확인. id가 있다면 성공 메시지를 보여주고 목록을 다시 조회
        // 등록 실패시 id 값으로 null을 반환하여 체크
    }

    @Test
    @DisplayName("제목이 두 글자 미만일 때 등록 실패")
    public void failAddBoardWrongTitle() throws Exception {
        String title = "테";
        String content = "테스트 내용 등록입니다.";

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(title);
        boardDto.setContent(content);

        mockMvc.perform(
                post("/boards")
                        .session(mockHttpSession)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist());
    }

    @Test
    @DisplayName("내용이 두 글자 미만일 때 등록 실패")
    public void failAddBoardWrongContent() throws Exception {
        String title = "테스트";
        String content = "테";

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(title);
        boardDto.setContent(content);

        mockMvc.perform(
                post("/boards")
                        .session(mockHttpSession)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist());
    }

    @Test
    @DisplayName("게시글 수정")
    public void updateBoard() throws Exception {
        SearchType searchType = SearchType.CONTENT;
        String searchWord = "게시글1의";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Long boardId = boardList.getContent().get(0).getId();

        String title = "변경된 타이틀입니다.";
        String content = "변경된 내용입니다.";

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(title);
        boardDto.setContent(content);

        mockMvc.perform(
                patch("/boards/{boardId}", boardId)
                        .session(mockHttpSession)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").value(boardId))
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("content").value(content))
                .andExpect(jsonPath("commentList.length()").value(5));
    }

    @Test
    @DisplayName("게시글 작성자와 다른 계정으로 수정")
    public void failUpdateBoardByWrongAccount() throws Exception {
        SearchType searchType = SearchType.CONTENT;
        String searchWord = "게시글12의";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Long boardId = boardList.getContent().get(0).getId();

        String title = "변경된 타이틀입니다.";
        String content = "변경된 내용입니다.";

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(title);
        boardDto.setContent(content);

        mockMvc.perform(
                patch("/boards/{boardId}", boardId)
                        .session(mockHttpSession)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist());
    }

    @Test
    @DisplayName("최소 글자 미만의 제목으로 게시글 수정")
    public void failUpdateBoardByWrongTitle() throws Exception {
        SearchType searchType = SearchType.CONTENT;
        String searchWord = "게시글1의";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Long boardId = boardList.getContent().get(0).getId();

        String title = "";
        String content = "변경된 내용입니다.";

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(title);
        boardDto.setContent(content);

        mockMvc.perform(
                patch("/boards/{boardId}", boardId)
                        .session(mockHttpSession)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist());
    }
}
