package live.mojing.momusic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("live.mojing.momusic.mapper")
public class MoMusicBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoMusicBackendApplication.class, args);
    }

}
