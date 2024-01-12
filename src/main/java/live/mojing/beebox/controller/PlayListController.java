package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedPlayList;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/playlist")
public class PlayListController {
    @Autowired
    private PlayListService playListService;

    @GetMapping("/get-playlist")
    public RestBean<List<PlayList>> selectThreePlaylists(@RequestParam("limit") int limit,
                                                         @RequestParam("offset") int offset){
        List<PlayList> playLists=playListService.SelectAllPlaylist(limit,offset);
        return RestBean.success(playLists);
    }

    @PostMapping("/insert-playlist")
    public RestBean<String> insertPlaylist(@RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @RequestParam("accountId") Integer accountId,
                                           @RequestParam("cover") String cover){
        int flag=playListService.insertPlaylist(name,description,accountId,cover);
        if(flag==1){
            return RestBean.success("插入歌单成功!");
        }
        return RestBean.failure(403,"插入歌单失败!");
    }

    @PostMapping("/get-music-by-playlistid")
    public RestBean<JudgedPlayList> selectMusicInPlaylist(@RequestParam("accountId") Integer accountId,
                                                       Integer playlistId){
        List<Music> musicList=playListService.selectMusicInPlaylist(playlistId);
        int isLiked=playListService.judgePlaylistLiked(accountId,playlistId);
        JudgedPlayList judgedPlayList=null;
        judgedPlayList.setMusicList(musicList);
        judgedPlayList.setIsLiked(isLiked);
        return RestBean.success(judgedPlayList);
    }

}
