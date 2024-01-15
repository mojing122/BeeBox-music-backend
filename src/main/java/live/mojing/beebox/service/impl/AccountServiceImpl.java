package live.mojing.beebox.service.impl;

import live.mojing.beebox.mapper.AccountMapper;
import live.mojing.beebox.mapper.entity.auth.Account;
import live.mojing.beebox.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Account> selectAllAccount() {

        return accountMapper.slelctAllAccount();

    }

    @Override
    public int resetRoleById(String role, int id) {

        return accountMapper.resetRoleById(role, id);

    }
}
