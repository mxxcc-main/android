package com.qy.ccm.view.base;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.qy.ccm.utils.function.WidgetUtils;


public abstract class BaseLinearLayout extends LinearLayout {

    public Context context;
    public LayoutInflater inflater;
    protected WidgetUtils dialogUtils;


    public BaseLinearLayout(Context context) {
        super(context);
        init(context);
        initView(inflater);
    }


    public BaseLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initView(inflater);
        setAttrs(attrs);
    }

    public BaseLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initView(inflater);
        setAttrs(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        initView(inflater);
        setAttrs(attrs);
    }

    public void init(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        dialogUtils = new WidgetUtils(context);

    }


    public abstract void setAttrs(AttributeSet attrs);

    public abstract void initView(LayoutInflater inflater);

}
