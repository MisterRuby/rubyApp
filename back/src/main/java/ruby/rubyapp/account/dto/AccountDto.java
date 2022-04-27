package ruby.rubyapp.account.dto;

import lombok.*;
import ruby.rubyapp.account.entity.AccountRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 사용자 Dto
 */
@Setter @Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

}
