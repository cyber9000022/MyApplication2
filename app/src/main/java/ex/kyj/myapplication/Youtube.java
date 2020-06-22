package ex.kyj.myapplication;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Youtube {
    String Youtube_id;
    YouTubePlayerView youTubeView;
    YouTubePlayer player;
    String youYube_key = "AIzaSyB1Upwc_UqcfgRifFDLFLIr418zV6wFYuU";

    public Youtube(YouTubePlayerView youTubeView){
        this.youTubeView=youTubeView;
    }
    public void setYoutube_id(String youtube_id) {
        Youtube_id = youtube_id;
    }

    public String getYoutube_id() {
        return Youtube_id;
    }

    private YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {

        //초기화 성공시
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

            player=youTubePlayer;
            player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(String s) {
                    player.play();
                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {

                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        }

    };
    //영상이 재생 중이면 멈추고 새로운 영상 재생
    public void playVideo(){
        if(player != null) {
            if (player.isPlaying()) {
                player.pause();
            }
            player.cueVideo(Youtube_id);
        }
    }
    //유튜브뷰 초기화
    public void initVideo(){
        youTubeView.initialize(youYube_key, listener);
    }
}
