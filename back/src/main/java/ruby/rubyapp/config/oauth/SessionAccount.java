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
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    public SessionAccount(Account account) {
        this.name = account.getName();
        this.email = account.getEmail();
        this.accountRole = account.getRole();
    }
}
