package live.mojing.beebox.service;

import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.Music;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface MusicService{

    Music findMusicById(Integer id);

    //    void MusicService(MusicMapper musicMapper);
    //    public void MusicService(MusicMapper musicMapper) {
    //        this.musicMapper = musicMapper;
    //    }

}

