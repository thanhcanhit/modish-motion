package fit.iuh.modish_motion.configs;

import fit.iuh.modish_motion.entities.Account;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.servicesImpl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Autowired
    private AccountService accountService;
    @Bean
//    @SuppressWarnings("deprecation")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        .requestMatchers("/admin").hasAuthority("ADMIN")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/loginrequest")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            response.sendRedirect("/"); // Điều hướng sau khi đăng nhập thành công
                        })
                )
//                .logout(logout -> logout.logoutUrl("/admin-logout").logoutSuccessUrl("/login"))
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL dùng để xử lý logout
                        .logoutSuccessUrl("/login") // Trang điều hướng sau khi logout thành công
                        .clearAuthentication(true) // Xóa thông tin xác thực
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("Access Denied for user: " +
                                    SecurityContextHolder.getContext().getAuthentication());
                            response.sendRedirect("/not-found");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("User not authenticated: " +
                                    SecurityContextHolder.getContext().getAuthentication());
                            response.sendRedirect("/login");
                        }))
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                        .userDetailsService(userDetailsService()));

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/assets/**", "/assetsUser/**", "uploads/**", "/assetsAdmin/**"
        );
    }

//    @Bean
//    BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("UserDetailsService đang tìm kiếm tài khoản với username: " + username);

            // Tìm kiếm tài khoản và trả về UserDetails
            return accountService.findByUserNameAndPassword(username, "")
                    .map(accountDTO -> {
                        System.out.println("Thông tin tài khoản tìm thấy: " + accountDTO);

                        // Tạo UserDetails từ AccountDTO
                        return new org.springframework.security.core.userdetails.User(
                                accountDTO.getUsername(),
                                accountDTO.getPassword(), // Spring Security sẽ tự kiểm tra password
                                Collections.singletonList(
                                        new SimpleGrantedAuthority(accountDTO.isAdmin() ? "ADMIN" : "USER")
                                )
                        );
                    })
                    .orElseThrow(() -> {
                        System.out.println("Không tìm thấy tài khoản với username: " + username);
                        return new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
                    });
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                System.out.println("Authenticating user: " + username + " with password: " + password);

                // Tìm tài khoản từ database
                Account account = accountService.findByUsername(username)
                        .orElseThrow(() -> {
                            System.out.println("User not found: " + username);
                            return new UsernameNotFoundException("Không tìm thấy tài khoản");
                        }).toEntity();

                // So sánh mật khẩu không mã hóa
                if (!account.getPassword().equals(password)) {
                    System.out.println("Invalid password for user: " + username);
                    throw new BadCredentialsException("Tài khoản hoặc mật khẩu không chính xác");
                }

                // Gán quyền dựa trên isAdmin
                List<GrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority(account.isAdmin() ? "ADMIN" : "USER")
                );

                // Tạo Authentication object
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, password, authorities
                );
                SecurityContextHolder.getContext().setAuthentication(auth); // Đảm bảo lưu Authentication vào SecurityContext

                System.out.println("Authentication success for user: " + username);
                return auth;
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }



}