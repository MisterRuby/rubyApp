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
}
