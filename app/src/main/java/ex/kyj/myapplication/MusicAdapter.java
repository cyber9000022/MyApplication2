package ex.kyj.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

class MusicAdapter extends BaseAdapter {
    //데이터 담을 수 있는 공간 설정
    private Context ctx;
    ListView list;


    ArrayList<Music> items = new ArrayList<>();
    MusicAdapter(Context ctx, ListView list){
        this.ctx = ctx;
        this.list = list;

    }
    //아이템 갯수
    @Override
    public int getCount() {
        return items.size();
    }
    //아이템 삽입
    void addItem(Music item) {
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MusicItemView view = new MusicItemView(ctx);


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                for(int i =0;i<list.getChildCount();i++) {
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