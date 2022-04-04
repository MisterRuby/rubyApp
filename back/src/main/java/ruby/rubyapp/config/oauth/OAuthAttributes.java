package ruby.rubyapp.config.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.entity.AccountRole;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;

    /**
     * @param registrationId
     * @param userNameAttributeName oAuth2User 에서 반환하는 사용자 정보
     * @param attributes
     * @return
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver(attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }

    /**
     * UserInfo 엔티티 생성
     *  - userInfoRepository.findBtEmail 에서 해당 유저의 정보를 찾지 못했을 때. 즉, 신규로 등록할 때
     * @return
     */
    public Account toEntity() {
        return Account.builder()
                .name(name)
                .email(email)
                .role(AccountRole.USER)
                .build();
    }
}
