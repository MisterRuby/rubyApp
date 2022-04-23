package ruby.rubyapp.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ruby.rubyapp.account.entity.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

/**
 * 댓글 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column(name = "comment_id")
    private Long id;
    @Column(length = 1000)
    private String content;
    @Column(nullable = false)
    private LocalDateTime reportingDate;

    /** 연관관계 */
    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public Comment(String content, Board board, Account account) {
        this.content = content;
        this.board = board;
        this.account = account;
        this.reportingDate = LocalDateTime.now();
    }

    /** 연관관계 메서드 */

    /** 비즈니스 메서드 */
    public void update(String content) {
        this.content = content;
    }
}
