package ruby.rubyapp.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ruby.rubyapp.board.dto.BoardDto;
import ruby.rubyapp.board.dto.BoardListDto;
import ruby.rubyapp.board.dto.BoardSearchDto;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;
import ruby.rubyapp.board.service.BoardService;

import java.util.Optional;

@RestController
@RequestMapping(value = "/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public BoardListDto getBoards(BoardSearchDto searchDto) {
        SearchType type = SearchType.valueOf(searchDto.getSearchType());

        Page<Board> boardList = boardService.getBoardList(type, searchDto.getSearchWord(), searchDto.getPageNum());

        return new BoardListDto(boardList);
    }

    @GetMapping("/{boardId}")
    public BoardDto getBoard(@PathVariable Long boardId) {
        Optional<Board> optionalBoard = boardService.getBoard(boardId);

        return new BoardDto(optionalBoard);
    }
}
