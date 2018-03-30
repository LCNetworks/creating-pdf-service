package cn.com.tools.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by han on 18-1-17.
 */
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableDiscoveryClient
public class CreatingPdfApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreatingPdfApplication.class, args);
    }

}
