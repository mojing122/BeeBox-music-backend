package live.mojing.beebox.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.service.MusicService;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private MusicService musicService;

    @Value("${local.path}")
    private String SAVE_PATH;

    /**
     *  上传音乐
     *  请求路径：/api/admin/creat-music
     * @param file
     * @param cover
     * @param length
     * @param name
     * @param artist
     * @return 上传成功或失败的信息
     */
    @PostMapping("/creat-music")
    public RestBean<String> insertMusic(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cover") MultipartFile cover,
            @RequestParam("name") String name,
            @RequestParam("artist") String artist,
            @RequestParam("length") Integer length
    )
    {

        // 1. 获取的是文件的完整名称，包括文件名称+文件拓展名
        String fileNameAndType = file.getOriginalFilename();
        String extMusic = "."+ fileNameAndType.split("\\.")[1];//后缀

        // 2. 查询数据库中是否存在当前要上传的音乐（歌曲名+歌手）

        // 判断当前上传的歌曲+歌手在数据库中是否同时存在，如果存在则上传失败（歌曲名+歌手 不能重复）
        List<Music> list = musicService.selectBytitle(name);
        if(list != null){
            for(Music music : list){
                Integer artistId=music.getArtistId();
                Artist artistInDatabase=musicService.findArtistById(artistId);;
                String  artistName=artistInDatabase.getName();
                if(artistName.equals(artist)){
                    return RestBean.failure(403,"上传失败，数据库中存在此歌曲，不能重复上传");
                }
            }
        }

        // 3. 数据上传到服务器

        // 上传文件路径
        String path = SAVE_PATH+"/music/"+name+extMusic; //绝对路径
        String RelativePath="/music/"+name+extMusic; //相对路径

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
            int ret = musicService.insertMusic(name,path_cover,length,RelativePath,artistId);
            if(ret == 1 && ret1==1){// 数据插入成功
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
}
