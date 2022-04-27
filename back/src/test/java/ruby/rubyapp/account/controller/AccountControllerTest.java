package ruby.rubyapp.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import ruby.rubyapp.account.AccountControllerBaseTest;
import ruby.rubyapp.account.dto.AccountDto;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.config.oauth.CustomOAuth2UserService;
import ruby.rubyapp.config.oauth.SessionAccount;

import javax.validation.constraints.Min;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AccountControllerTest extends AccountControllerBaseTest {

    MockHttpSession sessionAdminAccount() {
        Optional<Account> accountOptional = accountRepository.findByEmail("test1@naver.com");

        SessionAccount sessionAccount = new SessionAccount(accountOptional.get());
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("account", sessionAccount);

        return mockHttpSession;
    }

    MockHttpSession sessionUserAccount() {
        Optional<Account> accountOptional = accountRepository.findByEmail("test10@naver.com");

        SessionAccount sessionAccount = new SessionAccount(accountOptional.get());
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("account", sessionAccount);

        return mockHttpSession;
    }

    @Test
    @DisplayName("리다이렉트")
    public void login() throws Exception {
        MockHttpSession session = sessionUserAccount();
        mockMvc.perform(
                get("/")
                .session(session)
                .with(oauth2Login())
        )
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("전체 사용자 목록 조회")
    void getAccounts() throws Exception {
        MockHttpSession session = sessionAdminAccount();
        String role = "ALL";
        String searchWord = "";
        String pageNum = "11";

        mockMvc.perform(
                get("/accounts")
                        .session(session)
                        .with(oauth2Login())
                        .param("role", role)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(12))
                .andExpect(jsonPath("pageNum").value(11))
                .andExpect(jsonPath("accountList.length()").value(2));
    }


    @Test
    @DisplayName("관리자 목록 조회")
    void getAdminAccounts() throws Exception {
        MockHttpSession session = sessionAdminAccount();
        String role = "ADMIN";
        String searchWord = "";
        String pageNum = "0";

        mockMvc.perform(
                get("/accounts")
                .session(session)
                .with(oauth2Login())
                .param("role", role)
                .param("searchWord", searchWord)
                .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(1))
                .andExpect(jsonPath("pageNum").value(0))
                .andExpect(jsonPath("accountList.length()").value(9));
    }

    @Test
    @DisplayName("일반 사용자 목록 조회")
    void getUserAccounts() throws Exception {
        MockHttpSession session = sessionAdminAccount();
        String role = "USER";
        String searchWord = "";
        String pageNum = "10";

        mockMvc.perform(
                get("/accounts")
                        .session(session)
                        .with(oauth2Login())
                        .param("role", role)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(11))
                .andExpect(jsonPath("pageNum").value(10))
                .andExpect(jsonPath("accountList.length()").value(1));
    }

    @Test
    @DisplayName("일반 사용자 목록 조회")
    void getBlockAccounts() throws Exception {
        MockHttpSession session = sessionAdminAccount();
        String role = "BLOCK";
        String searchWord = "";
        String pageNum = "0";

        mockMvc.perform(
                get("/accounts")
                        .session(session)
                        .with(oauth2Login())
                        .param("role", role)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(1))
                .andExpect(jsonPath("pageNum").value(0))
                .andExpect(jsonPath("accountList.length()").value(2));
    }

    @Test
    @DisplayName("검색어를 포함한 이메일을 사용하는 관리자 목록 조회")
    void getAccountsByEmail() throws Exception {
        MockHttpSession session = sessionAdminAccount();
        String role = "ADMIN";
        String searchWord = "st7";
        String pageNum = "0";

        mockMvc.perform(
                get("/accounts")
                        .session(session)
                        .with(oauth2Login())
                        .param("role", role)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(jsonPath("totalPages").value(1))
                .andExpect(jsonPath("pageNum").value(0))
                .andExpect(jsonPath("accountList.length()").value(1));
    }

    @Test
    @DisplayName("관리자 권한이 아닌 계정으로 조회")
    void getAccountsByNotAdmin() throws Exception {
        MockHttpSession session = sessionUserAccount();
        String role = "USER";
        String searchWord = "";
        String pageNum = "0";

        mockMvc.perform(
                get("/accounts")
                        .session(session)
                        .with(oauth2Login())
                        .param("role", role)
                        .param("searchWord", searchWord)
                        .param("pageNum", pageNum)
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("권한 수정")
    void updateAccount() throws Exception {
        // given
        MockHttpSession session = sessionAdminAccount();
        Account account = accountRepository.findByEmail("test11@naver.com").get();
        AccountDto accountDto = AccountDto.builder()
                .role(AccountRole.BLOCK)
                .build();

        // when
        mockMvc.perform(
                patch("/accounts/{accountId}", account.getId())
                        .session(session)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
        )
                .andDo(print())
                .andExpect(status().isOk());

        Account updatedAccount = accountRepository.findByEmail("test11@naver.com").get();
        assertThat(updatedAccount.getRole()).isEqualTo(AccountRole.BLOCK);
    }

    @Test
    @DisplayName("관리자 권한이 아닌 계정으로 수정")
    void updateAccountByWrongRole() throws Exception {
        MockHttpSession session = sessionUserAccount();
        Account account = accountRepository.findByEmail("test99@naver.com").get();
        AccountDto accountDto = AccountDto.builder()
                .role(AccountRole.BLOCK)
                .build();

        // when
        mockMvc.perform(
                patch("/accounts/{accountId}", account.getId())
                        .session(session)
                        .with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    // TODO - login 시 BLOCK 권한의 계정은 로그인 불가능
}
