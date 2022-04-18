package ruby.rubyapp.board.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    @NotBlank(message = "내용은 최소 두글자 이상이어야 합니다.")
    @Size(min = 2)
    private String content;
    private LocalDate reportingDate;
    private String name;
    private String email;
    @NotNull
    private Long boardId;
}
