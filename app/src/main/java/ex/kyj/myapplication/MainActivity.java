package ex.kyj.myapplication;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity {


    String item;
    Button button,eraseButton,renewalButton;
    ListView list;
    TextView textView;
    EditText text;
    MusicList musicList;
    MusicAdapter adapter;
    MusicItemView view;
    Youtube youtube;
    ConfirmDB confirmDB;
    public static final String DATABASE_NAME = "music.db";
    private static final String TABLE_NAME = "music_av";
    YouTubePlayerView youTubeView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (EditText) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        eraseButton = (Button) findViewById(R.id.Erasebutton);
        renewalButton = (Button) findViewById(R.id.renewalbutton);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        list = (ListView) findViewById(R.id.listView);
        textView =(TextView) findViewById(R.id.textView);

        list.setOnItemClickListener(itemClickListenerOfList);
        button.setOnClickListener(buttonClickListenerOfConfirm);
        eraseButton.setOnClickListener(buttonClickListenerOfErase);
        renewalButton.setOnClickListener(buttonClickListenerOfRenewal);
        text.addTextChangedListener(watcher);

        text.setFilters(new InputFilter[] {filterKoreanAlpahDigit});
        text.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500)});//글자수 제한 500자
        youtube = new Youtube(youTubeView);
        confirmDB = new ConfirmDB(this);

        confirmDB.init();
        youtube.initVideo();

    }
    //글자수 표시
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String input = text.getText().toString();
            textView.setText(String.format("%d/500자", input.length()));
            if(input.length()>=500){
                Toast.makeText(getApplicationContext(),"500자 까지만 입력이 가능합니다.",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    //리스트뷰 클릭시 해당하는 유튜브 재생
    private OnItemClickListener itemClickListenerOfList = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id) {
            youtube.setYoutube_id(((Music)adapterView.getAdapter().getItem(pos)).getSongid());
            youtube.playVideo();
        }
    };



    //버튼 클릭시 리스트 출력
    private OnClickListener buttonClickListenerOfConfirm = new OnClickListener() {
        public void onClick(View v) {

            String s = text.getText().toString().trim();
            //공백시 메시지 출력 및 재입력
            if(s.getBytes().length<=0){
                Toast.makeText(getApplicationContext(),"다시 입력해 주세요",Toast.LENGTH_LONG).show();
            } else {
                list = (ListView) findViewById(R.id.listView);
                musicList = new MusicList(getApplicationContext(),adapter,list);
                //인터넷 확인
                if(getConnectivityStatus()== true) {
                    //음악 리스트를 보여줌
                    musicList.setEmotion(MusicList.getResponse(s));
                    musicList.showList(musicList.getEmotion());
                } else {
                    Toast.makeText(getApplicationContext(),"인터넷을 연결하고 검색해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    //버튼 클릭시 텍스트 null로 만듬
    private OnClickListener buttonClickListenerOfErase = new OnClickListener() {
        public void onClick(View v) {
            text.setText(null);
        }
    };
    //버튼 클릭시 곡 갱신
    private OnClickListener buttonClickListenerOfRenewal = new OnClickListener() {
        public void onClick(View v) {
            //검색을 하지 않곡 갱신을 했는지 확인
            if(musicList == null  ||musicList.getEmotion() == null ){
                Toast.makeText(getApplicationContext(),"검색을 먼저 해주세요",Toast.LENGTH_SHORT).show();
            }else {
                musicList.showList(musicList.getEmotion());
            }
        }
    };
    //특수문자 및 이모티콘 입력 필터
    protected InputFilter filterKoreanAlpahDigit = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if(source.equals("")){ // for backspace
                return source;
            }
            if(source.toString().matches("[a-zA-Z0-9ㄱ-ㅎ가-힣 ]+")){
                return source;
            }
            Toast.makeText(getApplicationContext(),"특수문자 및 이모티콘은 입력하실 수 없습니다.",Toast.LENGTH_SHORT).show();
            return "";
        }
    };

    public boolean getConnectivityStatus(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            return  true;
        } else{
            return  false;
        }
    }

}

