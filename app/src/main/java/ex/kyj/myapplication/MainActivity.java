package ex.kyj.myapplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends YouTubeBaseActivity {

    YouTubePlayerView youTubeView;
    YouTubePlayer player;
    String item;
    String emotion;
    Button button,eraseButton,renewalButton;
    ListView list;
    TextView textView;
   private EditText text;

    MusicAdapter adapter;
    MusicItemView view;

    public static final String PACKAGE_DIR = "ex.kyj.myapplication";
    public static final String DATABASE_NAME = "music.db";
    private static final String TABLE_NAME = "music_av";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        eraseButton = (Button) findViewById(R.id.Erasebutton);
        renewalButton = (Button) findViewById(R.id.renewalbutton);
        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(itemClickListenerOfList);
        button.setOnClickListener(buttonClickListenerOfConfirm);
        eraseButton.setOnClickListener(buttonClickListenerOfErase);
        renewalButton.setOnClickListener(buttonClickListenerOfRenewal);
        text.setFilters(new InputFilter[] {filterKoreanAlpahDigit});
        text.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500)});
        textView =(TextView) findViewById(R.id.textView);

        try {
            boolean bResult = isCheckDB(getApplicationContext());
            // DB 확인
            if (!bResult) {
                // DB가 없으면 복사
                copyDB(getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initVideo();
        text.addTextChangedListener(watcher);
    }

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
    //DB 확인
    public boolean isCheckDB(Context mContext) {
        String filePath = "/data/data/" + PACKAGE_DIR + "/databases/" + DATABASE_NAME;
        File file = new File(filePath);
        if (file.exists()) {
            Log.d("MiniApp", "1");
            return true;
        }
        Log.d("MiniApp", "2");
        return false;
    }
    //Asset에서 db파일을 가져와 databases 밑에 db 복사
    public void copyDB(Context mContext) {
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/" + PACKAGE_DIR + "/databases";
        String filePath = "/data/data/" + PACKAGE_DIR + "/databases/" + DATABASE_NAME;
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("db/" + DATABASE_NAME);
            BufferedInputStream bis = new BufferedInputStream(is);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            player.cueVideo(item);
        }
    }
    //유튜브뷰 초기화
    public void initVideo(){
        String youYube_key = "AIzaSyB1Upwc_UqcfgRifFDLFLIr418zV6wFYuU";
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        youTubeView.initialize(youYube_key, listener);
    }

    //리스트뷰 클릭시 해당하는 유튜브 재생
    private OnItemClickListener itemClickListenerOfList = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id) {
            item = ((Music) adapter.getItem(pos)).getSongid();
            playVideo();
        }
    };
    //api에서 받은 감정을 통해 arousal valence 범위를 나눈다.
    public String getPlain(String emotion) {
        switch (emotion) {
            case "슬픔":
                return "arousal <=5 AND valence <=5";
            case "공포":
                return "arousal <=5 AND valence <=5";
            case "신뢰":
                return "arousal >=5 AND valence >=5";
            case "기대":
                return "arousal >=5 AND valence >=5";
            case "혐오":
                return "arousal >=5 AND valence <=5";
            case "분노":
                return "arousal >=5 AND valence <=5";
            case "기쁨":
                return "arousal >=5 AND valence >=5";
            case "놀라움":
                return "arousal >=5 AND valence >=5";
        }
        return null;
    }
    protected void showList(String emotion) {

        try {

            SQLiteDatabase ReadDB = this.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            adapter = new MusicAdapter();
            String plain = getPlain(emotion);
            //랜덤으로 곡을 뽑기 위해 RANDOM()이용한다.
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + plain + " ORDER BY RANDOM()", null);
            int count = 0;
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String name = c.getString(c.getColumnIndex("songname"));
                        String singer = c.getString(c.getColumnIndex("singer"));
                        String id = c.getString(c.getColumnIndex("id"));
                        //8곡을  뽑는다.
                        if (count < 8) {
                            adapter.addItem(new Music(name,singer,id));
                            //personList.add(persons);
                            count++;
                        }
                    } while (c.moveToNext());
                }
                c.close();
            }
            ReadDB.close();
            //화면에 보여주기 위해 Listview에 연결한다.
            list.setAdapter(adapter);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
    //버튼 클릭시 리스트 출력
    private OnClickListener buttonClickListenerOfConfirm = new OnClickListener() {
        public void onClick(View v) {

            String s = text.getText().toString().trim();
            //공백시 메시지 출력 및 재입력
            if(s.getBytes().length<=0){
                Toast.makeText(getApplicationContext(),"다시 입력해 주세요",Toast.LENGTH_LONG).show();
            } else {
                list = (ListView) findViewById(R.id.listView);
                //음악 리스트를 보여줌
                emotion=getResponse(s);
                showList(emotion);

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
            showList(emotion);
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

    public static String getResponse(final String inputtext) {
        String answer = null;
        try {
            AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
                protected String doInBackground(String... url) {
                    BufferedReader br = null;
                    String emotion = null;
                    String JsonData = "";
                    try {
                        //API에 GET메소드를 통해 JSON 형태로 받는다.
                        String text = URLEncoder.encode(inputtext, "utf-8");
                        String urlstr = "http://api.adams.ai/datamixiApi/omAnalysis?query=" + text + "&type=1&key=259218649902045256";
                        URL url1 = new URL(urlstr);
                        HttpURLConnection urlconnection = (HttpURLConnection) url1.openConnection();
                        urlconnection.setRequestMethod("GET");
                        br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
                        StringBuilder buffer = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            buffer.append(line).append("\n");
                        }
                        //JSON에서 원하는 데이터를 찾는다.
                        JsonData = buffer.toString();
                        JSONObject jo = new JSONObject(JsonData);
                        JSONObject retrun_object = jo.getJSONObject("return_object");
                        JSONArray result = retrun_object.getJSONArray("Result");
                        JSONArray result2 = result.getJSONArray(0);
                        emotion = result2.getString(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return emotion;
                }
            };
            answer = asyncTask.execute().get();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class MusicAdapter extends BaseAdapter {
        //데이터 담을 수 있는 공간 설정
        ArrayList<Music> items = new ArrayList<Music>();

        //아이템 갯수
        @Override
        public int getCount() {
            return items.size();
        }
        //아이템 삽입
        private void addItem(Music item) {
            items.add(item);
        }
        //선택한 아이템 번호
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        //중요 !! 어뎁터가 뷰를 만들어줌 (부분화면 정의한거 리턴)
        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //뷰를 만들어 줄때 재사용할 수 있게
            //화면에 안보여 지게되는것은 다시 재사용하게 되면서 new로 만들필요 없다.
            // 코드 재사용 convertView 사용

            MusicItemView view = new MusicItemView(getApplicationContext());
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi != null ? vi.inflate(R.layout.list_item, null) : null;
            }
            //convertView 가 null 이면

           view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    for(int i =0;i<list.getChildCount();i++){
                        list.getChildAt(i).setBackgroundColor(0xFFFFFFFF);
                    }
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        v.setBackgroundColor(0xFFF0FFFF);
                    }
                    return false;
                }
            });
            //위에서 정의한 arraylist 에서 하나하나의 데이터를 가져와서
            //Music item 객체에 담는다.
            Music item = items.get(position);
            // MusicItemView 클래스에 정의한 set 메서드에 데이터를 담아서 리턴한다.
            view.setName(item.getSongname());
            view.setSinger(item.getSinger());
            return view;
        }
    }
}

