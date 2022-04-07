package ruby.rubyapp.board.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ruby.rubyapp.board.entity.Board;
import ruby.rubyapp.board.entity.QBoard;
import ruby.rubyapp.board.entity.SearchType;
import ruby.rubyapp.board.repository.BoardRepositoryCustom;

import java.util.List;
import java.util.Optional;

import static ruby.rubyapp.account.entity.QAccount.*;
import static ruby.rubyapp.board.entity.QBoard.*;
import static ruby.rubyapp.board.entity.QComment.*;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 게시판 목록 조회
     * @param searchType    검색 종류
     * @param searchWord    검색어
     * @param pageable      페이징 객체
     * @return
     */
    @Override
    public Page<Board> getBoardList (SearchType searchType, String searchWord, Pageable pageable) {
        // 전체 목록 수
        Long size = queryFactory.select(  board.count())
                .from(board)
                .leftJoin(board.account, account)
                .where(getBoardSearchCondition(searchType, searchWord))
                .fetchOne();

        // 페이지 번호에 해당하는 목록 조회
        List<Board> boardList = queryFactory.selectFrom(board)
                .leftJoin(board.account, account)
                .where(getBoardSearchCondition(searchType, searchWord))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        return new PageImpl<>(boardList, pageable, size);
    }

    /**
     * 게시글 단건, 연관된 댓글목록 조회
     * @param boardId       게시글 id
     * @return
     */
    @Override
    public Optional<Board> getBoard(Long boardId) {
        Board board = queryFactory.selectFrom(QBoard.board)
                .leftJoin(QBoard.board.commentList, comment).fetchJoin()
                .leftJoin(QBoard.board.account, account).fetchJoin()
                .where(QBoard.board.id.eq(boardId))
                .distinct()
                .fetchOne();

        return Optional.ofNullable(board);
    }

    /**
     * 게시글 검색조건 설정
     * @param searchType        검색 종류
     * @param searchWord        검색어
     * @return
     */
    public BooleanExpression getBoardSearchCondition(SearchType searchType, String searchWord) {
        if (searchType.equals(SearchType.CONTENT)) {
            return board.content.contains(searchWord);
        }
        if (searchType.equals(SearchType.USERNAME)) {
            return board.account.name.contains(searchWord);
        }
        if (searchType.equals(SearchType.TITLE)) {
            return board.title.contains(searchWord);
        }

        return null;
    }
}
