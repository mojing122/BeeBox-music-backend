package live.mojing.beebox.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName(value = "db_playlist")
public class PlayList {
    @TableId(value = "id",type= IdType.AUTO)
    Integer id;
    String name;
    String description;
    Integer creatorid;
    String cover;
    private Date createTime;
    private Date updateTime;
}
