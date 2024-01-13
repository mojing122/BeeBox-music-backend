package live.mojing.beebox.service.impl;

import live.mojing.beebox.mapper.PlayListMapper;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import live.mojing.beebox.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class PlayListServicelmpl implements PlayListService {
    @Autowired
    private PlayListMapper playListMapper;

    @Override
    public List<PlayList> SelectAllPlaylist(int limit,int offset){
        return playListMapper.selectAllPlaylist(limit,offset);
    }

    @Override
    public int insertPlaylist(String name, String desc, Integer accountId, String cover){
        Date createTime = new Date();
        Date updateTime = new Date();
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);
        int flag=playListMapper.insertPlaylist(decodedName,desc,accountId,cover,createTime,updateTime);
        return flag;
    }

    @Override
    public List<JudgedMusic> selectMusicInPlaylistById(Integer playlistId,Integer accountId){
        return playListMapper.selectMusicInPlaylistById(playlistId,accountId);
    }
    @Override
    public PlayList findPlaylistById(Integer playlistId){
        return playListMapper.findPlaylistById(playlistId);
    }

    @Override
    public int judgePlaylistLiked(Integer accountId,Integer playlistId){
        return playListMapper.judgePlaylistLiked(accountId,playlistId);
    }
    //---------------------------------------------
    @Override
    public int likeMusicSheet(Integer playlistId,Integer accountId){
        return playListMapper.likeInsert(playlistId,accountId);
    }
    @Override
    public int cancelLike(Integer playlistId,Integer accountId){
        return playListMapper.likeDelete(playlistId,accountId);
    }

    @Override
    public int judgeLike(Integer playlistId,Integer accountId){
        return playListMapper.judgeLike(playlistId,accountId);
    }

    @Override
    public List<PlayList> ShowThePlaylistICreated(Integer accountId){
        return playListMapper.ShowThePlaylistICreated(accountId);
    }

    @Override
    public List<PlayList> ShowMyFavoritePlaylists(Integer accountId){
        return playListMapper.ShowMyFavoritePlaylists(accountId);
    }
}
