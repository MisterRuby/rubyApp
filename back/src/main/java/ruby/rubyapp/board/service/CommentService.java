package ruby.rubyapp.board.service;

import ruby.rubyapp.board.entity.Comment;

/**
 * 댓글 서비스
 */
public interface CommentService {

    /**
     * 댓글 등록
     * @param content       댓글 내용
     * @param accountEmail  작성자 email
     * @param boardId       게시글 id
     * @return              등록된 댓글
     */
    Comment addComment(String content, String accountEmail, Long boardId);
}
