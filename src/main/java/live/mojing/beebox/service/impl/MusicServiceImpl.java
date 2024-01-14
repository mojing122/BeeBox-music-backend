package live.mojing.beebox.service.impl;

import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;



@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    private MusicMapper musicMapper;

    /**
     * 音乐相关Service
     */
    @Override
    public JudgedMusic findJudgedMusicById(Integer userid, Integer musicid) {
        return musicMapper.findJudgedMusicById(userid,musicid);
    }
    @Override
    public Music findMusicById(Integer musicId){
        return musicMapper.findMusicById(musicId);
    }

    @Override
    public int insertMusic(String name, String cover, int length, String fileUrl, Integer artistId){
        /**
         *  获取上传的时间
         *  将获取的时间格式化为：年-月-日 的形式
         */
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String time = simpleDateFormat.format(new Date());
        Date createTime = new Date();
        Date updateTime = new Date();
        int flag=musicMapper.insertMusic(name,cover,length,fileUrl,artistId,createTime,updateTime);
        return flag;
    }

    @Override
    public int deleteMusicByAdmin(Integer musicId){
        return musicMapper.deleteMusicByAdmin(musicId);
    }

    @Override
    public List<JudgedMusic> selectBytitle(String musicName,Integer accountId){
        List<JudgedMusic> musicList= musicMapper.selectBytitle(musicName,accountId);
        return musicList;
    }

    @Override
    public List<JudgedMusic> findMusicByLikeCount(Integer limit, Integer offset){
        List<JudgedMusic> musicList =musicMapper.findMusicByLikeCount(limit,offset);
        return musicList;
    }

    @Override
    public int likeMusic(Integer musicId,Integer accountId){
        return musicMapper.likeInsert(musicId,accountId);
    }
    @Override
    public int cancelLike(Integer musicId,Integer accountId){
        return musicMapper.likeDelete(musicId,accountId);
    }

    @Override
    public int judgeLike(Integer musicId,Integer accountId){
        return musicMapper.judgeLike(musicId,accountId);
    }
    @Override
    public int getMusicNum(){
        return musicMapper.getMusicNum();
    }

    /**
     * 艺术家相关Service
     */
    @Override
    public int insertArtist(String name,String desc){
        Date createTime = new Date();
        Date updateTime = new Date();
        int flag=musicMapper.insertArtist(name,desc,createTime,updateTime);
        return flag;
    }

    @Override
    public Artist findArtistById(Integer artistid) {
        Artist artist= musicMapper.findArtistById(artistid);
        return artist;
    }

    @Override
    public Artist findArtistByName(String  artistname){
        Artist artist= musicMapper.findArtistByName(artistname);
        return artist;
    }


}

