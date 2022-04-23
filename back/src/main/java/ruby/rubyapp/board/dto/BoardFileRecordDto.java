package ruby.rubyapp.board.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardFileRecordDto {

    private Long id;
    private String originFileName;
    private Long fileSize;
}
