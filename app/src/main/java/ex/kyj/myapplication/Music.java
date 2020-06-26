package ex.kyj.myapplication;

class Music {
    private String songname;
    private String songid;
    private String singer;

    Music(String songname,String singer,String songid){
        this.songname=songname;
        this.songid=songid;
        this.singer=singer;
    }

    String getSongid() {
        return songid;
    }
    String getSongname() {
        return songname;
    }
    String getSinger() {
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
