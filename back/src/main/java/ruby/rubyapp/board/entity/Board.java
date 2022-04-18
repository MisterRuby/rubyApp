package ruby.rubyapp.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ruby.rubyapp.account.entity.Account;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * 게시글 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
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
    @OneToMany(mappedBy = "board", fetch = LAZY)
    private List<Comment> commentList = new ArrayList<>();

    public Board(String title, String content, Account account) {
        this.title = title;
        this.content = content;
        this.account = account;
        this.reportingDate = LocalDateTime.now();
    }

    /** 비즈니스 메서드 */

    /** 게시글 수정 */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
