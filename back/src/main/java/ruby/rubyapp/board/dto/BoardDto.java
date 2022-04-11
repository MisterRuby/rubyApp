package ruby.rubyapp.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ruby.rubyapp.board.entity.Board;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter @Setter
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime reportingDate;
    private String username;
    private String userEmail;
    private List<CommentDto> commentList;

    public BoardDto (Optional<Board> optionalBoard) {
        optionalBoard.ifPresent(board -> {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.reportingDate = board.getReportingDate();
            this.username = board.getAccount().getName();
            this.userEmail = board.getAccount().getEmail();
            this.commentList = board.getCommentList().stream()
                    .map(comment -> new CommentDto(
                            comment.getId(), comment.getContent(), comment.getReportingDate(),
                            comment.getAccount().getName(), comment.getAccount().getEmail()
                    ))
                    .collect(Collectors.toList());
        });
    }

    @Getter @Setter
    @AllArgsConstructor
    class CommentDto {
        private Long id;
        private String content;
        private LocalDateTime reportingDate;
        private String username;
        private String userEmail;
    }
}
