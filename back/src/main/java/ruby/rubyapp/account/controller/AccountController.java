package ruby.rubyapp.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ruby.rubyapp.account.dto.AccountDto;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final HttpSession httpSession;

    /**
     * http://localhost:3000 으로 리다이렉트
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/")
    public ResponseEntity<Void> index() throws URISyntaxException {
        URI redirectUri = new URI("http://localhost:3000");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    /**
     * 로그인 상태 체크
     * @param account       사용자 세션 정보
     * @return
     */
    @GetMapping("/accounts")
    public AccountDto checkLogin(@LoginAccount SessionAccount account) {
        return account != null ? new AccountDto(account.getName(), account.getEmail()) : null;
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/accounts/logout")
    public void logout() {
        httpSession.removeAttribute("account");
    }
}