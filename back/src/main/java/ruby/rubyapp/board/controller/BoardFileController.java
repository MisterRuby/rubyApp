package ruby.rubyapp.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ruby.rubyapp.board.entity.BoardFileRecord;
import ruby.rubyapp.board.service.BoardFileService;
import ruby.rubyapp.config.oauth.LoginAccount;
import ruby.rubyapp.config.oauth.SessionAccount;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * 게시판 관련 파일 Controller
 */
@Controller
@RequestMapping(value = "/boardFiles")
@RequiredArgsConstructor
public class BoardFileController {

    private final BoardFileService boardFileService;

    @Value("${file.uploadDir}")
    private String uploadDir;

    /**
     * 게시글에 등록된 파일 다운로드
     * @param boardFileId   파일 id
     * @return
     * @throws IOException
     */
    @GetMapping("/{boardFileId}")
    public ResponseEntity downloadBoardFile (@PathVariable Long boardFileId, @LoginAccount SessionAccount account) {
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Optional<BoardFileRecord> boardFileRecord = boardFileService.downLoadBoardFile(boardFileId);

        if (boardFileRecord.isPresent()) {
            File file = boardFileRecord.map(record -> new File(uploadDir + File.separator + record.getStoredFileName())).orElse(null);
            String originFileName = boardFileRecord.get().getOriginFileName();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(originFileName, StandardCharsets.UTF_8)
                            .build()
            );

            Path path = Paths.get(file.getAbsolutePath());
            try {
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
                return ResponseEntity.ok()
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .headers(httpHeaders)
                        .body(resource);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
