package ruby.rubyapp.config.oauth;

import lombok.Getter;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * 인증된 사용자 정보
 */
@Getter
public class SessionAccount implements Serializable {
    private final Long id;
    private final String name;
    private final String email;
    @Enumerated(EnumType.STRING)
    private final AccountRole accountRole;

    public SessionAccount(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.accountRole = account.getRole();
    }
}
