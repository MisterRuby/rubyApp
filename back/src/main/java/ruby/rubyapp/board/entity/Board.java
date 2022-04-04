package ruby.rubyapp.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ruby.rubyapp.account.entity.Account;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;
    @Column(length = 50) @NotBlank
    private String title;
    @Column(length = 3000) @NotBlank
    private String content;
    @Column
    private LocalDateTime reportingDate;

    /** 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToMany(mappedBy = "board", cascade = ALL)
    private List<Comment> commentList = new ArrayList<>();

    /** 비즈니스 메서드 */
    public Board(String title, String content, Account account) {
        this.title = title;
        this.content = content;
        this.account = account;
        this.reportingDate = LocalDateTime.now();
    }
}