package live.mojing.beebox.service;

import live.mojing.beebox.mapper.entity.Artist;
import live.mojing.beebox.mapper.entity.JudgedEntity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MusicService{

    JudgedMusic findJudgedMusicById(Integer userid, Integer musicid);
    Music findMusicById(Integer musicId);
    int deleteMusicByAdmin(Integer musicId);

    int insertMusic(String name, String cover, int length, String fileUrl, Integer artistId);

    int insertArtist(String name, String desc);

    List<JudgedMusic> selectBytitle(String musicName,Integer accountId);

    Artist findArtistById(Integer artistid);

    Artist findArtistByName(String artistname);

    List<JudgedMusic> findMusicByLikeCount(Integer limit, Integer offset);

    int likeMusic(Integer musicId,Integer accountId);
    int cancelLike(Integer musicId,Integer accountId);
    int judgeLike(Integer musicId,Integer accountId);

    int getMusicNum();
}

