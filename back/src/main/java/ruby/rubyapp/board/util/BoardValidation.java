package ruby.rubyapp.board.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Board 관련 검증 클래스
 */
public class BoardValidation {

    /**
     * 게시글 등록 시 입력값 검증
     * @param title             게시글 제목
     * @param content           게시글 내용
     * @param accountEmail      게시글 등록자 이메일
     * @return
     */
    public static boolean validateAddBoard (String title, String content, String accountEmail) {
        return !(title == null || title.isBlank()
                || content == null || content.isBlank()
                || accountEmail == null || accountEmail.isBlank());
    }

    /**
     * 게시글 수정 시 입력값 검증
     * @param title             게시글 제목
     * @param content           게시글 내용
     * @param accountEmail      게시글 등록자 이메일
     * @param boardId           게시글 id
     * @return
     */
    public static boolean validateUpdateBoard (String title, String content, String accountEmail, Long boardId) {
        return !(title == null || title.isBlank()
                || content == null || content.isBlank()
                || accountEmail == null || accountEmail.isBlank()
                || boardId == null);
    }


    /**
     * 댓글 등록 시 입력값 검증
     * @param content           댓글 내용
     * @param accountEmail      댓글 등록자 이메일
     * @param boardId           관련 게시글 id
     * @return
     */
    public static boolean validateAddComment (String content, String accountEmail, Long boardId) {
        return !(content == null || content.isBlank()
                || accountEmail == null || accountEmail.isBlank()
                || boardId == null);
    }

    /**
     * 댓글 수정 시 입력값 검증
     * @param content           댓글 내용
     * @param accountEmail      댓글 등록자 이메일
     * @param commentId         댓글 id
     * @return
     */
    public static boolean validateUpdateComment (String content, String accountEmail, Long commentId) {
        return !(content == null || content.isBlank()
                || accountEmail == null || accountEmail.isBlank()
                || commentId == null);
    }

    /**
     * 업로드 파일 확장자 검사
     * @param files     업로드 파일 목록
     * @return
     */
    public static boolean validateFileExtension (List<MultipartFile> files) {
        Set<String> extSet = new HashSet<>(Arrays.asList("txt", "png", "img", "pdf", "xls"));

        boolean result = true;

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                result = false;
                break;
            }

            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!extSet.contains(extension)) {
                result = false;
                break;
            }
        }

        return result;
    }
}
