package ruby.rubyapp.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ruby.rubyapp.board.entity.BoardSearchType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BoardSearchDto {

    @NotNull(message = "검색타입은 반드시 지정되어야 합니다.")
    private BoardSearchType searchType;
    @NotNull
    private String searchWord;
    @Min(value = 0, message = "페이지 번호는 0이상이어야 합니다.")
    private int pageNum;
}
