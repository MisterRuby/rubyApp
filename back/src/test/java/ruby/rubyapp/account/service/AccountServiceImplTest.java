package ruby.rubyapp.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import ruby.rubyapp.account.AccountBaseTest;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.entity.AccountSearchType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 사용자 테스트
 */
class AccountServiceImplTest extends AccountBaseTest {

    @Test
    @DisplayName("관리자 목록 조회")
    void getAccountListByAdmin() {
        AccountSearchType accountRole = AccountSearchType.ADMIN;
        String email = "";
        int pageNum = 0;

        Page<Account> accounts = accountService.getAccounts(accountRole, email, pageNum);

        assertThat(accounts.getTotalElements()).isEqualTo(9);
        assertThat(accounts.getContent()).extracting("name")
                .containsExactly("test9", "test8", "test7", "test6", "test5",
                        "test4", "test3", "test2", "test1");
        assertThat(pageNum).isEqualTo(accounts.getNumber());
    }

    @Test
    @DisplayName("일반 사용자 목록 조회")
    void getAccountListByUser() {
        AccountSearchType accountRole = AccountSearchType.USER;
        String email = "";
        int pageNum = 1;

        Page<Account> accounts = accountService.getAccounts(accountRole, email, pageNum);

        assertThat(accounts.getTotalElements()).isEqualTo(101);
        assertThat(accounts.getContent()).extracting("name")
                .containsExactly("test100", "test99", "test98", "test97", "test96",
                        "test95", "test94", "test93", "test92", "test91");
        assertThat(pageNum).isEqualTo(accounts.getNumber());
    }

    @Test
    @DisplayName("사용정지된 사용자 목록 조회")
    void getAccountListByBlock() {
        AccountSearchType accountRole = AccountSearchType.BLOCK;
        String email = "";
        int pageNum = 0;

        Page<Account> accounts = accountService.getAccounts(accountRole, email, pageNum);

        assertThat(accounts.getTotalElements()).isEqualTo(2);
        assertThat(accounts.getContent()).extracting("name")
                .containsExactly("test112", "test111");
        assertThat(pageNum).isEqualTo(accounts.getNumber());
    }

    @Test
    @DisplayName("없는 이메일 주소로 검색")
    void getAccountListByWrongEmail() {
        AccountSearchType accountRole = AccountSearchType.USER;
        String email = "wrong@naver.com";
        int pageNum = 0;

        Page<Account> accounts = accountService.getAccounts(accountRole, email, pageNum);

        assertThat(accounts.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 페이지 번호로 조회")
    void getAccountListByWrongPageNum() {
        AccountSearchType accountRole = AccountSearchType.BLOCK;
        String email = "";
        int pageNum = 1;

        Page<Account> accounts = accountService.getAccounts(accountRole, email, pageNum);

        assertThat(accounts.getTotalElements()).isEqualTo(2);
        assertThat(accounts.getContent().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자 권한 변경")
    void updateAccountRole() {
        Long accountId = 15L;
        AccountRole beforeRole = accountRepository.findById(accountId).get().getRole();
        AccountRole accountRole = AccountRole.BLOCK;

        accountService.updateAccountRole(accountId, accountRole).get();

        em.flush();
        em.clear();

        AccountRole afterRole = accountRepository.findById(accountId).get().getRole();

        assertThat(beforeRole).isEqualTo(AccountRole.USER);
        assertThat(afterRole).isEqualTo(AccountRole.BLOCK);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 권한 변경")
    void failUpdateAccountRoleWrongAccount() {
        Long accountId = 120L;
        AccountRole accountRole = AccountRole.BLOCK;

        Account account = accountService.updateAccountRole(accountId, accountRole).orElse(null);

        assertThat(account).isNull();
    }
}