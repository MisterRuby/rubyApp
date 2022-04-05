package ruby.rubyapp.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.rubyapp.account.entity.Account;

import java.util.Optional;

/**
 * 사용자 Repository
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * 이메일로 사용자 정보 조회
     * @param email     사용자 이메일
     * @return
     */
    Optional<Account> findByEmail(String email);
}
