package live.mojing.momusic;

import live.mojing.momusic.entity.auth.Account;
import live.mojing.momusic.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class MoMusicBackendApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Account> userList = accountMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
