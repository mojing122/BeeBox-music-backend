package live.mojing.beebox.service.impl;

import live.mojing.beebox.mapper.AllMusicMapper;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.service.AllMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllMusicServicelmpl implements AllMusicService {
    @Autowired
    private AllMusicMapper allMusicMapper;



    @Override
    public  List<Music> findAllMusicByName(String musicName) throws UsernameNotFoundException {
        List<Music> music=allMusicMapper.findAllMusic(musicName);
        if (music ==null)
            throw new UsernameNotFoundException("未找到该音乐");
        return music;
    }


}
