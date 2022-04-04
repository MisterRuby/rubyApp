package ruby.rubyapp.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.entity.Board;

import javax.persistence.EntityManager;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback(value = false)
class BoardServiceImplTest {

    @Autowired
    EntityManager em;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BoardService boardService;

    @BeforeEach
    public void setUp(){

        for (int i = 1; i <= 5; i++) {
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

    @Test
    @DisplayName("제목, 내용, 사용자 정보가 모두 있을 때 게시글 등록 성공")
    public void successRegisterBoard() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.register(title, content, email);

        // 저장한 게시글의 id로 조회
        Optional<Board> board1 = boardService.getBoard(board.getId());
        
        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(board).isEqualTo(board1.get());
    }

    @Test
    @DisplayName("제목이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByTitle() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "  ";
        String content = "테스트 내용 등록입니다.";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.register(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("내용이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByContent() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "            ";
        String email = "test1@naver.com";

        // 게시글 저장
        Board board = boardService.register(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(board.getId()).isNull();
    }

    @Test
    @DisplayName("이메일이 빈 값일 경우 등록 실패")
    public void failRegisterBoardByEmail() {
        // 제목과 내용, 사용자 정보 셋팅
        String title = "테스트 등록";
        String content = "테스트 내용 등록입니다.";
        String email = "   ";

        // 게시글 저장
        Board board = boardService.register(title, content, email);

        // 저장된 게시글과 조회한 게시글 확인
        Assertions.assertThat(board.getId()).isNull();
    }
}