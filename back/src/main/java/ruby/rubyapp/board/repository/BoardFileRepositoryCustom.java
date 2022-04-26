package ruby.rubyapp.board.repository;

public interface BoardFileRepositoryCustom {

    /**
     * 게시글에 연관된 파일 레코드 전체 삭제
     * @param boardId         게시글 id
     */
    void deleteBulkBoardFile(Long boardId);
}
