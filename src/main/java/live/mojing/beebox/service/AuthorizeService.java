package live.mojing.beebox.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    /**
     * 发送验证邮件
     * @param email 邮件地址
     * @return 是否发送成功
     */
    String sendValidateEmail(String email, String sessionId, boolean hasRegistered);

    /**
     * 验证并注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱地址
     * @param code 验证码
     * @param sessionId sessionId
     * @return 是否注册成功
     */
    String validateAndRegister(String username, String password, String email, String code, String sessionId);

    /**
     * 重置密码时验证邮箱
     * @param email 邮箱地址
     * @param code 验证码
     * @param sessionId sessionId
     * @return 验证结果
     */
    String validateOnly(String email, String code, String sessionId);

    /**
     * 重置密码操作
     * @param password 密码
     * @param email 邮箱地址
     * @return 重置结果
     */
    boolean resetPassword(String password, String email);
}
