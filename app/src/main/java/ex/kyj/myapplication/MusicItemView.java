package ex.kyj.myapplication;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kang on 2017-08-23.
 */

public class MusicItemView extends LinearLayout {

    TextView textView;
    TextView textView2;

    //viwe를 위한 필수 생성자 2개
    public MusicItemView(Context context) {
        super(context);

        init(context);
    }

    public MusicItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        //단말이 시작될때 기본적으로 뒤에서 실행
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //단말이 시작될때 리스트 아이템 디자인을 정의한 singer_item.xml 이
        // activity_main.xml에 정의한 리니어 레이아웃에 달라 붙는다.
        inflater.inflate(R.layout.list_item, this, true);

        //singer_item 에 정의한 것들 선택자 가져오기
        textView = (TextView) findViewById(R.id.listtext1);
        textView2 = (TextView) findViewById(R.id.listtext2);
    }

    //MainActivity getView() 에서 setName 이 호출되면 값을 textView 에 넣어준다.
    public void setName(String name) {

        textView.setText(name);
    }

    public void setSinger(String mobile) {

        textView2.setText(mobile);
    }

}
