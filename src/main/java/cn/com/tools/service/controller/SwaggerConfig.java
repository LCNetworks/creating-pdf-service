package cn.com.tools.service.controller;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.any;

/**
 * Created by lpc on 2018-1-18.
 */
@Configuration
@EnableSwagger2
@ComponentScan("cn.com.inhand.tools.service.controller")
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    @Value("${inhand.product.host}")
    private String host;
    @Value("${inhand.product.path}")
    private String path;

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("creatingPdfService")
                .select()
                .apis(any())
                .paths(paths())
                .build()
                .apiInfo(apiInfo());
        if (host != null && !host.equals("")) {
            docket.host(host);
        }
        if (path != null && !path.equals("")) {
            docket.pathMapping(path);
        }
//        docket.host("localhost");
//        docket.pathMapping("creatingPdfService/");
        return docket;
    }

    private Predicate<String> paths() {
        return or(
                regex("/api.*"),
                regex("/public.*"),
                regex("/common.*"));
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("韩传俊", "", "han@inhand.com.cn");
        ApiInfo apiInfo = new ApiInfo(
                "A+ 公用模块模块api",
                "A+ 公用模块",
                "v2.0.0",
                "",
                contact,
                "",
                "");
        return apiInfo;
    }
}

