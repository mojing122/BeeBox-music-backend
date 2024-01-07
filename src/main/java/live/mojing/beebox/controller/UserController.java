package live.mojing.beebox.controller;

import jakarta.annotation.security.RolesAllowed;
import live.mojing.beebox.entity.RestBean;
import live.mojing.beebox.entity.user.AccountUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/get-user-info")
    public RestBean<AccountUser> getUserInfo(@SessionAttribute("account") AccountUser user){
        return RestBean.success(user);
    }

}
