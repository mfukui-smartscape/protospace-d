package in.tech_camp.proto_space.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // パスワードのハッシュ化方式（登録・ログイン共通）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // --- 認証必須を先に書く（順番が重要）---
                .requestMatchers("/prototypes/new").authenticated()
                .requestMatchers("/prototypes/*/edit").authenticated()
                .requestMatchers(HttpMethod.POST,   "/prototypes").authenticated()
                .requestMatchers(HttpMethod.PUT,    "/prototypes/*").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/prototypes/*").authenticated()
                .requestMatchers(HttpMethod.POST,   "/prototypes/*/comments").authenticated()

                // --- 公開（認証なしで見れる）---
                .requestMatchers("/").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers(HttpMethod.GET,  "/users/new").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET,  "/prototypes/*").permitAll()  // 詳細
                .requestMatchers(HttpMethod.GET,  "/users/*").permitAll()       // ユーザー詳細

                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }
}