package ruby.rubyapp.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ruby.rubyapp.account.dto.AccountDto;
import ruby.rubyapp.account.dto.AccountListDto;
import ruby.rubyapp.account.dto.AccountSearchDto;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.service.AccountService;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final HttpSession httpSession;
    private final AccountService accountService;

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
    @GetMapping("/accounts/check")
    public AccountDto checkLogin(@LoginAccount SessionAccount account) {
        return account != null ? new AccountDto(account.getId(), account.getName(), account.getEmail(), account.getAccountRole()) : null;
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/accounts/logout")
    public void logout() {
        httpSession.removeAttribute("account");
    }
    
    // TODO - 회원 목록 조회, 권한 수정

    /**
     * 회원 목록 조회
     * @param searchDto
     * @param account
     */
    @GetMapping("/accounts")
    public ResponseEntity<AccountListDto> getAccounts(AccountSearchDto searchDto, @LoginAccount SessionAccount account) {
        if (!account.getAccountRole().equals(AccountRole.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Page<Account> accounts = accountService.getAccounts(searchDto.getRole(), searchDto.getSearchWord(), searchDto.getPageNum());
        AccountListDto accountListDto = new AccountListDto(accounts);

        return new ResponseEntity<>(accountListDto, HttpStatus.OK);
    }
}