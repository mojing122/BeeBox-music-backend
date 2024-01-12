package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import org.apache.ibatis.annotations.*;

import java.util.Date;
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
                "    db_like l ON m.id = l.musicid AND l.accountid = #{accountid}\n" +
                "WHERE \n" +
                "    m.id = #{musicid};")
        JudgedMusic findMusicById(Integer accountid, Integer musicid);

        /**
         *  插入音乐
         * @param name
         * @param cover
         * @param length
         * @param fileUrl
         * @return
         */
        @Insert("insert into db_music (name,cover,length,file_url,artist_id,createTime,updateTime) \n" +
                "values (#{name}, #{cover},#{length},#{fileUrl},#{artistId},#{createTime},#{updateTime})")
        int insertMusic(String name, String cover, int length, String fileUrl,
                        Integer artistId, Date createTime,Date updateTime);

        /**
         *  通过音乐名查找音乐
         * @param musicName
         * @return
         */
        @Select("select * from db_music where name = #{musicName}")
        public List<Music> selectBytitle(String musicName);

        /**
         *  根据歌曲的点赞量查找规定数量歌曲歌曲
         * @param limit
         * @param offset
         * @return 歌曲列表
         */
        @Select("SELECT m.id, m.name, m.cover, m.length, m.file_url, m.artist_id, m.createTime, m.updateTime, COUNT(l.id) AS like_count\n" +
                "FROM db_music m\n" +
                "LEFT JOIN db_like l \n" +
                "ON m.id = l.musicid\n" +
                "GROUP BY m.id\n" +
                "ORDER BY like_count DESC\n" +
                "LIMIT #{limit} OFFSET #{offset};")
        List<Music> findMusicByLikeCount(Integer limit, Integer offset);


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
         * @param createTime
         * @param updateTime
         * @return
         */

        @Insert("insert into db_artist (name,description,createTime,updateTime) \n" +
                "values (#{name}, #{desc},#{createTime},#{updateTime})")
        int insertArtist(String name,String desc, Date createTime,Date updateTime);


}
