package ruby.rubyapp.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ruby.rubyapp.board.entity.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 엔티티
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private LocalDateTime signUpDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    public Account updateName(String name) {
        this.name = name;
        return this;
    }

    public Account updateRole(AccountRole role) {
        this.role = role;
        return this;
    }
}
