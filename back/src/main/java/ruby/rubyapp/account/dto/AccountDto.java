package ruby.rubyapp.account.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 Dto
 */
@Setter @Getter
public class AccountDto {

    private String name;
    private String email;

    public AccountDto (String name, String email) {
        this.name = name;
        this.email = email;
    }
}
