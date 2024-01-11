package live.mojing.beebox.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import live.mojing.beebox.mapper.MusicMapper;
import live.mojing.beebox.mapper.entity.JudgedMusic;
import live.mojing.beebox.mapper.entity.Music;
import live.mojing.beebox.mapper.entity.user.AccountUser;
import live.mojing.beebox.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.scanner.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import live.mojing.beebox.mapper.MusicMapper;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.scanner.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public int insert(String name,String cover,int length,String fileUrl){
        int flag=musicMapper.insert(name,cover,length,fileUrl);
        return flag;
    }


    @Override
    public List<Music> selectBytitle(String musicName) throws UsernameNotFoundException{
        List<Music> musicList= musicMapper.selectBytitle(musicName);
        if (musicList ==null)
            throw new UsernameNotFoundException("未找到该音乐");
        return musicList;
    }


}

