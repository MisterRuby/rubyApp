package ruby.rubyapp.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ruby.rubyapp.account.entity.Account;
import ruby.rubyapp.account.repository.AccountRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * OAuth 로그인 이후 가져온 사용자의 정보를 기반으로 가입 및 정보 수정, 세션 저장
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AccountRepository accountRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth 서비스 id (구글, 네이버 등등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest
                .getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Account account = saveOrUpdate(attributes);

        httpSession.setAttribute("account", new SessionAccount(account));

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(account.getRole().name())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    // 사용자 정보가 업데이트 되었을 때 업데이트 된 내용을 반영
    private Account saveOrUpdate(OAuthAttributes attributes) {
        Account account = accountRepository
                .findByEmail(attributes.getEmail())
                .map(entity -> entity.updateName(attributes.getName()))
                .orElse(attributes.toEntity());

        return accountRepository.save(account);
    }
}
