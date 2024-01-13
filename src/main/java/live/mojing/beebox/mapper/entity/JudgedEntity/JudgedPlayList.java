package live.mojing.beebox.mapper.entity.JudgedEntity;
import live.mojing.beebox.mapper.entity.Music;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JudgedPlayList {
    String name;
    String description;
    Integer listlength;
    String cover;
    int isLiked;
    Boolean editable;

    List<JudgedMusic> musicList;
}
