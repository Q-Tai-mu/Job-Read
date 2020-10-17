package org.QinTai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wuangjing
 * @create 2020/10/16-14:37
 * @Description:
 */
@SpringBootApplication
@EnableScheduling
public class ApplicationRead {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRead.class,args);
    }
}
