package live.mojing.beebox.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class JudgedMusic {
    @TableId(value = "id",type= IdType.AUTO)
    Integer id;
    String name;
    String cover;
    Integer length;
    String fileUrl;
    Boolean isLiked;
}


