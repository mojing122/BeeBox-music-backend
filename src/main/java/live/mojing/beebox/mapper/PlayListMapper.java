package live.mojing.beebox.mapper;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import org.apache.ibatis.annotations.*;

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
     * @param playList
     * @return
     */
    @Insert("insert into db_playlist (name,description,creatorid,cover,createTime,updateTime) " +
            "values (#{playList.name}, #{playList.description},#{playList.creatorid},#{playList.cover},#{playList.createTime},#{playList.updateTime}); ")
    @Options(useGeneratedKeys=true, keyProperty="playList.id")
    int insertPlaylist(@Param("playList") PlayList playList);
    /**
     *  通过歌单ID查找其中包含的音乐
     * @param playlistId
     * @param accountId
     * @return
     */
    @Select(
            "SELECT \n" +
            "    m.id,\n" +
            "    m.name,\n" +
            "    m.cover,\n" +
            "    m.length,\n" +
            "    m.file_url,\n" +
            "    m.createTime,\n" +
            "    m.updateTime,\n" +
            "    (CASE WHEN l.musicid IS NOT NULL THEN TRUE ELSE FALSE END) AS isLiked,\n" +
            "    a.name as artist\n" +
            "FROM db_music_in_playlist mip\n" +

            "LEFT JOIN db_music  m ON  mip.musicid = m.id\n" +
            "LEFT JOIN db_like   l ON  m.id = l.musicid AND l.accountid = #{accountId}\n" +
            "LEFT JOIN db_artist a ON  m.artist_id=a.id\n" +

            "where mip.playlistid=#{playlistId}\n" +
            "ORDER BY m.createTime DESC\n")
    List<JudgedMusic> selectMusicInPlaylistById(Integer playlistId,Integer accountId);

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
    @Select("select count(*) from db_playlist_by_account where playlistid = #{playlistId} and accountid=#{accountId}")
    int judgePlaylistLiked(Integer accountId,Integer playlistId);//会返回受影响的行数，如果查询成功，则返回1，否则返回0。


    //----------like表相关操作----------//
    /**
     *  收藏歌单
     * @param playlistId
     * @param accountId
     * @return
     */
    @Insert("insert into db_playlist_by_account (accountid,playlistid) \n" +
            "values (#{accountId},#{playlistId})")
    int likeInsert(Integer playlistId,Integer accountId);

    /**
     *  取消收藏歌单
     * @param playlistId
     * @param accountId
     * @return
     */
    @Delete("DELETE FROM db_playlist_by_account WHERE accountid = #{accountId} AND playlistid=#{playlistId};")
    int likeDelete(Integer playlistId,Integer accountId);

    /**
     *  删除歌单
     * @param playlistId
     * @param accountId
     * @return
     */
    @Delete("DELETE FROM db_playlist WHERE id = #{playlistId} AND creatorid=#{accountId};")
    int deletePlaylist(Integer playlistId,Integer accountId);

    /**
     *  管理员删除歌单
     * @param playlistId
     * @return
     */
    @Delete("DELETE FROM db_playlist WHERE id = #{playlistId};")
    int deletePlaylistByAdmin(Integer playlistId);

    /**
     *  查询是否已经收藏
     * @param accountId
     * @param accountId
     * @return
     */
    @Select("SELECT Count(*) FROM db_playlist_by_account WHERE accountid = #{accountId} AND playlistid=#{playlistId};")
    int judgeLike(Integer playlistId,Integer accountId);

    //--------------------------
    /**
     *  展示我收藏的歌单
     * @param accountId
     * @return
     */
    @Select(
            "select \n" +
            "    p.id,\n" +
            "    p.name,\n" +
            "    p.description,\n" +
            "    p.creatorid,\n" +
            "    p.cover,\n" +
            "    p.createTime,\n" +
            "    p.updateTime\n" +
            "from db_playlist p \n"+
            "where creatorid=#{accountId};"
    )
    List<PlayList> ShowThePlaylistICreated(Integer accountId);

    @Select(
            "select \n" +
            "    p.id,\n" +
            "    p.name,\n" +
            "    p.description,\n" +
            "    p.creatorid,\n" +
            "    p.cover,\n" +
            "    p.createTime,\n" +
            "    p.updateTime\n" +
            "from db_playlist_by_account pba\n"+
            "left join db_playlist p on pba.playlistid=p.id \n"+
            "where accountid=#{accountId};"
    )
    List<PlayList> ShowMyFavoritePlaylists(Integer accountId);

    //----------歌单中插入删除歌曲相关操作----------//
    /**
     *  插入歌曲
     * @param musicId
     * @param playlistId
     * @return
     */
    @Insert("insert into db_music_in_playlist (playlistid,musicid) \n" +
            "values (#{playlistId},#{musicId})")
    int InsertMusic(Integer playlistId,Integer musicId);

    /**
     *  删除歌曲
     * @param musicId
     * @param playlistId
     * @return
     */
    @Delete("DELETE FROM db_music_in_playlist WHERE playlistid = #{playlistId} AND musicid=#{musicId};")
    int DeleteMusic(Integer playlistId,Integer musicId);

    /**
     *  查询是否已经收藏
     * @param musicId
     * @param playlistId
     * @return
     */
    @Select("SELECT Count(*) FROM db_music_in_playlist WHERE playlistid = #{playlistId} AND musicid=#{musicId};")
    int judgeExist(Integer playlistId,Integer musicId);
    //------------end-----------------//
}

