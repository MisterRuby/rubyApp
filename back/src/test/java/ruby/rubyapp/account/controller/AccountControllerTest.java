package ruby.rubyapp.account.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.config.oauth.CustomOAuth2UserService;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@MockBeans({
        @MockBean(CustomOAuth2UserService.class)
})
@Transactional
public class AccountControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AccountController accountController;

    @BeforeAll
    void setUp(){
        initTestAccount();
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

    @Test
    @DisplayName("로그인 확인")
    public void login() throws Exception {
        mockMvc.perform(
                get("/")
                .with(oauth2Login())
        )
                .andDo(print());
    }
}
