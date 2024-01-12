package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedPlayList;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
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
    public RestBean<JudgedPlayList> selectMusicInPlaylist(@SessionAttribute("account") AccountUser user,
                                                          @RequestParam("playlistId") Integer playlistId){

        int accountId = user.getId();

        //定义包含所需的各种信息的新歌单实体
        JudgedPlayList judgedPlayList=null;

        //查找歌单中的音乐
        List<Music> musicList=playListService.selectMusicInPlaylistById(playlistId);

        //设计歌单信息
        PlayList playList =playListService.findPlaylistById(playlistId);
        judgedPlayList.setName(playList.getName());
        judgedPlayList.setDescription(playList.getDesc());
        judgedPlayList.setListlength(musicList.size());
        judgedPlayList.setCover(playList.getCover());
        if(accountId==playList.getCreatorid()){
            judgedPlayList.setEditable(true);
        }

        //判断当前歌单是否被当前用户喜欢,设计收藏信息
        int isLiked=playListService.judgePlaylistLiked(accountId,playlistId);
        judgedPlayList.setMusicList(musicList);
        judgedPlayList.setIsLiked(isLiked);

        return RestBean.success(judgedPlayList);
    }

}
