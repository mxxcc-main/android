package com.qy.ccm.view.group;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qy.ccm.R;
import com.qy.ccm.view.base.BaseLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自定义导航栏
 * 透明
 */
public class NavigationLucencyLayout extends BaseLinearLayout {
    private OnBuyListener listener;
    private OnBuyOneListener listenerOne;
    private OnBuyTwoListener listenerTwo;
    private OnBuyThreeListener listenerThree;

    @BindView(R.id.iv_arrows_lift)
    public ImageView ivArrowsLift;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.iv_bar_one)
    ImageView ivBarOne;
    @BindView(R.id.iv_bar_two)
    ImageView ivBarTwo;
    @BindView(R.id.ll_buyview)
    RelativeLayout llBuyview;
    @BindView(R.id.tv_bar_three)
    TextView tvBarThree;


    @OnClick({R.id.iv_arrows_lift, R.id.iv_bar_one, R.id.iv_bar_two, R.id.tv_bar_three})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_arrows_lift:
                if (listener != null) {
                    listener.OnClickListener();
                } else {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
                break;
            case R.id.iv_bar_one:
                if (listenerOne != null) {
                    listenerOne.OnClickListener();
                }
                break;
            case R.id.iv_bar_two:
                if (listenerTwo != null) {
                    listenerTwo.OnClickListener();
                }
                break;
            case R.id.tv_bar_three:
                if (listenerThree != null) {
                    listenerTwo.OnClickListener();
                }
                break;

            default:
                break;
        }
    }

    public NavigationLucencyLayout(Context context) {
        super(context);
    }

    public NavigationLucencyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationLucencyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NavigationLucencyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setAttrs(AttributeSet attrs) {

    }

    @Override
    public void initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_navigation_luncency, this);
        ButterKnife.bind(view);

    }

    public interface OnBuyListener {
        void OnClickListener();
    }

    public void setOnBuyListener(OnBuyListener listener) {
        this.listener = listener;
    }

    public interface OnBuyOneListener {
        void OnClickListener();
    }

    public void setOnBuyListener(OnBuyOneListener listener) {
        this.listenerOne = listener;
    }

    public interface OnBuyTwoListener {
        void OnClickListener();
    }

    public void setOnBuyListener(OnBuyTwoListener listener) {
        this.listenerTwo = listener;
    }

    public interface OnBuyThreeListener {
        void OnClickListener();
    }

    public void setOnBuyListener(OnBuyThreeListener listener) {
        this.listenerThree = listener;
    }

    public void setBackArrowsVisibility(int visibility) {
        ivArrowsLift.setVisibility(visibility);
    }

    public void setColor(int color) {
        tvBarTitle.setTextColor(getResources().getColor(color));
        tvBarThree.setTextColor(getResources().getColor(color));
        ivArrowsLift.setColorFilter(getResources().getColor(color));
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tvBarTitle.setText(title);
        tvBarTitle.setVisibility(VISIBLE);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        tvBarTitle.setTextColor(color);
    }

    /**
     * 设置右边文本
     *
     * @param text
     */
    public void setTextThree(String text) {
        tvBarThree.setVisibility(VISIBLE);
        tvBarThree.setText(text);
    }

    public void setIvLift(int drawable) {
        ivArrowsLift.setVisibility(VISIBLE);
        ivArrowsLift.setImageResource(drawable);
    }

    public void setIvOne(int drawable) {
        ivBarOne.setVisibility(VISIBLE);
        ivBarOne.setImageResource(drawable);
    }

    public void setIvTwo(int drawable) {
        ivBarTwo.setVisibility(VISIBLE);
        ivBarTwo.setVisibility(VISIBLE);
        ivBarTwo.setImageResource(drawable);
    }

    public void setBackground(int color) {
        llBuyview.setBackgroundResource(color);
    }


}
