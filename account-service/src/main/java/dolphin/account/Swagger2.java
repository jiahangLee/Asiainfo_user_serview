package dolphin.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2017/1/22.
 */

@Configuration
@ConditionalOnProperty(prefix = "swagger",value = "enabled", matchIfMissing = false)
@EnableSwagger2

public class Swagger2 {

    private String clientId;
    @Value("${micro-service-password}")
    private String microServiceClientSecret;

    @Bean
    public Docket createRestApi() {
        List<SecurityScheme> securitySchemes=new ArrayList();
        securitySchemes.add(oauth());
        List<SecurityContext> securityContexts=new ArrayList();
        securityContexts.add(securityContext());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("dolphin.account.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes)
                .securityContexts(securityContexts).apiInfo(apiInfo());
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户服务api")
                .description("")
                .termsOfServiceUrl("")

                .version("1.0")
                .build();
    }



    @Bean
    SecurityContext securityContext() {


        AuthorizationScope[] scopes = new AuthorizationScope[]{new AuthorizationScope("swagger-ui", "")};


        SecurityReference securityReference = SecurityReference
                .builder()
                .reference("oauth2")
                .scopes(scopes)
                .build();

        List<SecurityReference> list=new ArrayList();
        list.add(securityReference);

        return SecurityContext
                .builder()
                .securityReferences(list)
                .forPaths(PathSelectors.any())
                .build();
    }

    @Bean
    SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("oauth2")
                .grantTypes(grantTypes())
                .scopes(scopes())
                .build();
    }

    List<AuthorizationScope> scopes() {
        List<AuthorizationScope> authorizationScopes=new ArrayList();

        authorizationScopes.add(new AuthorizationScope("swagger-ui", ""));

        return authorizationScopes;
    }

    List<GrantType> grantTypes() {

        List<GrantType> grants = new ArrayList();
        //grants.add(new ResourceOwnerPasswordCredentialsGrant("/uaa/oauth/token"));
        grants.add(new ClientCredentialsGrant("/uaa/oauth/token"));
       return grants;
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration("swagger-ui", microServiceClientSecret , "swagger-ui",
                "swagger", "Authorization: Bearer", ApiKeyVehicle.HEADER, "Authorization", ",");
    }
}
