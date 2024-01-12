package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface PlayListMapper {

    /**
     *  playlist分页查询
     * @param limit
     * @param offset
     * @return
     */
    @Select("select * from db_playlist order by createTime desc limit #{limit} offset #{offset}")
    List<PlayList> selectAllPlaylist(int limit,int offset);

    /**
     *   playlist插入
     * @param name
     * @param description
     * @param creatorId
     * @param cover
     * @param createTime
     * @param updateTime
     * @return
     */
    @Insert("insert into db_playlist (name,description,creatorid,cover,createTime,updateTime) \n" +
            "values (#{name}, #{description},#{accountId},#{cover},#{createTime},#{updateTime})")
    int insertPlaylist(String name,String description, Integer creatorId, String cover, Date createTime, Date updateTime);

    /**
     *  通过歌单ID查找其中包含的音乐
     * @param playlistId
     * @return
     */
    @Select("select * from db_music_in_playlist where playlistid = #{playlistId}")
    List<Music> selectMusicInPlaylistById(Integer playlistId);

    /**
     *  通过歌单ID查找其中包含的音乐
     * @param playlistId
     * @return
     */
    @Select("select * from db_playlist where id = #{playlistId}")
    PlayList findPlaylistById(Integer playlistId);

    /**
     *  判断当前歌单是否被当前用户收藏
     * @param playlistId
     * @param accountId
     * @return
     */
    @Select("select * from db_playlist_by_account where playlistid = #{playlistId} and accountid=#{accountId}")
    int judgePlaylistLiked(Integer accountId,Integer playlistId);//会返回受影响的行数，如果查询成功，则返回1，否则返回0。

//    /**
//     *  通过歌单名查找其中包含的音乐
//     * @param playListId
//     * @param accountId
//     * @return
//     */
//    @Select("SELECT \n" +
//            "    p.id,\n" +
//            "    p.name,\n" +
//            "    p.description,\n" +
//            "    p.creatorid,\n"+
//            "    p.cover,\n" +
//            "    p.createTime,\n" +
//            "    p.updateTime,\n" +
//            "    (CASE WHEN pba.playlistid IS NOT NULL THEN TRUE ELSE FALSE END) AS isLiked\n" +
//            "FROM \n" +
//            "    db_playlist p\n" +
//            "LEFT JOIN \n" +
//            "    db_playlist_by_account pba ON p.id = pba.playlistid AND pba.accountid = #{accountId}\n" +
//            "WHERE \n" +
//            "    p.id = #{playListId};")
//    List<Music> selectMusicInPlaylist(Integer accountId, Integer playListId);
}

