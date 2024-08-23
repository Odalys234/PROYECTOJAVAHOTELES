package proyecto.Hoteles.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery("SELECT login, clave, status FROM usuarios WHERE login = ?");
        users.setAuthoritiesByUsernameQuery("SELECT u.login, r.nombre FROM usuario_rol ur " +
                "INNER JOIN usuarios u ON u.id = ur.usuario_id " +
                "INNER JOIN roles r ON r.id = ur.rol_id " +
                "WHERE u.login = ?");
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/dist/**", "/plugins/**").permitAll()
                .requestMatchers("/", "/privacy", "/terms").permitAll()
                .anyRequest().authenticated()
        );
        http.formLogin(form -> form.permitAll());
        return http.build();
    }
}
