package ruby.rubyapp.board.util;

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
    public static boolean validateRegisterBoard (String title, String content, String accountEmail) {
        return title == null || title.isBlank()
                || content == null || content.isBlank()
                || accountEmail == null || accountEmail.isBlank();
    }

    /**
     * 댓글 등록 시 입력값 검증
     * @param content           댓글 내용
     * @param accountEmail      댓글 등록자 이메일
     * @param boardId           관련 게시글 id
     * @return
     */
    public static boolean validateRegisterComment (String content, String accountEmail, Long boardId) {
        return content == null || content.isBlank()
                || accountEmail == null || accountEmail.isBlank()
                || boardId == null;
    }
}
