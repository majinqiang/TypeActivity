package com.laoma.baseloadpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by majinqiang on 3/24/2017.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPager mLoadingPager;


    protected boolean isViewInitiated;
    protected boolean isVisiableToUser;
    protected boolean isDataInitiated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLoadingPager == null) {//第一次执行
            //BaseFragment 也是基类，就定义，具体实现也得交给子类实现，
            mLoadingPager = new LoadingPager(UIUtils.getContext()) {

                //BaseFragment 也是基类，就定义，具体实现也得交给子类实现，
                @Override
                public LoadResult initData() {
                    return BaseFragment.this.initData();
                }

                @Override
                protected View initSuccess() {
                    return BaseFragment.this.initSuccess();
                }
            };
        }
// else {
// ((ViewGroup) mLoadingPager.getParent()).removeView(mLoadingPager);
// }
        return mLoadingPager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isViewInitiated = true;
        prepareFetchData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        this.isVisiableToUser = isVisibleToUser;
        prepareFetchData();
    }


    public abstract void fetchData();

    public boolean prepareFetchData() {
        boolean b = prepareFetchData(false);
        return b;
    }

    /**
     * 是否预加载
     *
     * @param forceUpdate
     * @return
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisiableToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    public LoadingPager getmLoadingPager() {
        return mLoadingPager;
    }


    /**
     * 创建成功视图，交给具体子类完成<br>
     * 返回成功视图，正在加载数据完成之后，并且数据加载成功，我们必须告知具体的成功制图<br>
     * initSuccess()它是LoadingPager的同名方法,实现了一个中转
     * <br> 子类完成的，子类这个方法返回的view就是oncreateView方法中返回的view
     *
     * @return 成功视图
     */
    protected abstract View initSuccess();

    /**
     * 真正加载数据，必须实现，直接抽象让子类实现<br>
     * loadData()调用的时候被调用<br>
     * initData 它是LoadingPager的同名方法,实现了一个中转
     *
     * @return 加载状态返回
     */
    protected abstract LoadingPager.LoadResult initData();

    /**
     * @param object 网络数据json解析之后的对象
     * @return 返回成功、失败、空
     */
    public LoadingPager.LoadResult checksState(Object object) {
        if (object == null) {
            return LoadingPager.LoadResult.EMPTY;
        }
        //如果是对象类型 list
        if (object instanceof List) {
            if (((List) object).size() == 0) {
                return LoadingPager.LoadResult.EMPTY;
            }
        }
        //如果对象类型是map
        if (object instanceof Map) {
            if (((Map) object).size() == 0) {
                return LoadingPager.LoadResult.EMPTY;
            }
        }
        return LoadingPager.LoadResult.SUCCESS;
    }
}

