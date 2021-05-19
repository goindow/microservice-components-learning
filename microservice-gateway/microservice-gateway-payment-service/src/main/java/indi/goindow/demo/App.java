package indi.goindow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/payment")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/info")
    public String getPayments() {
        return "payment service";
    }

}
