package ruby.rubyapp.account.entity;

/**
 * 사용자 권한 EnumClass
 */
public enum AccountRole {
    ADMIN, USER, BLOCK;

    public static boolean isExistRole(AccountRole role) {
        return role.equals(ADMIN) || role.equals(USER) || role.equals(BLOCK);
    }
}
