package ruby.rubyapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.repository.BoardRepository;
import ruby.rubyapp.board.repository.CommentRepository;

@SpringBootTest
class RubyAppApplicationTests {

    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected CommentRepository commentRepository;

    @Test
    void contextLoads() {
        commentRepository.deleteAll();
        boardRepository.deleteAll();
        accountRepository.deleteAll();
    }

}
