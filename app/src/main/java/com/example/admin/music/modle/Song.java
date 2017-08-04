package com.example.admin.music.modle;

/**
 * Created by Administrator on 2017/6/27.
 */

public class Song {
    //歌曲名称
    public String songName;
    // 歌曲文件名称
    public String songFileName;
    //歌曲名称长度
    public int songNameLength;
   public char[] getSongNameCharacters(){
       return songName.toCharArray();
   }
    public Song() {
    }

    public Song(String songName, String songFileName, int songNameLength) {
        this.songName = songName;
        this.songFileName = songFileName;
        this.songNameLength = songNameLength;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
        this.songNameLength = songName.length();
    }

    public String getSongFileName() {
        return songFileName;
    }

    public void setSongFileName(String songFileName) {
        this.songFileName = songFileName;
    }

    public int getSongNameLength() {
        return songNameLength;
    }


    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", songFileName='" + songFileName + '\'' +
                ", songNameLength=" + songNameLength +
                '}';
    }
}
