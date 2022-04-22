package ruby.rubyapp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.repository.BoardRepository;
import ruby.rubyapp.board.repository.CommentRepository;

@Configuration
@RequiredArgsConstructor
public class DummyDataConfig implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Override
    public void run(String... args) throws Exception {
        initTestAccount();
        initTestBoard();
    }

    // 테스트 계정 생성
    protected void initTestAccount() {
        for (int i = 1; i <= 112; i++) {
            String name = "test" + i;
            String email = name + "@naver.com";
            Account account = Account.builder()
                    .email(email)
                    .name(name)
                    .role(AccountRole.USER)
                    .build();
            accountRepository.save(account);
        }
    }

    // 테스트 게시글 생성
    protected void initTestBoard() {
        for (int i = 1; i <= 112; i++) {
            String title = "게시글" + i;
            String content = title + "의 본문입니다.";
            String email = "test" + i + "@naver.com";
            Account account = accountRepository.findByEmail(email).get();

            Board board = new Board(title, content, account);
            boardRepository.save(board);

            initTestComment(board);
        }
    }

    // 테스트 댓글 생성
    private void initTestComment(Board board) {
        for (int i = 1; i <= 5; i++) {
            String content = board.getTitle() + "의 " + i + "번째 댓글입니다." ;
            String email = "test" + i + "@naver.com";
            Account account = accountRepository.findByEmail(email).get();

            Comment comment = new Comment(content, board, account);
            commentRepository.save(comment);
        }
    }
}
