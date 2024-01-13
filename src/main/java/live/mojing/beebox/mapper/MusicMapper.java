package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import org.apache.ibatis.annotations.*;
import org.springframework.data.relational.core.sql.In;

import java.util.Date;
import java.util.List;

@Mapper
public interface MusicMapper{
        /**
         *  根据用户id和音乐id查找音乐并判断该音乐是否被喜欢
         * @param accountid
         * @param musicid
         * @return
         */
        @Select("SELECT \n" +
                "    m.id,\n" +
                "    m.name,\n" +
                "    m.cover,\n" +
                "    m.length,\n" +
                "    m.file_url,\n" +
                "    m.createTime,\n" +
                "    m.updateTime,\n" +
                "    (CASE WHEN l.musicid IS NOT NULL THEN TRUE ELSE FALSE END) AS isLiked,\n" +
                "    a.name as artist\n" +
                "FROM db_music m\n" +
                "LEFT JOIN db_like l ON m.id = l.musicid AND l.accountid = #{accountid}\n" +
                "LEFT JOIN db_artist a on m.artist_id=a.id\n" +
                "WHERE m.id = #{musicid};")
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
         * @param accountId
         * @return
         */
        @Select("SELECT \n" +
                "    m.id,\n" +
                "    m.name,\n" +
                "    m.cover,\n" +
                "    m.length,\n" +
                "    m.file_url,\n" +
                "    m.createTime,\n" +
                "    m.updateTime,\n" +
                "    (CASE WHEN l.musicid IS NOT NULL THEN TRUE ELSE FALSE END) AS isLiked,\n" +
                "    a.name as artist\n" +
                "FROM db_music m\n" +
                "LEFT JOIN db_like l ON m.id = l.musicid AND l.accountid = #{accountId}\n" +
                "LEFT JOIN db_artist a on m.artist_id=a.id\n" +
                "WHERE m.name = #{musicName};")
        List<JudgedMusic> selectBytitle(String musicName,Integer accountId);

        //----------like表相关操作----------//
        /**
         *  收藏歌曲
         * @param musicId
         * @param accountId
         * @return
         */
        @Insert("insert into db_like (musicid,accountid) \n" +
                "values (#{musicId},#{accountId})")
        int likeInsert(Integer musicId,Integer accountId);

        /**
         *  取消收藏歌曲
         * @param musicId
         * @param accountId
         * @return
         */
        @Delete("DELETE FROM db_like WHERE musicid = #{musicId} AND accountid=#{accountId};")
        int likeDelete(Integer musicId,Integer accountId);

        /**
         *  查询是否已经收藏
         * @param musicId
         * @param accountId
         * @return
         */
        @Select("SELECT Count(*) FROM db_like WHERE musicid = #{musicId} AND accountid=#{accountId};")
        int judgeLike(Integer musicId,Integer accountId);
        //------------like end-----------------//



        /**
         *  根据歌曲的点赞量查找规定数量歌曲歌曲
         * @param limit
         * @param offset
         * @return 歌曲列表
         */
        @Select("SELECT m.id, m.name, m.cover, m.length, m.file_url, m.artist_id, COUNT(l.id) AS like_count,\n" +
                "(CASE WHEN l.musicid IS NOT NULL THEN TRUE ELSE FALSE END) AS isLiked ,\n"+
                "a.name as artist\n" +
                "FROM db_music m\n" +
                "LEFT JOIN db_like l ON m.id = l.musicid\n" +
                "LEFT JOIN db_artist a on m.artist_id=a.id\n" +
                "GROUP BY m.id\n" +
                "ORDER BY like_count DESC\n" +
                "LIMIT #{limit} OFFSET #{offset};")
        List<JudgedMusic> findMusicByLikeCount(Integer limit, Integer offset);

        /**
         *  查询当前曲库中的音乐数量
         * @return
         */
        @Select("SELECT Count(*) FROM db_music;")
        int getMusicNum();


        //---------------artist---------------
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
