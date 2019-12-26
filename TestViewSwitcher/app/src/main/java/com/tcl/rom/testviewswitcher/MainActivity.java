package com.tcl.rom.testviewswitcher;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private static final int NUMBER_PER_SCREEN = 12;
    private ArrayList<DataItem> items = new ArrayList<>();
    private int screenNo = -1;
    private int screenCount;
    LayoutInflater inflater;
    private ViewSwitcher viewSwitcher;

    public static class DataItem{
        public String dataname;
        protected Drawable drawerable;

        public DataItem() { }

        public DataItem(String dataname, Drawable drawerable) {
            this.dataname = dataname;
            this.drawerable = drawerable;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = LayoutInflater.from(this.getApplicationContext());
        for(int i=0; i<40; i++){
            String label = ""+i;
            Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher_foreground);
            DataItem item = new DataItem(label, drawable);
            items.add(item);
        }
        screenCount = items.size() % NUMBER_PER_SCREEN == 0 ? items.size()/NUMBER_PER_SCREEN
                : items.size()/NUMBER_PER_SCREEN + 1;
        viewSwitcher = findViewById(R.id.viewSwitcher);
        viewSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return inflater.inflate(R.layout.slidelistview, null);
            }
        });
        next(null);
    }

    public void prev(View v){
        if(screenNo > 0){
            screenNo--;
            viewSwitcher.setInAnimation(this, R.anim.slide_in_left);
            viewSwitcher.setOutAnimation(this, R.anim.slide_in_right);
            ((GridView)viewSwitcher.getNextView()).setAdapter(adapter);
            viewSwitcher.showPrevious();
        }
    }

    public void next(View v){
        if(screenNo < screenCount - 1){
            screenNo++;
            viewSwitcher.setInAnimation(this, R.anim.slide_in_right);
            viewSwitcher.setOutAnimation(this, R.anim.slide_in_left);
            ((GridView)viewSwitcher.getNextView()).setAdapter(adapter);
            viewSwitcher.showNext();
        }
    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if(screenNo == screenCount - 1 && items.size() % NUMBER_PER_SCREEN != 0){
                return items.size() % NUMBER_PER_SCREEN;
            }
            return NUMBER_PER_SCREEN;
        }

        @Override
        public DataItem getItem(int position) {
            return items.get(screenNo * NUMBER_PER_SCREEN + position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null){
                view = inflater.inflate(R.layout.labelicon, null);
            }
            ImageView imageView = view.findViewById(R.id.imageview);
            imageView.setImageDrawable(getItem(position).drawerable);
            TextView textView = view.findViewById(R.id.textview);
            textView.setText(getItem(position).dataname);
            return view;
        }
    };


}
