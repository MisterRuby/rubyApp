package ruby.rubyapp.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ruby.rubyapp.account.entity.AccountSearchType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 사용자 검색 Dto
 */
@Setter
@Getter
@NoArgsConstructor
public class AccountSearchDto {
    @NotNull(message = "권한 조건은 반드시 지정되어야 합니다.")
    private AccountSearchType role;
    private String searchWord;
    @Min(value = 0, message = "페이지 번호는 0이상이어야 합니다.")
    private int pageNum;
}
