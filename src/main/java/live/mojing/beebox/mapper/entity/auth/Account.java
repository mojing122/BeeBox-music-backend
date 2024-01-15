package live.mojing.beebox.mapper.entity.auth;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "db_account")
public class Account {
    @TableId
    Integer id;
    String username;
    String password;
    String email;
    String accountrole;
}
