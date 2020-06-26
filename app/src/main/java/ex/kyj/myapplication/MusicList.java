package ex.kyj.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;

public class MusicList {
    MusicAdapter adapter;
    Context ctx;
    ListView list;
    String emotion =null;
    public static final String DATABASE_NAME = "music.db";
    private static final String TABLE_NAME = "music_av";

    public MusicList(Context ctx, MusicAdapter adapter , ListView list){
        this.ctx = ctx;
        this.adapter = adapter;
        this.list = list;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getEmotion() {
        return emotion;
    }

    public static String getResponse(final String inputtext) {
        String answer;
        try {
            AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
                protected String doInBackground(String... url) {
                    BufferedReader br;
                    String emotion = null;
                    String JsonData;
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

    public void showList(String emotion) {

        try {

            SQLiteDatabase ReadDB = ctx.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            adapter = new MusicAdapter(ctx,list);
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
}
