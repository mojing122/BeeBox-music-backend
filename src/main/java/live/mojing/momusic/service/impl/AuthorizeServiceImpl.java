package live.mojing.momusic.service.impl;

import jakarta.annotation.Resource;
import live.mojing.momusic.entity.auth.Account;
import live.mojing.momusic.mapper.AccountMapper;
import live.mojing.momusic.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String sendFrom;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        /*
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUsername, username)
                .or().eq(Account::getEmail, username);
        Account account = accountMapper.selectOne(queryWrapper);
        */
        Account account = accountMapper.findAccountByNameOrEmail(username);
        if (account ==null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasRegistered) {
        // 1.先生成对应的验证码
        // 2.把验证码发送到指定邮箱
        // 3.把邮箱和对应的验证码放到Redis，设置过期时间
        // 4.如果发送失败就把Redis中的对应记录删除
        // 5.用户注册时从Redis去除对应键值对，看验证码是否一致
        String key = "email:"+sessionId+":"+email+":"+hasRegistered;
        if(Boolean.TRUE.equals(template.hasKey(key))){
            Long expireTime = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if(expireTime>240){
                return "请求过于频繁，请稍后再试";
            }
        }
        Account account = accountMapper.findAccountByNameOrEmail(email);
        // 注册时要求邮箱未注册，重置密码时要求邮箱已注册
        if(hasRegistered && account == null)
            return "此邮箱地址尚未注册";
        if(!hasRegistered && account != null)
            return "邮箱已被注册";
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sendFrom);
        mailMessage.setTo(email);
        mailMessage.setSubject("您的验证邮件");
        mailMessage.setText("验证码是" + code);
        try {
            mailSender.send(mailMessage);
            template.opsForValue().set(key, String.valueOf(code),5, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "邮件发送失败，请检查邮箱地址是否有效";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String key = "email:"+sessionId+":"+email+":false";
        if(Boolean.TRUE.equals(template.hasKey(key))) {
            String codeCache = template.opsForValue().get(key);
            if (codeCache == null)  return "验证码已失效";
            if (codeCache.equals(code)) {
                Account account = accountMapper.findAccountByNameOrEmail(username);
                if(account != null) return "此用户名已被注册，请尝试其他用户名";
                template.delete(key);
                password = encoder.encode(password);
                if(accountMapper.createAccount(username, password, email) > 0){
                    return null;
                } else {
                    return "内部错误，请联系管理员";
                }
            } else {
                return "验证码错误";
            }
        } else {
            return "验证码已失效";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:"+sessionId+":"+email+":true";
        if(Boolean.TRUE.equals(template.hasKey(key))) {
            String codeCache = template.opsForValue().get(key);
            if (codeCache == null)  return "验证码已失效";
            if (codeCache.equals(code)) {
                template.delete(key);
                return null;
            } else {
                return "验证码错误";
            }
        } else {
            return "验证码已失效";
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password);
        return accountMapper.resetPasswordByEmail(password, email) > 0;
    }
}
