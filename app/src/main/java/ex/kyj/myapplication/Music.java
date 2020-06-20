package ex.kyj.myapplication;

public class Music {
    String songname;
    String songid;
    String singer;

    public Music(String songname,String singer,String songid){
        this.songname=songname;
        this.songid=songid;
        this.singer=singer;
    }

    public String getSongid() {
        return songid;
    }
    public String getSongname() {
        return songname;
    }
    public String getSinger() {
        return singer;
    }
    public void setSongid(String songid) {
        this.songid = songid;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
}
