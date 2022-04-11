package ruby.rubyapp.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ruby.rubyapp.board.entity.Board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 목록 Dto
 */
@Getter @Setter
public class BoardListDto {

    private List<BoardListItem> boardList;
    private int pageNum;
    private int totalPages;

    public BoardListDto(Page<Board> boardList) {
        this.boardList = boardList.getContent().stream()
                .map(board -> new BoardListItem(
                        board.getId(), board.getTitle(), board.getContent(), board.getReportingDate(), board.getAccount().getName()
                ))
                .collect(Collectors.toList());
        this.pageNum = boardList.getNumber();
        this.totalPages =  boardList.getTotalPages();
    }

    @AllArgsConstructor
    @Getter @Setter
    class BoardListItem {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime reportingDate;
        private String name;
    }
}
