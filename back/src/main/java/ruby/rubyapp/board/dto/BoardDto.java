package ruby.rubyapp.board.dto;

import lombok.*;
import ruby.rubyapp.board.entity.Board;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long id;
    @NotBlank(message = "제목은 최소 두글자 이상이어야 합니다.")
    @Size(min = 2)
    private String title;
    @NotBlank(message = "내용은 최소 두글자 이상이어야 합니다.")
    @Size(min = 2)
    private String content;
    private LocalDate reportingDate;
    private String name;
    private String email;
    private List<CommentDto> commentList;
    private List<BoardFileRecordDto> fileList;

    public BoardDto (Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.reportingDate = board.getReportingDate().toLocalDate();
        this.name = board.getAccount().getName();
        this.email = board.getAccount().getEmail();
        this.commentList = board.getCommentList().stream()
                .map(comment ->
                        CommentDto.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .reportingDate(comment.getReportingDate().toLocalDate())
                                .name(comment.getAccount().getName())
                                .email(comment.getAccount().getEmail())
                                .boardId(this.id)
                                .build()
                )
                .collect(Collectors.toList());
        this.fileList = board.getFileList().stream()
                .map(boardFileRecord ->
                        BoardFileRecordDto.builder()
                                .id(boardFileRecord.getId())
                                .originFileName(boardFileRecord.getOriginFileName())
                                .fileSize(boardFileRecord.getFileSize())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
