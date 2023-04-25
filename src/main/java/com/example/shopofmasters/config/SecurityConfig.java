package com.example.shopofmasters.config;

import com.example.shopofmasters.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{

    private final PersonDetailsService personDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception{
        // конфигурируем работу Spring Security

        httpSecurity
                //.csrf().disable() // отключаем защиту от межсайтовой подделки запросов
                // указываем что все страницы должны быть защищены аутентификацией
                .authorizeHttpRequests()
                // указываем на то что страница /admin доступна пользователю с ролью ADMIN
                .requestMatchers("/admin").hasAnyRole("ADMIN")
                // указываем что не аутентифицированные пользователя могут зайти на страницу аутентификации и на объект ошибки
                // c помощью permitAll указываем что не аутентифицированные пользователи могут заходить на перечисленные страницы
                // указываем что для всех остальных страниц необходимо вызывать метод authenticated(), который открывает форму аутентификации
                .requestMatchers("/authentication", "/registration", "/error", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/product", "/product/info/{id}", "/product/search").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                // указываем что дальше настраиватеся аутентификация и соединяем ее с настройкой доступа
                .and()
                // указываем какой url запрос будет отправлятся при заходе на защищенные страницы
                .formLogin().loginPage("/authentication")
                // указываем на какой адрес будут отправляться данные с формы. Нам уже не нужно будет создавать метод в контроллере и обрабатывать данные с формы. Мы задали url, который используется по умолчанию для обработки формы аутентификации по средствам Spring Security. Spring Security будет ждать объект с формы аутентификации и затем сверять логин и пароль с данными в БД
                .loginProcessingUrl("/process_login")
                // Указываем на какой url необходимо направить пользователя после успешной аутентификации. Вторым аргументом указывается true чтобы перенаправление шло в любом случае послу успешной аутентификации
                .defaultSuccessUrl("/person_account", true)
                // Указываем куда необходимо перенаправить пользователя при проваленной аутентификации. В запрос будет передан объект error, который будет проверятся на форме и при наличии данного объекта в запросе выводится сообщение "Неправильный логин или пароль"
                .failureUrl("/authentication?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");
        return httpSecurity.build();

    }

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                //хеширование паролей
                .passwordEncoder(getPasswordEncode());
    }
}
