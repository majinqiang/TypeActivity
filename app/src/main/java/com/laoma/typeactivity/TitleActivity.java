package com.laoma.typeactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by majinqiang on 3/24/2017.
 */

public class TitleActivity extends BaseActivity {
    private StateViewHelper stateViewHelper;
    private Toolbar mToolbar;
    private ActionBar mActionBar;

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        super.setContentView(R.layout.common_title_bar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化 为ContentView
        stateViewHelper = new BaseStateViewWrapper(this);
        setMode(StateViewHelper.MODE_CONTENT);
        stateViewHelper.setContentRoot((ViewGroup) findViewById(R.id.container));
        //添加三种mode的View进去
        stateViewHelper.setModeView(R.layout.view_activity_loading, StateViewHelper.MODE_LOADING);
        stateViewHelper.setModeView(R.layout.view_activity_error, StateViewHelper.MODE_ERROR);
        stateViewHelper.setModeView(R.layout.view_activity_empty, StateViewHelper.MODE_EMPTY);
    }


    //先去找父View一个级别的view，如果为null，则去找子VIew里面的view
    public View findViewById(int id){
        View view = super.findViewById(id);
        if(view!=null){
            return view;
        } else {
            return stateViewHelper.findViewById(id);
        }
    }

    public Toolbar getToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        return mToolbar;
    }
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mToolbar.setTitle(title);
    }
    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mToolbar.setTitle(titleId);
    }
    public void hideTitleBar() {
        if (mActionBar != null) {
            mActionBar.hide();
        }
    }
    //设置ContentVIew，子类调用
    public void setContentView(int layoutResID) {
        stateViewHelper.setContentView(layoutResID);
    }
    public void setContentView(View view) {
        stateViewHelper.setContentView(view);
    }
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        stateViewHelper.setContentView(view, params);
    }
    public void setModeView(View view, int mode) {
        stateViewHelper.setModeView(view, mode);
    }
    public void setModeView(int view, int mode) {
        stateViewHelper.setModeView(view, mode);
    }
    public void setMode(int mode) {
        stateViewHelper.setMode(mode);
    }
    public int getMode() {
        return stateViewHelper.getMode();
    }

    private class BaseStateViewWrapper extends StateViewHelper {

        public BaseStateViewWrapper(Context context) {
            super(context);
        }
        @Override
        public boolean onViewClear(View subView) {
            return subView != null && subView != mToolbar;
        }
    }
}
