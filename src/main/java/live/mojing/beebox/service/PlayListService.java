package live.mojing.beebox.service;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public interface PlayListService {
    //playlist分页查询
    List<PlayList> SelectAllPlaylist(int limit,int offset);

    //playlist插入
    int insertPlaylist( PlayList playList);

    List<JudgedMusic> selectMusicInPlaylistById(Integer playlistId, Integer accountId);

    PlayList findPlaylistById(Integer playlistId);

    int judgePlaylistLiked(Integer accountId,Integer playlistId);

    //-----------like-------------
    int likeMusicSheet(Integer playlistId,Integer accountId);
    int cancelLike(Integer playlistId,Integer accountId);
    int judgeLike(Integer playlistId,Integer accountId);

    List<PlayList> ShowThePlaylistICreated(Integer accountId);
    List<PlayList> ShowMyFavoritePlaylists(Integer accountId);

    int deletePlaylist(Integer playlistId,Integer accountId);
    int deletePlaylistByAdmin(Integer playlistId);

    int InsertMusic(Integer playlistId,Integer musicId);
    int DeleteMusic(Integer playlistId,Integer musicId);
    int judgeExist(Integer playlistId,Integer musicId);
}
