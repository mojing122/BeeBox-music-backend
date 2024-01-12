package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam("music_id") Integer musicId) {
        Integer userId=user.getId();
        JudgedMusic music= musicService.findMusicById(userId,musicId);

        return RestBean.success(music);
    }

    @PostMapping("get-musicList-by-likeCount")
    public RestBean<List<Music>> findMusicByLikeCount(Integer limit, Integer offset){
        List<Music> musicList=musicService.findMusicByLikeCount(limit,offset);
        return RestBean.success(musicList);
    }
}
