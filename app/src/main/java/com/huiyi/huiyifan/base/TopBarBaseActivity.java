package com.huiyi.huiyifan.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyi.huiyifan.R;

/**
 * Created by LW on 2017/8/1.
 */

public abstract class TopBarBaseActivity extends AppCompatActivity {

    private Toolbar toolbar;//标题栏
    private FrameLayout frameLayout;//布局
    private TextView tvTitle;//标题
    private OnClickListener onClickListenerLeft;


    public interface OnClickListener{
        void onClick();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titlebar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.viewContent);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        //继承TopBarActivity的布局及解析到FrameLayout 里面
        LayoutInflater.from(TopBarBaseActivity.this).inflate(getContentView(),frameLayout);

        //初始化设置Toolbar
        setSupportActionBar(toolbar);
        //设置不显示自带title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        init(savedInstanceState);
    }

    protected void setTitle(String title){
        if (!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
    }

    protected void setLeftButton(){
        setLeftButton(R.drawable.back,null);
    }

    //添加一个方法用于设置图标资源id 和监听器
    protected void setLeftButton(int icomResId, OnClickListener onClickListener){
        toolbar.setNavigationIcon(icomResId);
        this.onClickListenerLeft =  onClickListener;
    }

    //重写方法,处理点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onClickListenerLeft.onClick();
        }
        return true;
    }



    protected abstract int getContentView();
    protected abstract void init(Bundle savedInstanceState);
}
