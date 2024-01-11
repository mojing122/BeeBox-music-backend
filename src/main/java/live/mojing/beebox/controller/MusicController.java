package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


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
}
