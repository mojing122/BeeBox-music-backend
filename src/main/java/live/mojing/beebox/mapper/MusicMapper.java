package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.sqlbuilder.MusicBuilder;
import org.apache.ibatis.annotations.*;
import org.springframework.data.relational.core.sql.In;

@Mapper
public interface MusicMapper{
//        @SelectProvider(type = MusicBuilder.class ,method = "findMusic")
//        @Options(flushCache = Options.FlushCachePolicy.FALSE, timeout = 10000)
//        Music findMusicById(@Param(value = "id")Integer id);

        @Select("select * from db_music where id = #{id}")
        Music findMusicById(Integer id);
}
