package ruby.rubyapp.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ruby.rubyapp.board.dto.BoardDto;
import ruby.rubyapp.board.dto.CommentDto;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.service.BoardService;
import ruby.rubyapp.board.service.CommentService;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/comments")
@RequiredArgsConstructor
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;

    /**
     * 댓글 등록
     * @param account      작성자(접속자) 계정 정보
     * @return
     */
    @PostMapping
    public CommentDto addComment(@RequestBody @Valid CommentDto commentDto, Errors errors, @LoginAccount SessionAccount account) {
        if (errors.hasErrors()) {
            return new CommentDto();
        }

        Comment comment = commentService.addComment(commentDto.getContent(), account.getEmail(), commentDto.getBoardId());

        return CommentDto.builder().id(comment.getId()).build();
    }
}
