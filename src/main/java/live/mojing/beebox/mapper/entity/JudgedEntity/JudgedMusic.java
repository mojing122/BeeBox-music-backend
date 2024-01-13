package live.mojing.beebox.mapper.entity.JudgedEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import live.mojing.beebox.mapper.entity.Music;
import lombok.Data;

@Data
public class JudgedMusic {
//    Music music;
    @TableId(value = "id",type= IdType.AUTO)
    Integer id;
    String name;
    String cover;
    Integer length;
    String fileUrl;
    Boolean isLiked;
    String artist;
}


