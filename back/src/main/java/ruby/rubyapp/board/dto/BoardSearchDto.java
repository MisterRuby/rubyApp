package ruby.rubyapp.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BoardSearchDto implements Serializable {

    private String searchType;
    private String searchWord;
    private int pageNum;
}
