package ex.kyj.myapplication;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

class Youtube {
    private String Youtube_id;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    Youtube(YouTubePlayerView youTubeView){
        this.youTubeView=youTubeView;
    }
    void initVideo(){
        String youYutube_key = "AIzaSyB1Upwc_UqcfgRifFDLFLIr418zV6wFYuU";
        youTubeView.initialize(youYutube_key, listener);
    }

    void setYoutube_id(String youtube_id) {
        Youtube_id = youtube_id;
    }

    private YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {

        //초기화 성공시
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

            player=youTubePlayer;
            youTubePlayer.setShowFullscreenButton(false);
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
    void playVideo(){
        if(player != null) {
            if (player.isPlaying()) {
                player.pause();
            }
            player.cueVideo(Youtube_id);
        }

    }
    //유튜브뷰 초기화

}
