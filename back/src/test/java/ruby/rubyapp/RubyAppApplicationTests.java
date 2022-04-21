package ruby.rubyapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.BoardControllerBaseTest;
import ruby.rubyapp.board.repository.BoardRepository;
import ruby.rubyapp.board.repository.CommentRepository;

@SpringBootTest
class RubyAppApplicationTests extends BoardControllerBaseTest {

    @Test
    void contextLoads() {

    }

}
