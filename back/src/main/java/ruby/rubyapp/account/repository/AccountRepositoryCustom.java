package ruby.rubyapp.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.entity.AccountSearchType;

public interface AccountRepositoryCustom {

    /**
     * 사용자 목록 조회
     * @param role                  사용자 권한
     * @param searchWord            검색어
     * @param pageable              페이징 객체
     * @return
     */
    Page<Account> getAccounts (AccountRole role, String searchWord, Pageable pageable);
}
