package ruby.rubyapp.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardFileRecord is a Querydsl query type for BoardFileRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFileRecord extends EntityPathBase<BoardFileRecord> {

    private static final long serialVersionUID = -532398485L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardFileRecord boardFileRecord = new QBoardFileRecord("boardFileRecord");

    public final QBoard board;

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originFileName = createString("originFileName");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath storedFileName = createString("storedFileName");

    public QBoardFileRecord(String variable) {
        this(BoardFileRecord.class, forVariable(variable), INITS);
    }

    public QBoardFileRecord(Path<? extends BoardFileRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardFileRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardFileRecord(PathMetadata metadata, PathInits inits) {
        this(BoardFileRecord.class, metadata, inits);
    }

    public QBoardFileRecord(Class<? extends BoardFileRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

