package live.mojing.beebox.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.RestBean;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.MusicService;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.scanner.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @Value("${music.local.path}")
    private String SAVE_PATH;


    //搜索音乐信息
    @PostMapping("/get-music-by-id")
    public RestBean<JudgedMusic> selectMusicById(
            @SessionAttribute("account") AccountUser user,
            @RequestParam("music_id") Integer musicId) {
        Integer userId=user.getId();
        JudgedMusic music= musicService.findMusicById(userId,musicId);

        return RestBean.success(music);
    }

    /**
     *  上传音乐
     *  请求路径：/music/upload
     * @param file 上传歌曲
     * @param request 请求，验证是否登录
     * @param response
     * @return 返回true表示上传成功，返回false表示上传失败
     */
    @RequestMapping("/upload")
    public RestBean<String> insertMusic(
//                                        @RequestParam String singer,
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam ("filename") MultipartFile file,
                                        @RequestParam("cover") String cover,
                                        @RequestParam("length") Integer length
                                        )
    {

        // 1. 检查是否登录
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("account")==null){
            System.out.println("没有登录");
            return  RestBean.failure(402,"请登录后再进行上传");
        }

        // 2. 获取的是文件的完整名称，包括文件名称+文件拓展名
        String fileNameAndType = file.getOriginalFilename();

        // 3. 查询数据库中是否存在当前要上传的音乐（歌曲名+歌手）
        /**
         *  获取标题（标题不包含后缀.mp3）
         *  使用 lastIndexOf 从后向前找第一个 .
         */
        int index = fileNameAndType.lastIndexOf(".");
        String name = fileNameAndType.substring(0,index);


        // 使用 list 存放歌曲，获取歌曲名
        List<Music> list = musicService.selectBytitle(name);
//        if(list != null){
//            for(Music music : list){
//                // 判断当前上传的歌曲+歌手在数据库中是否存在，如果存在则上传失败（歌曲名+歌手 不能重复）
//                if(music.getSinger().equals(singer)){
//                    return RestBean.failure(403,"上传失败，数据库中存在此歌曲，不能重复上传");
//                }
//            }
//        }

        // 2. 数据上传到服务器

        // 上传文件路径
        String path = SAVE_PATH+fileNameAndType;

        // 上传文件
        File dest = new File(path);
        if(!dest.exists()){
            //如果路径不存在就创建目录
            dest.mkdir();
        }
        try {
            // 将接收到的文件传输到给定目标路径
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return RestBean.failure(404,"上传失败，服务器出现问题");
        }

        // 3. 判断上传的文件是否为mp3文件（判断是否存在 TAG 字符）
        File file1 = new File(path);
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file1.toPath());
            if(bytes == null){
                return RestBean.failure(405,"上传失败，文件不存在");
            }
            String str = new String(bytes);
            if(!str.contains("TAG")){
                file1.delete();
                return RestBean.failure(406,"上传的文件不是mp3文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return RestBean.failure(407,"上传失败，服务器出现问题");
        }

        // 4. 将数据上传到数据库中（1. 准备数据  2. 调用 insert）

        /**
         *  url 的作用： 播放音乐->发送 http 请求
         */
        String fileUrl = "/music/get?path="+name; // 将 url 存入数据库时不用加后缀 .mp3，在取数据的时候加一个后缀就可以了

//        /**
//         *  获取上传的时间
//         *  将获取的时间格式化为：年-月-日 的形式
//         */
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String time = simpleDateFormat.format(new Date());

        /**
         *  获取 cover,length,
         * 登录成功后将用户信息写到 session 中，通过 session 中key值 就可以获取到对应的 value 值
         */
//        // 封面链接cover
//        Music music = (Music)session.getAttribute("music");
//        String cover = music.getCover();
//        int length  =  music.getLength();

        // 插入数据
        try {
            int ret = musicService.insert(name,cover,length,path);
            if(ret == 1){
                // 数据插入成功
                // 这里应该跳转到音乐列表页面
//                response.sendRedirect("/list.html");
                response.sendRedirect("/");
                return RestBean.failure(408,"数据库上传成功");
            }else{
                // 数据插入失败
                return RestBean.failure(409,"数据库上传失败");
            }
        }catch (BindingException | IOException e){
            // 数据库上传失败，将上传到文件夹中的数据删除
            dest.delete();
            e.printStackTrace();
            return RestBean.failure(410,"数据库上传失败");
        }
    }
}
