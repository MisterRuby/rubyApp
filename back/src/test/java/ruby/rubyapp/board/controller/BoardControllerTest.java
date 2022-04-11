package ruby.rubyapp.board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ruby.rubyapp.BoardBaseTest;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;
import ruby.rubyapp.config.oauth.CustomOAuth2UserService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(username = "test1", roles = "USER")
@AutoConfigureMockMvc
@MockBeans({
        @MockBean(CustomOAuth2UserService.class)
})
public class BoardControllerTest extends BoardBaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Test
    @DisplayName("게시글 목록 전체 조회")
    public void getBoards() throws Exception {
        String searchType = "TITLE";
        String searchWord = "";
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
    @DisplayName("게시글 목록 전체 조회")
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
    @DisplayName("게시글 단건 조회")
    public void getBoard() throws Exception {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Long boardId = boardList.getContent().get(0).getId();

        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========조회 시작==============");

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
}
