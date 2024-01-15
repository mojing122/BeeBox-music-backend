package live.mojing.beebox.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.PlayList;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.auth.Account;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.AccountService;
import live.mojing.beebox.service.MusicService;
import live.mojing.beebox.service.PlayListService;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private AccountService accountService;

    @Value("${local.path}")
    private String SAVE_PATH;

    /**
     *  上传音乐
     *  请求路径：/api/admin/creat-music
     * @param file
     * @param cover
     * @param length
     * @param musicName
     * @param artist
     * @return 上传成功或失败的信息
     */
    @PostMapping("/creat-music")
    public RestBean<String> insertMusic(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cover") MultipartFile cover,
            @RequestParam("name") String musicName,
            @RequestParam("artist") String artist,
            @RequestParam("length") Integer length,
            @SessionAttribute("account")AccountUser accountUser
            )
    {

        // 1. 获取的是文件的完整名称，包括文件名称+文件拓展名
        String fileNameAndType = file.getOriginalFilename();
        String extMusic = "."+ fileNameAndType.split("\\.")[1];//后缀

        // 2. 查询数据库中是否存在当前要上传的音乐（歌曲名+歌手）

        // 判断当前上传的歌曲+歌手在数据库中是否同时存在，如果存在则上传失败（歌曲名+歌手 不能重复）
        Integer accountId =accountUser.getId();
        List<JudgedMusic> list = musicService.selectBytitle(musicName,accountId);
        if(list != null){
            for(JudgedMusic music : list){
                String artistName=music.getArtist();
                if(artistName.equals(artist)){
                    return RestBean.failure(403,"上传失败，数据库中存在此歌曲，不能重复上传");
                }
            }
        }

        // 3. 数据上传到服务器

        // 上传文件路径
        String path = SAVE_PATH+"/music/"+musicName+extMusic; //绝对路径
        String RelativePath="/music/"+musicName+extMusic; //相对路径

        // 上传音乐文件
        File destMusic = new File(path);
        if(!destMusic.exists()){//如果路径不存在就创建目录
            destMusic.mkdir();
        }
        try {// 将接收到的文件传输到给定目标路径
            file.transferTo(destMusic);
        } catch (IOException e) {
            e.printStackTrace();
            return RestBean.failure(404,"上传失败，服务器出现问题");
        }

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

        // 4. 跟新数据库中的内容（1. 准备数据  2. 调用 insert）
        // 插入数据
        try {
            int ret1=0;
            Artist SearchedArtist=musicService.findArtistByName(artist);
            if(SearchedArtist==null){
                String desc="一流歌手";
                ret1=musicService.insertArtist(artist,desc);
            }
            Integer artistId=musicService.findArtistByName(artist).getId();
            int ret = musicService.insertMusic(musicName,relative_path_cover,length,RelativePath,artistId);
            if(ret == 1 || ret1==1){// 数据插入成功
                return RestBean.success("数据库上传成功");
            }else{
                return RestBean.failure(409,"数据库上传失败");
            }
        }catch (BindingException e){
            // 数据库上传失败，将上传到文件夹中的数据删除
            destMusic.delete();
            destCover.delete();
            e.printStackTrace();
            return RestBean.failure(410,"数据库上传失败");
        }
    }

    @Autowired
    private PlayListService playListService;
    @PostMapping("/delete-playlist")
    public RestBean<String> deletePlaylist(@RequestParam("playlistId") Integer playlistId)
    {
        PlayList playList=playListService.findPlaylistById(playlistId);
        String coverPath=playList.getCover();
        Integer flag=playListService.deletePlaylistByAdmin(playlistId);


        // 指定要删除的文件路径
        String filePath = SAVE_PATH+coverPath;

        // 创建File对象
        File file = new File(filePath);

        // 判断文件是否存在
        if (file.exists()) {
            // 调用delete()方法来删除文件
            if (file.delete()) {
                System.out.println("歌单封面文件删除成功");
            } else {
                System.out.println("歌单封面文件删除失败");
            }
        } else {
            System.out.println("歌单封面文件不存在");
        }

        if(flag!=0)
            return RestBean.success("删除歌单成功!");
        else
            return RestBean.failure(404,"删除歌单失败!");
    }

    @PostMapping("/delete-music")
    public RestBean<String> deleteMusic(@RequestParam("musicId") Integer musicId)
    {
        Music music=musicService.findMusicById(musicId);
        String musicCoverPath=music.getCover();
        String musicFilePath=music.getFileUrl();
        Integer flag1=musicService.deleteMusicByAdmin(musicId);


        // 指定要删除的文件路径
        String coverPath = SAVE_PATH+musicCoverPath;
        String filePath = SAVE_PATH+musicFilePath;

        // 创建File对象
        File cover = new File(coverPath);
        File file  = new File(filePath);

        //-----删除音乐封面文件------//
        // 判断文件是否存在
        if (cover.exists()) {
            // 调用delete()方法来删除文件
            if (cover.delete()) {
                System.out.println("歌曲封面文件删除成功");
            } else {
                System.out.println("歌曲封面文件删除失败");
            }
        } else {
            System.out.println("歌曲封面文件不存在");
        }

        //-----删除音乐文件------//
        // 判断文件是否存在
        if (file.exists()) {
            // 调用delete()方法来删除文件
            if (file.delete()) {
                System.out.println("歌曲文件删除成功");
            } else {
                System.out.println("歌曲文件删除失败");
            }
        } else {
            System.out.println("歌曲文件不存在");
        }

        if(flag1!=0)
            return RestBean.success("删除歌曲成功!");
        else
            return RestBean.failure(404,"删除歌曲失败!");
    }

    @GetMapping("/get-user-list")
    public RestBean<List<Account>> getUserList(){
        List<Account> accountList = accountService.selectAllAccount();
        return RestBean.success(accountList);
    }

    @PostMapping("/reset-account-role")
    public RestBean<String> resetAccountRole(@RequestParam("role") String role,
                                             @RequestParam("id") int id){
        int response =accountService.resetRoleById(role, id);
        if(response>0){
            return RestBean.success("修改成功");
        }else {
            return RestBean.failure(407,"修改失败");
        }

    }
}
