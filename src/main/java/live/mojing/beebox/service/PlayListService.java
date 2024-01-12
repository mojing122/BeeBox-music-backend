package live.mojing.beebox.service;

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
    int insertPlaylist(String name, String desc, Integer accountId, String cover);

    List<Music> selectMusicInPlaylistById(Integer playlistId);

    PlayList findPlaylistById(Integer playlistId);

    int judgePlaylistLiked(Integer accountId,Integer playlistId);
}
