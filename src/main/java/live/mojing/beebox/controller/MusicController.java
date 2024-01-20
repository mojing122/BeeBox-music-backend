package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping("get-num")
    public RestBean<Integer> getMusicNum(){
        return RestBean.success(musicService.getMusicNum());
    }

    //搜索音乐信息
    @PostMapping("/get-music-by-id")
    public RestBean<JudgedMusic> selectMusicById(
            @SessionAttribute("account") AccountUser user,
            @RequestParam("music_id") Integer musicId)
    {
        Integer userId=user.getId();
        JudgedMusic music= musicService.findJudgedMusicById(userId,musicId);

        return RestBean.success(music);
    }

    @GetMapping("/get-music-by-name")
    public RestBean<List<JudgedMusic>> selectMusicByName(
            @SessionAttribute("account") AccountUser accountUser,
            @RequestParam("musicName") String musicName)
    {
        Integer accountId=accountUser.getId();
        List<JudgedMusic> musicList= musicService.selectBytitle(musicName,accountId);

        return RestBean.success(musicList);
    }

    @PostMapping("/get-musicList-by-likeCount")
    public RestBean<List<JudgedMusic>> findMusicByLikeCount(@RequestParam("limit") Integer limit,
                                                      @RequestParam("offset")Integer offset)
    {
        List<JudgedMusic> musicList=musicService.findMusicByLikeCount(limit,offset);
        return RestBean.success(musicList);
    }

    @PostMapping("/like-or-cancel-like-music")
    public RestBean<String> likeMusic(@RequestParam("musicId") Integer musicId,
                                      @SessionAttribute("account") AccountUser user,
                                      @RequestParam("flag") Boolean flag){
        Integer accountId=user.getId();
        int exist=musicService.judgeLike(musicId,accountId);

        if(flag){
            if(exist==0){
                if(musicService.likeMusic(musicId,accountId)!=0)
                    return RestBean.success("收藏成功!");
                else
                    return RestBean.failure(401,"收藏失败!");
            }
            else
                return RestBean.failure(402,"用户已经收藏该歌曲,请联系管理员!");
        }
        else{
            if(exist!=0){
                if(musicService.cancelLike(musicId,accountId)!=0)
                    return RestBean.success("取消收藏成功!");
                else
                    return RestBean.failure(403,"取消收藏失败!");
            }
            else
                return RestBean.failure(404,"用户没有收藏该歌曲,无法取消收藏,请联系管理员!");
        }
    }
}
