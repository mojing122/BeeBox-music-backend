package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.auth.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AccountMapper extends BaseMapper<Account>{
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Select("select * from db_account")
    List<Account> slelctAllAccount();

    @Select("select * from db_account where username = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);

    @Insert("insert into db_account ( username, password, email) values (#{username}, #{password}, #{email})")
    int createAccount(String username, String password, String email);

    @Update("update db_account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password, String email);

    @Update("update db_account set accountrole = #{role} where id = #{id}")
    int resetRoleById(String role, int id);



}
