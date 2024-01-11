package live.mojing.beebox.service.impl;

import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    private MusicMapper musicMapper;


    @Override
    public JudgedMusic findMusicById(Integer userid, Integer musicid) throws UsernameNotFoundException {
        JudgedMusic music=musicMapper.findMusicById(userid,musicid);
        if (music ==null)
            throw new UsernameNotFoundException("未找到该音乐");
        return music;
    }

    @Override
    public int insertMusic(String name,String cover,int length,String fileUrl){
        int flag=musicMapper.insertMusic(name,cover,length,fileUrl);
        return flag;
    }

    @Override
    public int insertArtist(String name,String desc){
        int flag=musicMapper.insertArtist(name,desc);
        return flag;
    }

    @Override
    public List<Music> selectBytitle(String musicName) throws UsernameNotFoundException{
        List<Music> musicList= musicMapper.selectBytitle(musicName);
        if (musicList ==null)
            throw new UsernameNotFoundException("未找到该音乐");
        return musicList;
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

