package ruby.rubyapp.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.repository.AccountRepository;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.SearchType;
import ruby.rubyapp.board.repository.BoardRepository;
import ruby.rubyapp.board.repository.CommentRepository;
import ruby.rubyapp.board.service.BoardService;
import ruby.rubyapp.board.util.BoardValidation;

import java.util.Optional;

/**
 * 게시글 서비스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시글 등록
     * @param title         게시글 제목
     * @param content       게시글 내용
     * @param accountEmail  작성자 email
     * @return              등록된 게시글
     */
    @Override
    public Board addBoard(String title, String content, String accountEmail) {
        if (!BoardValidation.validateAddBoard(title, content, accountEmail)) return new Board();

        Optional<Account> optionalAccount = accountRepository.findByEmail(accountEmail);

        if (optionalAccount.isPresent()) {
            Board board = new Board(title, content, optionalAccount.get());
            return boardRepository.save(board);
        }

        return new Board();
    }

    /**
     * 게시글 단건, 연관된 댓글목록 조회
     * @param boardId       게시글 id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Board> getBoard(Long boardId) {
        // 게시글 단건과 작성자 정보, 연관 댓글을 fetchjoin
        Optional<Board> optionalBoard = boardRepository.getBoard(boardId);

        // 연관댓글과 댓글 작성자 목록들을 fetchjoin
        optionalBoard.ifPresent(board ->
                commentRepository.getCommentListByBoard(board)
                        .forEach(comment -> comment.setBoard(board))
        );

        return optionalBoard;
    }


    /**
     * 게시판 목록 조회
     * @param searchType    검색 종류
     * @param searchWord    검색어
     * @param pageNum       페이지 번호
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Board> getBoardList(SearchType searchType, String searchWord, int pageNum) {
        int PAGE_PER_MAX_COUNT = 10;
        Pageable pageable = PageRequest.of(pageNum, PAGE_PER_MAX_COUNT, Sort.by(searchType.getValue()).descending());

        return boardRepository.getBoardList(searchType, searchWord, pageable);
    }

    /**
     * 게시글 수정
     * @param boardId       게시글 id
     * @param title         게시글 제목
     * @param content       게시글 내용
     * @param email         작성자 email
     * @return
     */
    @Override
    public Optional<Board> updateBoard(Long boardId, String title, String content, String email) {
        if (!BoardValidation.validateUpdateBoard(title, content, email, boardId)) return Optional.empty();

        Optional<Board> optionalBoard = boardRepository.findByIdAndAccountEmail(boardId, email);
        optionalBoard.ifPresent(board -> board.update(title, content));
        return optionalBoard;
    }

    /**
     * 게시글 삭제
     * @param boardId       게시글 id
     * @param email         작성자 email
     */
    @Override
    public void deleteBoard(Long boardId, String email) {
        Optional<Board> optionalBoard = boardRepository.findByIdAndAccountEmail(boardId, email);

        optionalBoard.ifPresent(board -> {
            commentRepository.deleteBulkComment(board);
            boardRepository.delete(board);
        });
    }
}
