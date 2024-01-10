package live.mojing.beebox.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.service.AuthorizeService;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";
    private final String USERNAME_REGEXP = "^[一-龥a-zA-Z0-9]+$";
    @Resource
    AuthorizeService authorizeService;

    /**
     * 验证注册邮箱
     * @param email 邮箱
     * @return RestBean
     */
    @PostMapping("/valid-register-email")
    public RestBean<String> validateRegisterEmail(@Pattern(regexp = EMAIL_REGEXP) @RequestParam("email") String email,
                                          HttpSession session){
        String s = authorizeService.sendValidateEmail(email, session.getId(),false);
        if(s == null)
            return RestBean.success("邮件已发送，5分钟内有效");
        else
            return RestBean.failure(400, s);
    }

    /**
     * 验证重置密码邮箱
     * @param email 邮箱
     * @return RestBean
     */
    @PostMapping("/valid-reset-email")
    public RestBean<String> validateResetEmail(@Pattern(regexp = EMAIL_REGEXP) @RequestParam("email") String email,
                                          HttpSession session){
        String s = authorizeService.sendValidateEmail(email, session.getId(),true);
        if(s == null)
            return RestBean.success("邮件已发送，5分钟内有效");
        else
            return RestBean.failure(400, s);
    }

    /**
     * 注册函数
     * @param username 用户名
     * @param password 密码
     * @param email 电子邮件
     * @param code 验证码
     * @return RestBean
     */
    @PostMapping("/register")
    public RestBean<String> registerAccount(@Pattern(regexp = USERNAME_REGEXP) @Length(min = 2, max = 10) @RequestParam("username") String username,
                                     @Length(min = 6, max = 16) @RequestParam("password") String password,
                                     @Pattern(regexp = EMAIL_REGEXP) @RequestParam("email") String email,
                                     @Length(min = 6, max = 6) @RequestParam("code") String code,
                                     HttpSession session){
        String s = authorizeService.validateAndRegister(username, password, email, code, session.getId());
        if(s == null)
            return RestBean.success("注册成功");
        else
            return RestBean.failure(400, s);

    }

    /**
     * 重置密码（验证邮箱）
     * @param email 邮箱
     * @param code 验证码
     * @return 验证结果
     */
    @PostMapping("/start-reset")
    public RestBean<String> startReset(@Pattern(regexp = EMAIL_REGEXP) @RequestParam("email") String email,
                                       @Length(min = 6, max = 6) @RequestParam("code") String code,
                                       HttpSession session){
        String s = authorizeService.validateOnly(email, code, session.getId());
        if(s == null) {
            session.setAttribute("reset-password", email);
            return RestBean.success("验证成功");
        }
        else
            return RestBean.failure(400, s);
    }

    @PostMapping("/do-reset")
    public RestBean<String> doReset(@Length(min = 6, max = 16) @RequestParam("password") String password,
                                    HttpSession session){
        String email = (String) session.getAttribute("reset-password");
        if(email == null){
            return RestBean.failure(401,"请先验证邮箱");
        } else if(authorizeService.resetPassword(password, email)) {
            session.removeAttribute("reset-password");
            return RestBean.success("密码重置成功");
        } else {
            return RestBean.failure(500,"内部错误，请联系管理员");
        }
    }

}
