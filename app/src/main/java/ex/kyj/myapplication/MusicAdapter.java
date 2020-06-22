package ex.kyj.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

class MusicAdapter extends BaseAdapter {
    //데이터 담을 수 있는 공간 설정
    Context ctx;
    ListView list;


    ArrayList<Music> items = new ArrayList<Music>();
    public MusicAdapter(Context ctx, ListView list){
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
    //중요 !! 어뎁터가 뷰를 만들어줌 (부분화면 정의한거 리턴)
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //뷰를 만들어 줄때 재사용할 수 있게
        //화면에 안보여 지게되는것은 다시 재사용하게 되면서 new로 만들필요 없다.
        // 코드 재사용 convertView 사용
        MusicItemView view = new MusicItemView(ctx);
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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