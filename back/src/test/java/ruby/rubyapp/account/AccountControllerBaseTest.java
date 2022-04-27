package ruby.rubyapp.account;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.web.servlet.MockMvc;
import ruby.rubyapp.config.oauth.CustomOAuth2UserService;

@AutoConfigureMockMvc
@MockBeans({
        @MockBean(CustomOAuth2UserService.class)
})
public class AccountControllerBaseTest extends AccountBaseTest{

    @Autowired
    protected MockMvc mockMvc;

    @Override
    @BeforeAll
    void setUp(){
        initTestAccount();
    }
}
