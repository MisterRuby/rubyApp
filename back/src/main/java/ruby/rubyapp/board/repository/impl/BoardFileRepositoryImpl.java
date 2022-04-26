package ruby.rubyapp.board.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ruby.rubyapp.board.repository.BoardFileRepositoryCustom;

import static ruby.rubyapp.board.entity.QBoardFileRecord.boardFileRecord;

@RequiredArgsConstructor
public class BoardFileRepositoryImpl implements BoardFileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 게시글에 연관된 파일 레코드 전체 삭제
     * @param boardId         게시글 id
     */
    public void deleteBulkBoardFile(Long boardId) {
        queryFactory.delete(boardFileRecord)
                .where(boardFileRecord.board.id.eq(boardId))
                .execute();
    }
}
