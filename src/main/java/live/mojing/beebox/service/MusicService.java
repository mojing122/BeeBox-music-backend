package live.mojing.beebox.service;

import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MusicService{

    JudgedMusic findMusicById(Integer userid, Integer musicid);

    int insertMusic(String name,String cover,int length,String fileUrl,Integer artistId);

    int insertArtist(String name,String desc);

    List<Music> selectBytitle(String musicName);

    Artist findArtistById(Integer artistid);

    Artist findArtistByName(String artistname);
}

