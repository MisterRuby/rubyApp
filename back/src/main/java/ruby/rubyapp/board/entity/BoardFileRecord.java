package ruby.rubyapp.board.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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
    public BoardFileRecord(String originFileName, Long fileSize, LocalDateTime regDate) {
        this.originFileName = originFileName;
        this.fileSize = fileSize;
        this.regDate = regDate;
        createStoredFileName();
    }

    /**
     * 고유 파일명 생성
     */
    void createStoredFileName() {
        this.storedFileName = UUID.randomUUID().toString();
    }
}
