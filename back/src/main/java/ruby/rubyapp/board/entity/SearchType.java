package ruby.rubyapp.board.entity;

/**
 * 게시글 검색 종류 EnumClass
 */
public enum SearchType {
    USERNAME("name"), TITLE("title"), CONTENT("content")

    private String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
