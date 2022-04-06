package ruby.rubyapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.repository.BoardRepository;
import ruby.rubyapp.board.repository.CommentRepository;
import ruby.rubyapp.board.service.BoardService;
import ruby.rubyapp.board.service.CommentService;

import javax.persistence.EntityManager;

/**
 * Board 테스트 기본 셋팅
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Disabled
@Transactional
public class BoardBaseTest {
    @Autowired
    protected EntityManager em;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected BoardService boardService;
    @Autowired
    protected CommentService commentService;

    @BeforeEach
    public void setUp(){
        initTestAccount();
        initTestBoard();
    }


    // 테스트 계정 생성
    private void initTestAccount() {
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
    private void initTestBoard() {
        for (int i = 1; i <= 112; i++) {
            String title = "게시글" + i;
            String content = title + "의 본문입니다.";
            String email = "test" + i + "@naver.com";
            Account account = accountRepository.findByEmail(email).get();

            Board board = new Board(title, content, account);
            boardRepository.save(board);

            if (i % 2 == 0) initTestComment(board);
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
