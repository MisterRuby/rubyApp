package ruby.rubyapp.board.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import ruby.rubyapp.board.entity.Board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 목록 Dto
 */
@Getter @Setter
@NoArgsConstructor
public class BoardListDto {

    private List<BoardDto> boardList;
    private int pageNum;
    private int totalPages;

    public BoardListDto(Page<Board> boardList) {
        this.boardList = boardList.getContent().stream()
                .map(board -> BoardDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .reportingDate(board.getReportingDate())
                        .name(board.getAccount().getName())
                        .build()
                )
                .collect(Collectors.toList());
        this.pageNum = boardList.getNumber();
        this.totalPages =  boardList.getTotalPages();
    }
}
