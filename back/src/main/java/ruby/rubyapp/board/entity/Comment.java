package ruby.rubyapp.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ruby.rubyapp.account.entity.Account;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private LocalDate reportingDate;

    /** 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
