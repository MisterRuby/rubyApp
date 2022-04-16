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
import java.util.Optional;

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
    public BoardDto addComment(@RequestBody @Valid CommentDto commentDto, Errors errors, @LoginAccount SessionAccount account) {
        if (errors.hasErrors()) {
            return new BoardDto();
        }

        Comment comment = commentService.addComment(commentDto.getContent(), account.getEmail(), commentDto.getBoardId());

        if (comment != null) {
            Optional<Board> optionalBoard = boardService.getBoard(commentDto.getBoardId());
            return optionalBoard.map(BoardDto::new).orElseGet(BoardDto::new);
        }

        return new BoardDto();
    }

    /**
     * 댓글 수정
     * @param commentId     댓글 id
     * @param commentDto    댓글 수정 내용
     * @param account       작성자(접속자) 계정 정보
     * @return
     */
    @PatchMapping("/{commentId}")
    public BoardDto updateComment(
            @PathVariable Long commentId, @RequestBody @Valid CommentDto commentDto, Errors errors, @LoginAccount SessionAccount account) {
        if (errors.hasErrors()) {
            return new BoardDto();
        }

        Optional<Comment> optionalComment = commentService.updateComment(commentDto.getContent(), account.getEmail(), commentId);

        if (optionalComment.isPresent()) {
            Optional<Board> optionalBoard = boardService.getBoard(commentDto.getBoardId());
            return optionalBoard.map(BoardDto::new).orElseGet(BoardDto::new);
        }

        return new BoardDto();
    }

    @DeleteMapping("/{commentId}")
    public CommentDto deleteComment (@PathVariable Long commentId, @LoginAccount SessionAccount account) {
        Long deleteCommentId = commentService.deleteComment(commentId, account.getEmail());

        return CommentDto.builder().id(deleteCommentId).build();
    }
}
