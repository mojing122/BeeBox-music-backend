package live.mojing.beebox.service;

import live.mojing.beebox.mapper.entity.Music;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AllMusicService {

      List<Music> findAllMusicByName(String musicName) throws UsernameNotFoundException;

}
