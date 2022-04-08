package ruby.rubyapp.board.repository;

import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    /**
     * 게시글에 연관된 댓글 목록 조회
     * @param belongBoard     게시글
     * @return
     */
    List<Comment> getCommentListByBoard(Board belongBoard);
}
