package dolphin.auth;


import dolphin.auth.service.CustomClientDetailsService;
import dolphin.auth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@SessionAttributes("authorizationRequest")

public class AuthApplication// extends WebMvcConfigurerAdapter
{
    private static TokenStore tokenStore = new InMemoryTokenStore();

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    HttpSessionSecurityContextRepository contextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
    /*
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }
    */
    /*
    @EnableResourceServer
    protected static class resourceServerConfig extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.tokenStore(tokenStore);

        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            SimpleUrlLogoutSuccessHandler redirectHandler = new SimpleUrlLogoutSuccessHandler();
            redirectHandler.setTargetUrlParameter("redirect");

            http
                    .authorizeRequests()
                    //             .antMatchers("/oauth/**").permitAll()

                    .antMatchers("/resources/**","/favicon.ico","/login","/logout").permitAll()
                    //        .antMatchers("/welcome","/v2/**","/swagger-ui.html",    "/webjars/**", "/configuration/**", "/swagger-resources/**", "/docs").hasAuthority("ADMIN")

                    .anyRequest().authenticated()
                    .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
                    .logout()
                    .logoutSuccessHandler(redirectHandler)
                    .permitAll()
                    .and()
                    .csrf().disable();

            // @formatter:on
        }


    }
    */
    @Configuration
    @EnableWebSecurity
    protected static class webSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserDetailsService userDetailsService;


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder());

            //auth.parentAuthenticationManager(authenticationManagerBean());
        }
        /*@Override
        protected void configure(HttpSecurity http) throws Exception{
            http.csrf().disable();
        }
        */
        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }


    }


    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

        private static final String ROLE_TRUSTED_CLIENT="ROLE_TRUSTED_CLIENT";
        @Autowired
        private CustomUserDetailsService userDetailsService;



        @Autowired
        private Environment env;

        @Value("${micro-service-password}")
        private String microServiceClientSecret;

        @Autowired
        private CustomClientDetailsService customClientDetailsService;
        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            //clients.withClientDetails(customClientDetailsService);
            clients.inMemory()
                    .withClient("browser")
                    .authorizedGrantTypes("refresh_token","authorization_code", "password")
                    .scopes("ui").autoApprove(true)
                    .and()
                    .withClient("micro-service")
                    .secret(microServiceClientSecret)
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("server")
                    .and()
                    .withClient("swagger-ui")
                    .secret(microServiceClientSecret)
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("swagger-ui");

            /*clients.inMemory()
                    .withClient("acme")
                    .secret("acmesecret")
                    .authorities("ROLE_TRUSTED_CLIENT")
                    .authorizedGrantTypes("authorization_code", "refresh_token",
                            "password").scopes("openid");*/
        }
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            //security.tokenKeyAccess("permitAll()");
            security.allowFormAuthenticationForClients();
            security.tokenKeyAccess("permitAll()");
            security.checkTokenAccess("isAuthenticated()");
        }


        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(tokenStore)
                    .authenticationManager(this.authenticationManager)
                    .userDetailsService(userDetailsService);
            //.userApprovalHandler(userApprovalHandler());
        }


        private UserApprovalHandler userApprovalHandler(){
            UserApprovalHandler handler=new DefaultUserApprovalHandler(){
                public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {


                    /*if(authorizationRequest.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_TRUSTED_CLIENT))){
                        authorizationRequest.setApproved(true);
                    }*/
                    authorizationRequest.setApproved(true);
                    return authorizationRequest;
                }
            };

            return handler;
        }
        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(tokenStore);
            return tokenServices;
        }
    }
}
