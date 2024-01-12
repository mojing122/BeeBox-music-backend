package live.mojing.beebox.mapper.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "db_music")
public class Music {
    @TableId(value = "id",type= IdType.AUTO)
    Integer id;
    String name;
    String cover;
    Integer artistId;
    Integer length;
    String fileUrl;
    private Date createTime;
    private Date updateTime;
}
