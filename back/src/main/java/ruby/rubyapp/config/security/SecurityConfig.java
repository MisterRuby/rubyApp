package ruby.rubyapp.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import ruby.rubyapp.account.entity.AccountRole;
import ruby.rubyapp.config.oauth.CustomOAuth2UserService;

/**
 * WebSecurityConfigurerAdapter
 *  - WebSecurity 설정을 위한 추상메서드
 *  - 상속을 통해 매서드를 재정의하여 설정을 추가할 수 있다.
 */
@RequiredArgsConstructor
@EnableWebSecurity      // @Configuration 을 포함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable();

        http
                .authorizeRequests()                                                               // url별 권한 관리를 설정하는 옵션의 시작점
                .antMatchers("/", "/accounts", "/boards").permitAll()                   // permitAll() : 인증 x
                .antMatchers("/boards/**").hasRole(AccountRole.USER.name())
                .anyRequest().authenticated();
//                .expressionHandler(expressionHandler());

        http
                .formLogin().disable()
                .httpBasic().disable();

        http.oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);                                 // 소셜 로그인 성공시 후속조치를 진행할 인터페이스의 구현체를 등록

        http.logout()
                .logoutSuccessUrl("/");
    }

    public SecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ADMIN > USER");

        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);

        return expressionHandler;
    }




}
