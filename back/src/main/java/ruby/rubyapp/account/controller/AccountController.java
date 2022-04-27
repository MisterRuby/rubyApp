package ruby.rubyapp.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ruby.rubyapp.account.dto.AccountDto;
import ruby.rubyapp.account.dto.AccountListDto;
import ruby.rubyapp.account.dto.AccountSearchDto;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.service.AccountService;
import ruby.rubyapp.board.dto.BoardDto;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

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

    /**
     * 권한 수정
     * @param accountId         사용자 id
     * @param accountDto        사용자 정보
     * @param errors
     * @param account
     * @return
     */
    @PatchMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(
            @PathVariable Long accountId, @RequestBody @Valid AccountDto accountDto, Errors errors, @LoginAccount SessionAccount account) {
        if (!account.getAccountRole().equals(AccountRole.ADMIN) || errors.hasErrors() || !AccountRole.isExistRole(accountDto.getRole())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Account> optionalAccount = accountService.updateAccountRole(accountId, accountDto.getRole());
        Long updatedId = optionalAccount.map(Account::getId).orElse(null);
        accountDto = AccountDto.builder().id(updatedId).build();

        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }
}