package ruby.rubyapp.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ruby.rubyapp.account.entity.Account;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 목록 Dto
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountListDto {

    private List<AccountDto> accountList;
    private int pageNum;
    private int totalPages;

    public AccountListDto(Page<Account> accountList) {
        this.accountList = accountList.getContent().stream()
                .map(account -> AccountDto.builder()
                        .id(account.getId())
                        .name(account.getName())
                        .email(account.getEmail())
                        .accountRole(account.getRole())
                        .build()
                )
                .collect(Collectors.toList());
        this.pageNum = accountList.getNumber();
        this.totalPages = accountList.getTotalPages();
    }
}
