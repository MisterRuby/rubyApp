package ruby.rubyapp.account.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class AccountController {

    /**
     * 로그인 성공 후 메인 페이지 이동
     * @param account       사용자 정보
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/")
    public ResponseEntity login(@LoginAccount SessionAccount account) throws URISyntaxException {
        URI redirectUri = new URI("http://localhost:3000");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity(account, httpHeaders, HttpStatus.SEE_OTHER);
    }

    // 로그인 체크는 account 의 값을 확인하여 각각의 메서드에서 확인
//    @GetMapping("/checkLogin")
//    public AccountDto loginCheck(@LoginAccount SessionAccount account) {
//        return account != null ? new AccountDto(account.getName(), account.getEmail()) : null;
//    }
}
