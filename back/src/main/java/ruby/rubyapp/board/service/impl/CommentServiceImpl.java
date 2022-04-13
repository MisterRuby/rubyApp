package ruby.rubyapp.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.repository.BoardRepository;
import ruby.rubyapp.board.repository.CommentRepository;
import ruby.rubyapp.board.service.CommentService;
import ruby.rubyapp.board.util.BoardValidation;

import java.util.Optional;

/**
 * 댓글 서비스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     * @param content       댓글 내용
     * @param email         작성자 email
     * @param boardId       게시글 id
     * @return 등록된 댓글
     */
    @Override
    public Comment addComment(String content, String email, Long boardId) {
        if (!BoardValidation.validateAddComment(content, email, boardId)) return new Comment();

        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        
        if (optionalAccount.isPresent() && optionalBoard.isPresent()) {
            Comment comment = new Comment(content, optionalBoard.get(), optionalAccount.get());
            return commentRepository.save(comment);
        }

        return new Comment();
    }

    /**
     * 댓글 수정
     * @param content           댓글 내용
     * @param email             작성자 email
     * @param commentId         댓글 id
     * @return
     */
    @Override
    public Optional<Comment> updateComment(String content, String email, Long commentId) {
        if (!BoardValidation.validateUpdateComment(content, email, commentId)) return Optional.empty();

        Optional<Comment> optionalComment = commentRepository.findByIdAndAccountEmail(commentId, email);
        optionalComment.ifPresent(comment -> comment.update(content));

        return optionalComment;
    }

    /**
     * 댓글 삭제
     * @param commentId         댓글 id
     * @param email             작성자 email
     * @return
     */
    @Override
    public Long deleteComment(Long commentId, String email) {
        Optional<Comment> optionalComment = commentRepository.findByIdAndAccountEmail(commentId, email);
        optionalComment.ifPresent(commentRepository::delete);

        return optionalComment.isPresent() ? commentId : null;
    }
}
