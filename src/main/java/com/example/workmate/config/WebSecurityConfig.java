package com.example.workmate.config;

import com.example.workmate.component.OAuth2SuccessHandler;
import com.example.workmate.entity.account.Authority;
import com.example.workmate.jwt.JwtTokenFilter;
import com.example.workmate.jwt.JwtTokenUtils;
import com.example.workmate.service.account.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers(
                                        "/token/issue",
                                        "/token/validate",
                                        "/account/home",
                                        "/account/login",

                                        // 로그인, 매장 아르바이트 요청 html
                                        "/account/login",
                                        "/account/register",
                                        "/account/users-register",
                                        "/account/business-register",
                                        "/account/logout",

                                        //근무표 관련 테스트중
                                        "/schedule/**"

                                )
                                .permitAll()

                                .requestMatchers(
                                        "/my-profile",
                                        "/profile",
                                        "/account/oauth")
                                .authenticated()

                                .requestMatchers(
                                        "/shop",
                                        "/shop/{id}",
                                        "/api/shop/read-all",
                                        "/api/shop/read-one",
                                        "/api/shop/{id}",
                                        "/profile/{id}",
                                        "/profile/{id}/update")
                                .hasAnyAuthority(
                                        Authority.ROLE_ADMIN.getAuthority(),
                                        Authority.ROLE_BUSINESS_USER.getAuthority(),
                                        Authority.ROLE_USER.getAuthority()
                                )

                                // 이메일 인증 - 비활성 유저
                                .requestMatchers(
                                        "/email-check",
                                        "/profile/email-check",
                                        "/profile/check-code")
                                .hasAnyAuthority(
                                        Authority.ROLE_INACTIVE_USER.getAuthority(),
                                        Authority.ROLE_ADMIN.getAuthority()
                                )

                                // 아르바이트 요청 - USER만 가능
                                .requestMatchers(
                                        "/profile/submit")
                                .hasAnyAuthority(
                                        Authority.ROLE_USER.getAuthority(),
                                        Authority.ROLE_ADMIN.getAuthority()
                                )

                                // 매장에서 아르바이트 요청 확인
                                .requestMatchers(
                                        "/shop/create",
                                        "/shop/{shopId}/shop-account",
                                        "/api/shop/crete",
                                        "/api/shop/{id}/update",
                                        "/api/shop/{id}/delete",
                                        "/api/shop/{id}/shop-account",
                                        "/api/shop/{id}/shop-account/account-name",
                                        "/api/shop/{id}/shop-account/account-status",
                                        "/api/shop/{shopId}/shop-account/accept/{accountShopId}")
                                .hasAnyAuthority(
                                        Authority.ROLE_ADMIN.getAuthority(),
                                        Authority.ROLE_BUSINESS_USER.getAuthority()
                                )

                                // 매장 커뮤니티 접속 권한
                                .requestMatchers(
                                        "/{shopId}/community",
                                        "/{shopId}/community/article/new",
                                        "/{shopId}/community/article/create",
                                        "/{shopId}/community/{board}",
                                        "/{shopId}/community/{board}/{shopArticleId}",
                                        "/{shopId}/community/{board}/{shopArticleId}/edit",
                                        "/{shopId}/community/{board}/{shopArticleId}/update",
                                        "/{shopId}/community/{board}/{shopArticleId}/delete",
                                        "/{shopId}/community/{board}/{shopArticleId}/password")
                                .hasAnyAuthority(
                                        Authority.ROLE_ADMIN.getAuthority(),
                                        Authority.ROLE_BUSINESS_USER.getAuthority(),
                                        Authority.ROLE_USER.getAuthority()
                                )

                                // 매장 커뮤니티 댓글 권한
                                .requestMatchers(
                                        "/{shopId}/community/{board}/{shopArticleId}/comment",
                                        "/{shopId}/community/{board}/{shopArticleId}/comment/{commentId}/update",
                                        "/{shopId}/community/{board}/{shopArticleId}/comment/{commentId}/delete")
                                .hasAnyAuthority(
                                        Authority.ROLE_ADMIN.getAuthority(),
                                        Authority.ROLE_BUSINESS_USER.getAuthority(),
                                        Authority.ROLE_USER.getAuthority()
                                )

//                                // 출퇴근 기록 페이지 테스트
//                                .requestMatchers(
//                                        "/attendance/{accountId}/{shopId}")
//                                .hasAnyAuthority(
//                                        Authority.ROLE_ADMIN.getAuthority(),
//                                        Authority.ROLE_BUSINESS_USER.getAuthority(),
//                                        Authority.ROLE_USER.getAuthority()
//                                )
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/account/login")
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))

                )
                .logout(logout -> logout
                        .logoutUrl("/account/logout")
                        .logoutSuccessUrl("/account/home")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "jwtToken")
                )
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenUtils, manager),
                        AuthorizationFilter.class)
        ;
        return http.build();
    }
}
