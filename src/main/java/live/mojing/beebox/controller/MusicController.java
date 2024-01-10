package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.AllMusicMapper;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.AllMusicService;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private AllMusicService allMusicService;

    //搜索音乐信息
    @PostMapping("/get-music-by-id")
    public RestBean<Music> selectMusicById(
            @SessionAttribute("account") AccountUser user,
            @RequestParam("user_id") Integer userId,
            @RequestParam("music_id") Integer musicId) {
        Integer userid=user.getId();
        Music music = null;
        if(userid==userId){
            music= musicService.findMusicById(musicId);
        }
        return RestBean.success(music);
    }

    @GetMapping("/search-all-music")
    public RestBean<Object> selectMusicById(@RequestParam("music_name") String musicName) {
        List<Music> music = allMusicService.findAllMusicByName(musicName);
        return RestBean.success((Object) music);
    }
}
