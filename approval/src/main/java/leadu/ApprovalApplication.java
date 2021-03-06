package leadu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Created by pengchao on 2017/5/17.
 */
@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class ApprovalApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApprovalApplication.class, args);
    }
}
