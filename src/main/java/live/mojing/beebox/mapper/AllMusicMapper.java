package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//
@Mapper
public interface AllMusicMapper {
    @Select("select * from db_music where name = #{musicName}")
    public List<Music> findAllMusic(String musicName);

}
