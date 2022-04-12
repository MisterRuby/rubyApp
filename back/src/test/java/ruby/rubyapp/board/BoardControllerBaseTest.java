package ruby.rubyapp.board;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.config.oauth.CustomOAuth2UserService;
import ruby.rubyapp.config.oauth.SessionAccount;

@AutoConfigureMockMvc
@MockBeans({
        @MockBean(CustomOAuth2UserService.class)
})
public class BoardControllerBaseTest extends BoardBaseTest{

    @Autowired
    protected MockMvc mockMvc;

    protected MockHttpSession mockHttpSession;

    @Override
    @BeforeAll
    void setUp(){
        initTestAccount();
        initTestBoard();
        initTestSessionAccount();
    }

    void initTestSessionAccount() {
        Account account = Account.builder()
                .email("test1@naver.com")
                .name("test1")
                .role(AccountRole.USER)
                .build();
        SessionAccount sessionAccount = new SessionAccount(account);
        mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("account", sessionAccount);
    }
}
