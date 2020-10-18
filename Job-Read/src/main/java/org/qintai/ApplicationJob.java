package org.qintai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wuangjing
 * @create 2020/10/18-16:13
 * @Description:
 */
@SpringBootApplication
@EnableScheduling
public class ApplicationJob {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationJob.class,args);
    }
}
