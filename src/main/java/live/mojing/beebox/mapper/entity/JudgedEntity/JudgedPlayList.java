package live.mojing.beebox.mapper.entity.JudgedEntity;
import live.mojing.beebox.mapper.entity.Music;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JudgedPlayList {
//    Integer id;
//    String name;
//    String desc;
//    Integer accountId;
//    String cover;
//    private Date createTime;
//    private Date updateTime;

    List<Music> musicList;
    int isLiked;
}
