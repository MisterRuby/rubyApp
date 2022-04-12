package ruby.rubyapp.board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ruby.rubyapp.board.BoardControllerBaseTest;
import ruby.rubyapp.board.dto.CommentDto;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CommentControllerTest extends BoardControllerBaseTest {

    @Test
    @DisplayName("게시글에 댓글 등록")
    public void addComment() throws Exception {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        Long boardId = board.getId();
        String content = "테스트 댓글 내용 등록입니다.";

        CommentDto commentDto = CommentDto.builder()
                .boardId(boardId)
                .content(content)
                .build();

        mockMvc.perform(
                post("/comments")
                .session(mockHttpSession)
                .with(oauth2Login())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").exists());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 번호에 댓글 등록")
    public void failAddCommentWrongBoardId() throws Exception {
        Long boardId = 123124154L;
        String content = "테스트 댓글 내용 등록입니다.";

        CommentDto commentDto = CommentDto.builder()
                .boardId(boardId)
                .content(content)
                .build();

        mockMvc.perform(
                post("/comments")
                        .session(mockHttpSession)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist());
    }

    @Test
    @DisplayName("두 글자 미만의 내용의 댓글을 등록")
    public void failAddCommentWrongContent() throws Exception {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        Long boardId = board.getId();
        String content = "테";

        CommentDto commentDto = CommentDto.builder()
                .boardId(boardId)
                .content(content)
                .build();

        mockMvc.perform(
                post("/comments")
                        .session(mockHttpSession)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
        )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist());
    }
}
