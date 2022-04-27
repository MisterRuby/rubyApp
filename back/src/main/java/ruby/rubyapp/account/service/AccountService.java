package ruby.rubyapp.account.service;

import org.springframework.data.domain.Page;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.entity.AccountSearchType;

import java.util.Optional;

public interface AccountService {

    /**
     * 회원 목록 조회
     * @param role                  사용자 권한
     * @param searchWord            검색어
     * @param pageNum               페이지 번호
     * @return
     */
    Page<Account> getAccounts (AccountSearchType role, String searchWord, int pageNum);

    /**
     * 회원 권한 수정
     * @param accountId         사용자 id
     * @param accountRole       권한
     * @return
     */
    Optional<Account> updateAccountRole (Long accountId, AccountRole accountRole);
}
