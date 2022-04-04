package ruby.rubyapp.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.repository.BoardRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;

    /**
     * 게시글 등록
     * @param title         게시글 제목
     * @param content       게시글 내용
     * @param accountEmail  작성자 email
     * @return              등록된 게시글
     */
    @Override
    public Board register(String title, String content, String accountEmail) {
        if (title == null || title.isBlank()) return new Board();
        if (content == null || content.isBlank()) return new Board();
        if (accountEmail == null || accountEmail.isBlank()) return new Board();

        Optional<Account> optionalAccount = accountRepository.findByEmail(accountEmail);

        if (optionalAccount.isPresent()) {
            Board board = new Board(title, content, optionalAccount.get());
            return boardRepository.save(board);
        }

        return new Board();
    }

    /**
     * 게시글 단건 조회
     * @param boardId   게시글 id
     * @return          게시글 단건
     */
    @Override
    public Optional<Board> getBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }
}
