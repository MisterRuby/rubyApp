package ruby.rubyapp.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.entity.AccountSearchType;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.account.service.AccountService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    /**
     * 회원 목록 조회
     * @param role              사용자 권한
     * @param searchWord        검색어
     * @param pageNum           페이지 번호
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Account> getAccounts(AccountSearchType role, String searchWord, int pageNum) {
        int PAGE_PER_MAX_COUNT = 10;
        Pageable pageable = PageRequest.of(pageNum, PAGE_PER_MAX_COUNT, Sort.by(role.name()).descending());

        return accountRepository.getAccounts(role, searchWord, pageable);
    }

    /**
     * 회원 권한 수정
     * @param accountId   사용자 id
     * @param accountRole 권한
     * @return
     */
    @Override
    public Optional<Account> updateAccountRole(Long accountId, AccountRole accountRole) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        optionalAccount.ifPresent(account -> account.updateRole(accountRole));

        return optionalAccount;
    }
}
