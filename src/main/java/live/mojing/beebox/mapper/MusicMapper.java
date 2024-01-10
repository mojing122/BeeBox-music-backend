package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.sqlbuilder.MusicBuilder;
import org.apache.ibatis.annotations.*;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

@Mapper
public interface MusicMapper{
        @Select("SELECT \n" +
                "    m.id,\n" +
                "    m.name,\n" +
                "    m.cover,\n" +
                "    m.length,\n" +
                "    m.file_url,\n" +
                "    (CASE WHEN l.musicid IS NOT NULL THEN TRUE ELSE FALSE END) AS isLiked\n" +
                "FROM \n" +
                "    db_music m\n" +
                "LEFT JOIN \n" +
                "    db_like l ON m.id = l.musicid AND l.userid = #{userid}\n" +
                "WHERE \n" +
                "    m.id = #{musicid};")
        JudgedMusic findMusicById(Integer userid, Integer musicid);

//        @Select("select * from db_music where name = #{musicName}")
//        public List<Music> findAllMusicByName(String musicName);
}
