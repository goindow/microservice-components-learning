package indi.goindow.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping("/user")
/** 配置自动刷新 */
@RefreshScope
public class App {

    /**
     * @dataid ${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
     *     eg: microservice-config-nacos.yaml
     *         microservice-config-nacos-dev.yaml
     * */
    @Value("${config.info}")
    private String config;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/info")
    public String getUsers() {
        return config;
    }

}



