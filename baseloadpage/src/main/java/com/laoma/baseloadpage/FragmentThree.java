package com.laoma.baseloadpage;

import android.view.View;
import android.widget.TextView;

/**
 * Created by majinqiang on 3/24/2017.
 */

public class FragmentThree extends BaseFragment {

    @Override
    public void fetchData() {  //这个方法在下面的basefragment中会给出为什么
        getmLoadingPager().loadData();
    }

    @Override
    protected View initSuccess() {
        TextView tv = new TextView(UIUtils.getContext());
        tv.setText(this.getClass().getSimpleName());
        return tv;
    }

    @Override
    protected LoadingPager.LoadResult initData() {
        return LoadingPager.LoadResult.EMPTY;
    }
}
