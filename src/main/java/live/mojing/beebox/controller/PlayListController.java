package live.mojing.beebox.controller;

import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedPlayList;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.MusicService;
import live.mojing.beebox.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/playlist")
public class PlayListController {
    @Autowired
    private PlayListService playListService;

    @Value("${local.path}")
    private String  SAVE_PATH;

    @GetMapping("/get-playlist")
    public RestBean<List<PlayList>> selectThreePlaylists(@RequestParam("limit") int limit,
                                                         @RequestParam("offset") int offset){
        List<PlayList> playLists=playListService.SelectAllPlaylist(limit,offset);
        return RestBean.success(playLists);
    }

    @PostMapping("/insert-playlist")
    public RestBean<String> insertPlaylist(@RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @SessionAttribute("account")AccountUser accountUser,
                                           @RequestParam("cover") MultipartFile cover)
    {
        //上传封面图片
        String originalFilename = cover.getOriginalFilename(); //获取原来的文件名和后缀
        String extCover = "."+ originalFilename.split("\\.")[1];//后缀
        String path_cover = SAVE_PATH+"/cover/"+originalFilename;//拼接图片上传的路径 url+图片名
        String relative_path_cover="/cover/"+originalFilename;
        File destCover = new File(path_cover);
        if(!destCover.exists()){
            //如果路径不存在就创建目录
            destCover.mkdir();
        }
        try {
            cover.transferTo(destCover);
        } catch (IOException e) {
            e.printStackTrace();
            return RestBean.failure(420,"图片上传失败");
        }

        Integer accountId =accountUser.getId();
        int flag=playListService.insertPlaylist(name,description,accountId,relative_path_cover);
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
        JudgedPlayList judgedPlayList=new JudgedPlayList();

        //查找歌单中的音乐
        List<JudgedMusic> musicList=playListService.selectMusicInPlaylistById(playlistId,accountId);

        //设计歌单信息
        PlayList playList =playListService.findPlaylistById(playlistId);
        judgedPlayList.setName(playList.getName());
        judgedPlayList.setDescription(playList.getDescription());
        judgedPlayList.setListlength(musicList.size());
        judgedPlayList.setCover(playList.getCover());
        if(accountId==playList.getCreatorid()){
            judgedPlayList.setEditable(true);
        }
        else{
            judgedPlayList.setEditable(false);
        }

        //判断当前歌单是否被当前用户喜欢,设计收藏信息
        int isLiked=playListService.judgePlaylistLiked(accountId,playlistId);
        judgedPlayList.setMusicList(musicList);
        judgedPlayList.setIsLiked(isLiked);

        return RestBean.success(judgedPlayList);
    }

    @PostMapping("/like-or-cancel-like-musicsheet")
    public RestBean<String> likeMusicSheet(@RequestParam("playlistId") Integer playlistId,
                                      @SessionAttribute("account") AccountUser user,
                                      @RequestParam("flag") Boolean flag){
        Integer accountId=user.getId();
        int exist=playListService.judgeLike(playlistId,accountId);

        if(flag){
            if(exist==0){
                if(playListService.likeMusicSheet(playlistId,accountId)!=0)
                    return RestBean.success("收藏成功!");
                else
                    return RestBean.failure(401,"收藏失败!");
            }
            else
                return RestBean.failure(402,"用户已经收藏该歌单,请联系管理员!");
        }
        else{
            if(exist!=0){
                if(playListService.cancelLike(playlistId,accountId)!=0)
                    return RestBean.success("取消收藏成功!");
                else
                    return RestBean.failure(403,"取消收藏失败!");
            }
            else
                return RestBean.failure(404,"用户没有收藏该歌单,无法取消收藏,请联系管理员!");
        }
    }

    @GetMapping("/show-the-playlist-I-created")
    public RestBean<List<PlayList>> ShowThePlaylistICreated(@SessionAttribute("account") AccountUser user){
        Integer accountId= user.getId();
        List<PlayList> playLists=playListService.ShowThePlaylistICreated(accountId);
        return RestBean.success(playLists);
    }
    @GetMapping("/show-my-favourite-playlist")
    public RestBean<List<PlayList>> ShowMyFavoritePlaylists(@SessionAttribute("account") AccountUser user){
        Integer accountId= user.getId();
        List<PlayList> playLists=playListService.ShowMyFavoritePlaylists(accountId);
        return RestBean.success(playLists);
    }

    @PostMapping("/delete-playlist")
    public RestBean<String> deletePlaylist(@RequestParam("playlistId") Integer playlistId,
                                           @SessionAttribute("account") AccountUser user)
    {
        Integer accountId= user.getId();
        Integer flag=playListService.deletePlaylist(playlistId,accountId);
        if(flag!=0)
            return RestBean.success("删除歌单成功!");
        else
            return RestBean.failure(404,"删除歌单失败!");
    }
}
