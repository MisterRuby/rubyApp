package ruby.rubyapp.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ruby.rubyapp.board.dto.BoardDto;
import ruby.rubyapp.board.dto.BoardListDto;
import ruby.rubyapp.board.dto.BoardSearchDto;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;
import ruby.rubyapp.board.service.BoardService;
import ruby.rubyapp.board.util.BoardValidation;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping(value = "/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 목록 조회
     * @param searchDto    검색조건
     * @return
     */
    @GetMapping
    public BoardListDto getBoards(@Valid BoardSearchDto searchDto, Errors errors) {
        if (errors.hasErrors()) {
            return new BoardListDto();
        }

        SearchType type = SearchType.valueOf(searchDto.getSearchType());

        Page<Board> boardList = boardService.getBoardList(type, searchDto.getSearchWord(), searchDto.getPageNum());

        return new BoardListDto(boardList);
    }

    /**
     * 게시글 단건 조회
     * @param boardId       게시글 id
     * @return
     */
    @GetMapping("/{boardId}")
    public BoardDto getBoard(@PathVariable Long boardId) {
        Optional<Board> optionalBoard = boardService.getBoard(boardId);

        return optionalBoard.map(BoardDto::new).orElseGet(BoardDto::new);
    }

    /**
     * 게시글 등록
     * @param boardDto      게시글 등록 정보
     * @param account       작성자(접속자) 계정 정보
     * @return
     */
    @PostMapping
    public BoardDto addBoard(
            @RequestPart("board") @Valid BoardDto boardDto, Errors errors,
            @RequestPart(name = "files" , required = false) List<MultipartFile> files, @LoginAccount SessionAccount account) throws IOException {
        if (errors.hasErrors()) {
            return new BoardDto();
        }

        if (files.size() == 0) files = new ArrayList<>();

        if (!BoardValidation.validateFileExtension(files)) return new BoardDto();

        Board savedBoard = boardService.addBoard(boardDto.getTitle(), boardDto.getContent(), account.getEmail(), files);

        return BoardDto.builder().id(savedBoard.getId()).build();
    }

    /**
     * 게시글 수정
     * @param boardId       게시글 id
     * @param boardDto      게시글 수정 정보
     * @param account       작성자(접속자) 계정 정보
     * @return
     */
    @PatchMapping("/{boardId}")
    public BoardDto updateBoard(
            @PathVariable Long boardId, @RequestBody @Valid BoardDto boardDto, Errors errors, @LoginAccount SessionAccount account) {
        if (errors.hasErrors()) {
            return new BoardDto();
        }

        Optional<Board> optionalBoard = boardService.updateBoard(boardId, boardDto.getTitle(), boardDto.getContent(), account.getEmail());

        Long updatedBoardId = optionalBoard.map(Board::getId).orElse(null);

        return BoardDto.builder().id(updatedBoardId).build();
    }

    /**
     * 게시글 삭제
     * @param boardId       게시글 id
     * @param account       작성자(접속자) 계정 정보
     * @return
     */
    @DeleteMapping("/{boardId}")
    public BoardDto deleteBoard(@PathVariable Long boardId, @LoginAccount SessionAccount account) {

        Long deleteBoardId = boardService.deleteBoard(boardId, account.getEmail());

        return BoardDto.builder().id(deleteBoardId).build();
    }
}
