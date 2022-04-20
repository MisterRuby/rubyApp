package ruby.rubyapp.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ruby.rubyapp.account.entity.AccountRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 사용자 Dto
 */
@Setter @Getter
@NoArgsConstructor
public class AccountDto {

    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    public AccountDto (String name, String email, AccountRole accountRole) {
        this.name = name;
        this.email = email;
        this.accountRole = accountRole;
    }
}
