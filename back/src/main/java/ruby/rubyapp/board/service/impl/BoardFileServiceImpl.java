package ruby.rubyapp.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ruby.rubyapp.board.entity.BoardFileRecord;
import ruby.rubyapp.board.repository.BoardFileRepository;
import ruby.rubyapp.board.service.BoardFileService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

    private final BoardFileRepository boardFileRepository;

    @Override
    public Optional<BoardFileRecord> downLoadBoardFile(Long fileId) {
        // id 를 통해 파일 레코드로부터 storedName 을 얻어낸다(실제 저장된 파일 이름)

        return boardFileRepository.findById(fileId);
    }
}
