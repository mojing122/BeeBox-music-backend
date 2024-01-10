package live.mojing.beebox.service.impl;

import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


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

//    @Override
//    public List<Music> findAllMusicByName(String musicName) throws UsernameNotFoundException {
//        List<Music> music=MusicMapper.findAllMusicByName(musicName);
//        if (music ==null)
//            throw new UsernameNotFoundException("未找到该音乐");
//        return music;
//    }

}

