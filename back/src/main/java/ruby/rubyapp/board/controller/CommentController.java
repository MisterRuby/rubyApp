package ruby.rubyapp.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ruby.rubyapp.board.dto.CommentDto;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.service.CommentService;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import javax.validation.Valid;
import java.util.Optional;

/**
 * 게시판 댓글 Controller
 */
@RestController
@RequestMapping(value = "/comments")
@RequiredArgsConstructor
public class CommentController {

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

    /**
     * 댓글 수정
     * @param commentId     댓글 id
     * @param commentDto    댓글 수정 내용
     * @param account       작성자(접속자) 계정 정보
     * @return
     */
    @PatchMapping("/{commentId}")
    public CommentDto updateComment(
            @PathVariable Long commentId, @RequestBody @Valid CommentDto commentDto, Errors errors, @LoginAccount SessionAccount account) {
        if (errors.hasErrors()) {
            return new CommentDto();
        }

        Optional<Comment> optionalComment = commentService.updateComment(commentDto.getContent(), account.getEmail(), commentId);
        Long savedCommentId = optionalComment.map(Comment::getId).orElse(null);

        return optionalComment.isPresent() ? CommentDto.builder().id(savedCommentId).build() : new CommentDto();
    }

    /**
     * 댓글 삭제
     * @param commentId     댓글 id
     * @param account       작성자(접속자) 계정 정보
     * @return
     */
    @DeleteMapping("/{commentId}")
    public CommentDto deleteComment (@PathVariable Long commentId, @LoginAccount SessionAccount account) {
        Long deleteCommentId = commentService.deleteComment(commentId, account.getEmail());

        return CommentDto.builder().id(deleteCommentId).build();
    }
}
