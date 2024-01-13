package live.mojing.beebox.mapper.entity.JudgedEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import live.mojing.beebox.mapper.entity.Music;
import lombok.Data;

import java.util.Date;

@Data
public class JudgedMusic {
    @TableId(value = "id",type= IdType.AUTO)
    Integer id;
    String name;
    String cover;
    Integer length;
    String fileUrl;
    Boolean isLiked;
    String artist;
    private Date createTime;
    private Date updateTime;
}


