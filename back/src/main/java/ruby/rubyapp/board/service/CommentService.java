package ruby.rubyapp.board.service;

import ruby.rubyapp.board.entity.Comment;

import java.util.Optional;

/**
 * 댓글 서비스
 */
public interface CommentService {

    /**
     * 댓글 등록
     * @param content       댓글 내용
     * @param email         작성자 email
     * @param boardId       게시글 id
     * @return              등록된 댓글
     */
    Comment addComment(String content, String email, Long boardId);

    /**
     * 댓글 수정
     * @param content           댓글 내용
     * @param email             작성자 email
     * @param commentId         댓글 id
     * @return
     */
    Optional<Comment> updateComment(String content, String email, Long commentId);

    /**
     * 댓글 삭제
     * @param commentId         댓글 id
     * @param email             작성자 email
     * @return
     */
    Long deleteComment(Long commentId, String email);
}
