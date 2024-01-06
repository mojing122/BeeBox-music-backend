package live.mojing.beebox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("live.mojing.beebox.mapper")
public class BeeBoxBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeeBoxBackendApplication.class, args);
    }

}
