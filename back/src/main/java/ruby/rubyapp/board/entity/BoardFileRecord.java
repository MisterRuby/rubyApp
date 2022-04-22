package ruby.rubyapp.board.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
public class BoardFileRecord {

    @Id
    @GeneratedValue
    @Column(name = "boardFile_id")
    private Long id;
    @Column
    private String originFileName;
    @Column
    private String storedFileName;
    @Column
    private Long fileSize;
    @Column
    private LocalDateTime regDate;

    /** 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardFileRecord(String originFileName, Long fileSize, Board board) {
        this.originFileName = originFileName;
        this.fileSize = fileSize;
        this.regDate = LocalDateTime.now();
        this.board = board;
        createStoredFileName(originFileName);
    }

    /**
     * 고유 파일명 생성
     */
    void createStoredFileName(String originFileName) {
        String suffix = getSuffix(originFileName);
        this.storedFileName = UUID.randomUUID() + suffix;
    }

    /**
     * 파일의 확장자 구하기
     * @param originFileName
     * @return
     */
    private String getSuffix(String originFileName) {
        int idx = originFileName.lastIndexOf(".");
        return idx != -1 ? originFileName.substring(idx).toLowerCase() : "";
    }
}
