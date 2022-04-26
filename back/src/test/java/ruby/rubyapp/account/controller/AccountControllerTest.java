package ruby.rubyapp.account.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.web.servlet.MockMvc;
import ruby.rubyapp.account.AccountBaseTest;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.config.oauth.CustomOAuth2UserService;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@MockBeans({
        @MockBean(CustomOAuth2UserService.class)
})
public class AccountControllerTest extends AccountBaseTest {

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

    @AfterAll
    void finish() {
        accountRepository.deleteAll();
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
                    .signUpDate(LocalDateTime.now())
                    .build();
            accountRepository.save(account);
        }
    }

    @Test
    @DisplayName("리다이렉트")
    public void login() throws Exception {
        mockMvc.perform(
                get("/")
                .with(oauth2Login())
        )
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}
