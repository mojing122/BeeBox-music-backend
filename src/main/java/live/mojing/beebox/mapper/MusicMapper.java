package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.sqlbuilder.MusicBuilder;
import org.apache.ibatis.annotations.*;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface MusicMapper{
        /**
         *  根据用户id和音乐id查找音乐并判断该音乐是否被喜欢
         * @param userid
         * @param musicid
         * @return
         */
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

        /**
         *  插入音乐
         * @param name
         * @param cover
         * @param length
         * @param fileUrl
         * @return
         */
        @Insert("insert into db_music (name,cover,length,file_url,artist_id) \n" +
                "values (#{name}, #{cover},#{length},#{fileUrl},#{artistId})")
        int insertMusic(String name,String cover,int length,String fileUrl,Integer artistId);

        /**
         *  通过音乐名查找音乐
         * @param musicName
         * @return
         */
        @Select("select * from db_music where name = #{musicName}")
        public List<Music> selectBytitle(String musicName);

        /**
         *  通过id查询艺术家
         * @param artistid
         * @return
         */
        @Select("select * from db_artist where id =#{artistid}")
        Artist findArtistById(Integer artistid);

        /**
         *  通过name查询艺术家
         * @param artistname
         * @return
         */
        @Select("select * from db_artist where name =#{artistname}")
        Artist findArtistByName(String artistname);



        /**
         *   插入艺术家
         * @param name
         * @param desc
         * @return
         */
        @Insert("insert into db_artist (name,description) \n" +
                "values (#{name}, #{desc})")
        int insertArtist(String name,String desc);

}
