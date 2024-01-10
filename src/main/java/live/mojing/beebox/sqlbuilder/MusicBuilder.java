package live.mojing.beebox.sqlbuilder;

import java.util.Map;

public class MusicBuilder {
    //查询音乐信息
    public String findMusic(Map<String,Object> map){
        String sql = "select * from db_music where id ="+map.get("id");
        return sql;
    }
}
