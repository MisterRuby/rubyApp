package ruby.rubyapp.board.service;

import ruby.rubyapp.board.entity.BoardFileRecord;

import java.util.Optional;

public interface BoardFileService {

    // 파일 다운로드
    Optional<BoardFileRecord> downLoadBoardFile (Long fileId);

}
