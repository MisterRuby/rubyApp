package ruby.rubyapp.board.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.Comment;
import ruby.rubyapp.board.repository.CommentRepositoryCustom;

import javax.persistence.EntityManager;
import java.util.List;

import static ruby.rubyapp.account.entity.QAccount.account;
import static ruby.rubyapp.board.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    /**
     * 게시글에 연관된 댓글 목록 조회
     * @param belongBoard     게시글
     * @return
     */
    public List<Comment> getCommentListByBoard(Board belongBoard) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.account, account).fetchJoin()
                .where(comment.board.eq(belongBoard))
                .fetch();
    }

    /**
     * 게시글에 연관된 댓글 전체 삭제
     * @param boardId         게시글 id
     */
    public void deleteBulkComment(Long boardId) {
        queryFactory.delete(comment)
                .where(comment.board.id.eq(boardId))
                .execute();
    }
}
