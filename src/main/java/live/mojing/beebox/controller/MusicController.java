package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;


    //搜索音乐信息
    @PostMapping("/get-music-by-id")
    public RestBean<JudgedMusic> selectMusicById(
            @SessionAttribute("account") AccountUser user,
            @RequestParam("music_id") Integer musicId)
    {
        Integer userId=user.getId();
        JudgedMusic music= musicService.findMusicById(userId,musicId);

        return RestBean.success(music);
    }

    @PostMapping("/get-musicList-by-likeCount")
    public RestBean<List<JudgedMusic>> findMusicByLikeCount(@RequestParam("limit") Integer limit,
                                                      @RequestParam("offset")Integer offset)
    {
        List<JudgedMusic> musicList=musicService.findMusicByLikeCount(limit,offset);
        return RestBean.success(musicList);
    }

    @PostMapping("/like")
    public RestBean<String> likeMusic(@RequestParam("offset") Integer musicId,
                                      @SessionAttribute("account") AccountUser user,
                                      @RequestParam("offset") Boolean flag){
        Integer accountId=user.getId();
        if(flag){
            musicService.likeMusic(musicId,accountId);
            return RestBean.success("收藏成功!");
        }
        else{
            return RestBean.failure(403,"收藏失败!");
        }
    }
}
