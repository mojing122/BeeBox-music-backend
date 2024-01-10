package live.mojing.beebox.interceptor;

import jakarta.annotation.Resource; // 引入资源注解
import jakarta.servlet.http.HttpServletRequest; // 引入 HttpServletRequest 类
import jakarta.servlet.http.HttpServletResponse; // 引入 HttpServletResponse 类
import live.mojing.beebox.mapper.entity.user.AccountUser; // 引入账户用户实体类
import live.mojing.beebox.mapper.AccountMapper; // 引入账户映射器
import org.springframework.security.core.Authentication; // 引入认证类
import org.springframework.security.core.context.SecurityContext; // 引入安全上下文类
import org.springframework.security.core.context.SecurityContextHolder; // 引入安全上下文持有者类
import org.springframework.security.core.userdetails.User; // 引入用户类
import org.springframework.stereotype.Component; // 引入组件注解
import org.springframework.web.servlet.HandlerInterceptor; // 引入拦截器接口

@Component // 注明这是一个组件
public class AuthorizeInterceptor implements HandlerInterceptor {  // 创建一个名为 AuthorizeInterceptor 的拦截器类，并实现 HandlerInterceptor 接口
    @Resource // 注入 AccountMapper 资源
    AccountMapper accountMapper; // 创建一个 AccountMapper 对象

    @Override
    // 在处理请求前进行拦截处理
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext(); // 获取安全上下文
        Authentication authentication = context.getAuthentication(); // 获取认证信息
        User user = (User) authentication.getPrincipal(); // 获取认证主体
        String username = user.getUsername(); // 获取用户的用户名
        AccountUser account = accountMapper.findAccountUserByNameOrEmail(username); // 根据用户名或邮箱查找账户用户
        request.getSession().setAttribute("account", account); // 将账户用户信息存入Session中
        return true; // 返回 true，表示通过拦截
    }
}