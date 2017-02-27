package net.yrom.screenrecorder.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.yrom.screenrecorder.R;
import net.yrom.screenrecorder.model.DanmakuBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * author : raomengyang on 2016/12/29.
 */

public class ScreenFloatingWindow extends FrameLayout implements IScreenRecordFloatingWIndow, View.OnClickListener {

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    private View floatingWindowView;

    private ImageView danmakuView;

    private ImageView micView;

    private ImageView changeButtonView;

    private ImageView floatingSwichView;

    private LinearLayout floatingMenuLL;

    private LinearLayout floatingDanmakuLL;

    private RelativeLayout rlMenu;

    private DanmakuListView danmakuLV;

    private OnFloatingWindowItemClickListener itemClickListener;

    private DanmakuListAdapter listAdapter;

    private boolean isDanmakuOn = true;
    private boolean isMicOn = true;
    private boolean isDanmakuVisible = true;

    public ScreenFloatingWindow(Context context) {
        super(context);
        initView(context);
        initData(context);
    }

    private void initData(Context context) {
        listAdapter = new DanmakuListAdapter();
        danmakuLV.setAdapter(listAdapter);
    }

    private void initView(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_floating_window, this);
        floatingMenuLL = (LinearLayout) findViewById(R.id.ll_floating_window_menu);
        rlMenu = (RelativeLayout) findViewById(R.id.rl_menu);
        floatingDanmakuLL = (LinearLayout) findViewById(R.id.ll_floating_window_danmaku);
        floatingWindowView = findViewById(R.id.layout_floating_window);
        danmakuView = (ImageView) findViewById(R.id.iv_danmaku);
        micView = (ImageView) findViewById(R.id.iv_mic);
        changeButtonView = (ImageView) findViewById(R.id.iv_other);
        floatingSwichView = (ImageView) findViewById(R.id.iv_close);
        danmakuLV = (DanmakuListView) findViewById(R.id.lv_danmaku);
        viewWidth = floatingWindowView.getLayoutParams().width;
        viewHeight = floatingWindowView.getLayoutParams().height;

        danmakuView.setOnClickListener(this);
        micView.setOnClickListener(this);
        changeButtonView.setOnClickListener(this);
        floatingSwichView.setOnClickListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();

                xDownInScreen = event.getRawX();
                xDownInScreen = event.getRawY() - getStatusBarHeight();

                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    @Override
    public void showFloatingWindow(boolean visible) {
        setViewVisible(floatingWindowView, visible);
    }

    @Override
    public void showDanmaku(boolean visible) {
        if (danmakuView != null && floatingDanmakuLL != null) {
            danmakuView.setImageResource(visible ? R.drawable.ic_float_danmaku_open : R.drawable.ic_float_danmaku_close);
            if (visible) {
                floatingDanmakuLL.setVisibility(VISIBLE);
            } else {
                floatingDanmakuLL.setVisibility(GONE);
            }
        }
    }

    @Override
    public void openMic(boolean on) {
        if (micView != null) {
            micView.setImageResource(on ? R.drawable.ic_float_mic_open : R.drawable.ic_float_mic_close);
        }
    }

    @Override
    public void showMenu(boolean visible) {
        if (floatingMenuLL != null && floatingDanmakuLL != null) {
            if (visible) {
                floatingDanmakuLL.setVisibility(VISIBLE);
            } else {
                floatingDanmakuLL.setVisibility(GONE);
            }
        }
    }

    private void setViewVisible(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(view);
        }
        switch (view.getId()) {
            case R.id.iv_mic:
                isMicOn = !isMicOn;
                openMic(isMicOn);
                break;
            case R.id.iv_other:
                break;
            case R.id.iv_danmaku:
                isDanmakuOn = !isDanmakuOn;
                showDanmaku(isDanmakuOn);
                isDanmakuVisible = !isDanmakuVisible;
                showMenu(isDanmakuVisible);
                break;
            default:
                break;
        }
    }

    public interface OnFloatingWindowItemClickListener {
        void onItemClick(View view);
    }

    public void setItemClickListener(OnFloatingWindowItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private static class DanmakuListAdapter extends BaseAdapter {

        private List<DanmakuBean> danmakuList = new ArrayList<>();

        private void setDataToAdapter(List<DanmakuBean> danmakuList) {
            if (danmakuList == null) return;
            if (danmakuList.size() <= 0) return;
            this.danmakuList.addAll(danmakuList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return danmakuList != null ? danmakuList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return danmakuList != null ? danmakuList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danmaku, null);
                holder = new ViewHolder(convertView);
                holder.usernameTV = (TextView) convertView.findViewById(R.id.tv_username);
                holder.messageTV = (TextView) convertView.findViewById(R.id.tv_danmaku_message);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (danmakuList != null && danmakuList.size() > 0) {
                holder.usernameTV.setText(danmakuList.get(position).getName());
                holder.messageTV.setText(danmakuList.get(position).getMessage());
            }
            return convertView;
        }

        static class ViewHolder {
            private TextView usernameTV;
            private TextView messageTV;

            private ViewHolder(View view) {
                // BindView(view);
            }
        }
    }

    public void setDataToList(final List<DanmakuBean> danmakuList) {
        if (listAdapter != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listAdapter.setDataToAdapter(danmakuList);
                }
            });
        }
    }

}
