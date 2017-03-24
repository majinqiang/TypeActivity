package com.laoma.baseloadpage;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by majinqiang on 3/24/2017.
 */

public abstract class LoadingPager extends FrameLayout {

    /**
     * //页面显示分析
     * //Fragment共性-->页面共性-->视图的展示
     * /**
     * 任何应用其实就只有4种页面类型
     * ① 加载页面
     * ② 错误页面
     * ③ 空页面
     * ④ 成功页面
     * <p/>
     * ①②③三种页面一个应用基本是固定的
     * 每一个fragment/activity对应的页面④就不一样
     * 进入应用的时候显示①,②③④需要加载数据之后才知道显示哪个
     */

    public static final int STATE_NONE = -1;// 默认状态
    public static final int STATE_LODING = 0;//正在请求网络
    public static final int STATE_EMPTY = 1;//空状态
    public static final int STATE_ERROR = 2;//错误状态
    public static final int STATE_SUCCESS = 3;// 成功状态

    public int mCurState = STATE_NONE;//当前默认状态
    private View mLoadingView;//加载中视图
    private View mErrorView;//加载错误视图
    private View mEmptyView;//空视图
    private View mSuccessView;//加载成功视图

    Context context;
    public LoadingPager(Context context) {
        super(context);
        this.context = context;

        initCommonView();
    }

    /**
     * 初始化常规视图
     *
     * @call LoadingPager初始化的时候
     * @des 这初始化的时候不创建successview是因为根据不同的情况，成功界面不一样
     */
    private void initCommonView() {
        // ① 加载页面
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        this.addView(mLoadingView);
        // ② 错误页面
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮重新请求加载数据
                loadData();
            }
        });
        this.addView(mErrorView);
        // ③ 空页面
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        this.addView(mEmptyView);

        refreshUI(); //第一次调用refreshUI();
    }

    /**
     * 刷新状态<br>
     * <p/>
     * 根据当前状态显示不同的view<br>
     * 、①：.LoadingPager初始化的时候的时候调用(initCommonView()的时候调用)<br>
     * ②：显示正在加载<br>
     * ③：正在加载数据执行完成的时候<br>
     */
    private void refreshUI() {
        //控制loading视图的显示
        mLoadingView.setVisibility((mCurState == STATE_LODING) || (mCurState == STATE_NONE) ? View.VISIBLE : View.GONE);
        //控制errprview视图的显示
        mErrorView.setVisibility(mCurState == STATE_ERROR ? View.VISIBLE : View.GONE);
        //控制emptyView视图的显示
        mEmptyView.setVisibility(mCurState == STATE_EMPTY ? View.VISIBLE : View.GONE);
        //控制mSuccessView 视图的显示
        if (mSuccessView == null && mCurState == STATE_SUCCESS) {
            //创建成功视图
            mSuccessView = initSuccess();
            this.addView(mSuccessView);
        }
        if (mSuccessView != null) {
            //控制mSuccessView的显示隐藏
            mSuccessView.setVisibility(mCurState == STATE_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }


/**
 * 数据加载的流程
 * ① 触发加载， 进入页面开始加载/点击某个按钮的时候开始加载
 * ② 异步加载数据 -->显示加载视图
 * ③ 处理加载结果
 * ③-1、成功，显示成功视图
 * ③-2、失败
 * ③-2-① 数据为空，显示空视图
 * ③-2-②数据加载失败，显示加载失败视图
 */

    /**
     * 这个得主动调用触发加载数据<br>
     * 暴露给外接调用, 其实就是外界触发加载数据
     */
    public void loadData() {
        //这里有个小bug，第二次执行的时候，界面显示由上一次的状态决定。这里需要重置状态
        //为了保证每次执行的时候都是加载中视图,而不是上一次的mCurState,也解决加载成功界面每次都重新加载的问题
        if (mCurState != STATE_SUCCESS && mCurState != STATE_LODING) {
            int state = STATE_LODING;
            mCurState = state;
            refreshUI();//第二次调用refreshUI();
            //使用线程池异步加载数据
            ThreadPoolFactory.getNormalPool().execute(new LoadDataTask());
        }
    }

    class LoadDataTask implements Runnable {

        @Override
        public void run() {
            //异步加载数据,通过加载的数据返回一个状态值
            LoadResult tempState = initData();
            //处理加载结果
            mCurState = tempState.getState();
            //刷新UI，这里需要注意的是更新Ui需要在Ui线程。所以写了个方法

            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    refreshUI();//第三次调用refreshUI()
                }
            });
        }
    }

    /**
     * 真正加载数据，必须实现，直接抽象让子类实现<br>
     * loadData()调用的时候被调用
     *
     * @return 加载状态返回
     */
    public abstract LoadResult initData();

    /**
     * 创建成功视图，交给具体子类完成<br>
     * 返回成功视图，正在加载数据完成之后，并且数据加载成功，我们必须告知具体的成功制图
     *
     * @return
     */
    protected abstract View initSuccess();


    /**
     * 使用枚举是，是为了不让随意传值，限定值
     */
    public enum LoadResult {

        SUCCESS(STATE_SUCCESS), ERROR(STATE_ERROR), EMPTY(STATE_EMPTY);
        int state;

        public int getState() {
            return state;
        }

        private LoadResult(int state) {
            this.state = state;
        }
    }
}
