package ruby.rubyapp.board;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {

    @GetMapping
    public void getBoards(@LoginAccount SessionAccount account)  {
        System.out.println(account.getEmail());
    }
}
