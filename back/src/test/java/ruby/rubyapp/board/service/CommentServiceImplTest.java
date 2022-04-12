package ruby.rubyapp.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import ruby.rubyapp.board.BoardBaseTest;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.entity.SearchType;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 댓글 테스트
 */
class CommentServiceImplTest extends BoardBaseTest {

    @Test
    @DisplayName("내용, 게시글 정보, 사용자 정보가 모두 있을 때 등록 성공")
    public void successRegisterComment() {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        String content = "테스트 댓글 내용 등록입니다.";
        String email = "test1@naver.com";

        // 댓글 저장
        Comment savedComment = commentService.addComment(content, email, board.getId());
        // 저장된 댓글 조회
        Comment searchComment = commentRepository.findById(savedComment.getId()).get();

        assertThat(savedComment).isEqualTo(searchComment);
        assertThat(savedComment.getBoard()).isEqualTo(board);
        assertThat(searchComment.getBoard()).isEqualTo(board);
    }

    @Test
    @DisplayName("제목이 빈 값일 경우 등록 실패")
    public void failRegisterCommentByContent() {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        String content = "    ";
        String email = "test1@naver.com";

        // 댓글 저장
        Comment comment = commentService.addComment(content, email, board.getId());

        // 저장된 댓글 조회
        assertThat(comment.getId()).isNull();
    }

    @Test
    @DisplayName("이메일 값이 없을 경우 등록 실패")
    public void failRegisterCommentByEmail() {
        List<Board> allBoard = boardRepository.findAll();
        Board board = allBoard.get(0);
        String content = "테스트 댓글 내용 등록입니다.";
        String email = null;

        // 댓글 저장
        Comment comment = commentService.addComment(content, email, board.getId());

        // 저장된 댓글 조회
        assertThat(comment.getId()).isNull();
    }


    @Test
    @DisplayName("게시글 id가 없을 경우 등록 실패")
    public void failRegisterCommentByBoardId() {
        Board board = new Board();
        String content = "테스트 댓글 내용 등록입니다.";
        String email = "test1@naver.com";

        // 댓글 저장
        Comment comment = commentService.addComment(content, email, board.getId());

        // 저장된 댓글 조회
        assertThat(comment.getId()).isNull();
    }

    @Test
    @DisplayName("댓글 수정")
    public void updateComment() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Comment comment = board.getCommentList().get(2);
        String content = "댓글 내용 수정!";
        String email = "test3@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========수정 시작==============");

        commentService.updateComment(content, email, comment.getId()).get();

        // 저장되어 조회가 가능한지 확인
        Optional<Comment> optionalComment = commentRepository.findById(comment.getId());
        Comment savedComment = optionalComment.get();

        assertThat(savedComment.getId()).isEqualTo(comment.getId());
        assertThat(savedComment.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("내용이 빈 값일 경우 수정 실패")
    public void failUpdateCommentByWrongContent() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Comment comment = board.getCommentList().get(2);
        String content = "        ";
        String email = "test3@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========수정 시작==============");

        Optional<Comment> optionalComment = commentService.updateComment(content, email, comment.getId());
        assertThat(optionalComment.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("접속한 유저가 댓글 작성자와 다르다면 수정 불가능")
    public void failUpdateCommentByWrongAccount() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Comment comment = board.getCommentList().get(2);
        String content = "댓글 내용 수정!";
        String email = "test1@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        System.out.println("===========수정 시작==============");

        Optional<Comment> optionalComment = commentService.updateComment(content, email, comment.getId());
        assertThat(optionalComment.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("댓글 삭제")
    public void deleteComment() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Comment comment = board.getCommentList().get(2);
        Long commentId = comment.getId();
        String email = "test3@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        assertThat(comment).isNotNull();

        System.out.println("===========삭제 시작==============");

        commentService.deleteComment(commentId, email);

        em.flush();
        em.clear();

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        assertThat(optionalComment.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("댓글 작성자와 사용자가 다를 경우 삭제 실패")
    public void failDeleteComment() {
        SearchType searchType = SearchType.TITLE;
        String searchWord = "게시글110";
        int pageNum = 0;

        Page<Board> boardList = boardService.getBoardList(searchType, searchWord, pageNum);
        Board board = boardList.getContent().get(0);
        Comment comment = board.getCommentList().get(2);
        Long commentId = comment.getId();
        String email = "wrong@naver.com";
        em.clear();             // 영속성 컨텍스트 비우기

        assertThat(comment).isNotNull();

        System.out.println("===========삭제 시작==============");

        commentService.deleteComment(commentId, email);

        em.flush();
        em.clear();

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        assertThat(optionalComment.isEmpty()).isFalse();
    }
}