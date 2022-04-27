package ruby.rubyapp.account.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.account.entity.AccountSearchType;
import ruby.rubyapp.account.repository.AccountRepositoryCustom;

import java.util.List;

import static ruby.rubyapp.account.entity.QAccount.account;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 사용자 목록 조회
     * @param role                  사용자 권한
     * @param searchWord            검색어
     * @param pageable              페이징 객체
     * @return
     */
    @Override
    public Page<Account> getAccounts(AccountSearchType role, String searchWord, Pageable pageable) {

        Long size = queryFactory.select(account.count())
                .from(account)
                .where(getAccountSearchCondition(role, searchWord))
                .fetchOne();

        List<Account> accountList = queryFactory.selectFrom(account)
                .where(getAccountSearchCondition(role, searchWord))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(account.signUpDate.desc())
                .fetch();

        return new PageImpl<>(accountList, pageable, size);
    }

    public BooleanExpression getAccountSearchCondition(AccountSearchType role, String searchWord) {
        if (AccountSearchType.ALL == role) {
            return account.email.contains(searchWord);
        }
        if (AccountSearchType.ADMIN == role) {
            return account.email.contains(searchWord).and(account.role.eq(AccountRole.ADMIN));
        }
        if (AccountSearchType.USER == role) {
            return account.email.contains(searchWord).and(account.role.eq(AccountRole.USER));
        }
        if (AccountSearchType.BLOCK == role) {
            return account.email.contains(searchWord).and(account.role.eq(AccountRole.BLOCK));
        }

        return null;
    }
}
