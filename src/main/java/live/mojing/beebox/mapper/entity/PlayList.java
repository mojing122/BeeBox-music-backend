package live.mojing.beebox.mapper.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PlayList {
    Integer id;
    String name;
    String desc;
    Integer accountId;
    String cover;
    private Date createTime;
    private Date updateTime;
}
