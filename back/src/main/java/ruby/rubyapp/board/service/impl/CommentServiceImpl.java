package ruby.rubyapp.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
public class CommentServiceImpl implements CommentService {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     * @param content       댓글 내용
     * @param accountEmail  작성자 email
     * @param boardId       게시글 id
     * @return 등록된 댓글
     */
    @Override
    public Comment registerComment(String content, String accountEmail, Long boardId) {
        if (BoardValidation.validateRegisterComment(content, accountEmail, boardId)) return new Comment();

        Optional<Account> optionalAccount = accountRepository.findByEmail(accountEmail);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        
        if (optionalAccount.isPresent() && optionalBoard.isPresent()) {
            Comment comment = new Comment(content, optionalBoard.get(), optionalAccount.get());
            return commentRepository.save(comment);
        }

        return null;
    }
}
