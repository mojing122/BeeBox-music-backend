package live.mojing.beebox.service;

import live.mojing.beebox.mapper.entity.auth.Account;

import java.util.List;

public interface AccountService {

    List<Account> selectAllAccount();

    int resetRoleById(String role, int id);

}
