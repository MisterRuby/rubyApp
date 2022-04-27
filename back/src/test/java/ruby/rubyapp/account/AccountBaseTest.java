package ruby.rubyapp.account;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ruby.rubyapp.account.controller.AccountController;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.account.service.AccountService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
@Disabled
public class AccountBaseTest {

    @Autowired
    protected EntityManager em;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected AccountController accountController;

    @BeforeAll
    void setUp(){
        initTestAccount();
    }

    @AfterAll
    void finish() {
        accountRepository.deleteAll();
    }

    // 테스트 계정 생성
    protected void initTestAccount() {
        for (int i = 1; i <= 112; i++) {
            String name = "test" + i;
            String email = name + "@naver.com";
            Account account = Account.builder()
                    .email(email)
                    .name(name)
                    .role(i < 10 ? AccountRole.ADMIN :
                            i > 110 ? AccountRole.BLOCK : AccountRole.USER)
                    .signUpDate(LocalDateTime.now())
                    .build();
            accountRepository.save(account);
        }
    }
}
