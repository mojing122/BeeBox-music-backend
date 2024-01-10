package live.mojing.beebox.service.impl;

import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    private MusicMapper musicMapper;

    @Override
    public Music findMusicById(Integer id) throws UsernameNotFoundException {
        Music music=musicMapper.findMusicById(id);
        if (music ==null)
            throw new UsernameNotFoundException("未找到该音乐");
        return music;
    }

    //    public void MusicService(MusicMapper musicMapper) {
    //        this.musicMapper = musicMapper;
    //    }
}

