package ruby.rubyapp.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ruby.rubyapp.account.entity.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

/**
 * 댓글 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column(name = "comment_id")
    private Long id;
    @Column(length = 1000)
    private String content;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private LocalDateTime reportingDate;

    /** 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public Comment(String content, Board board, Account account) {
        this.content = content;
        this.writer = account.getName();
        this.board = board;
        this.account = account;
        this.reportingDate = LocalDateTime.now();
    }

    /** 비즈니스 메서드 */
}
