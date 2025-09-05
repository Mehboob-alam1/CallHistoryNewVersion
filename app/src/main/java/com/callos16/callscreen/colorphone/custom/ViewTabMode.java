package com.callos16.callscreen.colorphone.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.callos16.callscreen.colorphone.R;


public class ViewTabMode extends LinearLayout {
    private TabResult tabResult;
    private LayoutItemTab vCon;
    private LayoutItemTab vFar;
    private LayoutItemTab vPad;
    private LayoutItemTab vRec;
    private LayoutItemTab vSet;


    public interface TabResult {
        void onTapClick(int i);
    }

    public void setTabResult(TabResult tabResult) {
        this.tabResult = tabResult;
    }

    public ViewTabMode(Context context) {
        super(context);
        init();
    }

    public ViewTabMode(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ViewTabMode(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutItemTab layoutItemTab = new LayoutItemTab(getContext());
        this.vFar = layoutItemTab;
        layoutItemTab.setOnClickListener(new OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                ViewTabMode.this.onTabClick(view);
            }
        });
        this.vFar.setData(R.drawable.im_tab_fav, R.string.favorites);
        addView(this.vFar, new LayoutParams(0, -2, 1.0f));
        LayoutItemTab layoutItemTab2 = new LayoutItemTab(getContext());
        this.vRec = layoutItemTab2;
        layoutItemTab2.setOnClickListener(new OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                ViewTabMode.this.onTabClick(view);
            }
        });
        this.vRec.setData(R.drawable.im_tab_rec, R.string.recents);
        addView(this.vRec, new LayoutParams(0, -2, 1.0f));
        LayoutItemTab layoutItemTab3 = new LayoutItemTab(getContext());
        this.vCon = layoutItemTab3;
        layoutItemTab3.setOnClickListener(new OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                ViewTabMode.this.onTabClick(view);
            }
        });
        this.vCon.setData(R.drawable.im_tab_contact, R.string.contacts);
        addView(this.vCon, new LayoutParams(0, -2, 1.0f));
        LayoutItemTab layoutItemTab4 = new LayoutItemTab(getContext());
        this.vPad = layoutItemTab4;
        layoutItemTab4.setOnClickListener(new OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                ViewTabMode.this.onTabClick(view);
            }
        });
        this.vPad.setData(R.drawable.im_tab_keypab, R.string.keypad);
        addView(this.vPad, new LayoutParams(0, -2, 1.0f));
        LayoutItemTab layoutItemTab5 = new LayoutItemTab(getContext());
        this.vSet = layoutItemTab5;
        layoutItemTab5.setOnClickListener(new OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                ViewTabMode.this.onTabClick(view);
            }
        });
        this.vSet.setData(R.drawable.im_tab_setting, R.string.setting);
        addView(this.vSet, new LayoutParams(0, -2, 1.0f));
    }


    public void onTabClick(View view) {
        LayoutItemTab layoutItemTab = this.vFar;
        layoutItemTab.setChoose(view == layoutItemTab);
        LayoutItemTab layoutItemTab2 = this.vRec;
        layoutItemTab2.setChoose(view == layoutItemTab2);
        LayoutItemTab layoutItemTab3 = this.vCon;
        layoutItemTab3.setChoose(view == layoutItemTab3);
        LayoutItemTab layoutItemTab4 = this.vPad;
        layoutItemTab4.setChoose(view == layoutItemTab4);
        LayoutItemTab layoutItemTab5 = this.vSet;
        layoutItemTab5.setChoose(view == layoutItemTab5);
        TabResult tabResult = this.tabResult;
        if (tabResult != null) {
            if (view == this.vFar) {
                tabResult.onTapClick(0);
            } else if (view == this.vRec) {
                tabResult.onTapClick(1);
            } else if (view == this.vCon) {
                tabResult.onTapClick(2);
            } else if (view == this.vPad) {
                tabResult.onTapClick(3);
            } else {
                tabResult.onTapClick(4);
            }
        }
    }

    public void setTabDefault(int i) {
        if (i == 0) {
            this.vFar.setChoose(true);
        } else if (i == 1) {
            this.vRec.setChoose(true);
        } else if (i == 2) {
            this.vCon.setChoose(true);
        } else if (i == 3) {
            this.vPad.setChoose(true);
        } else {
            this.vSet.setChoose(true);
        }
        TabResult tabResult = this.tabResult;
        if (tabResult != null) {
            tabResult.onTapClick(i);
        }
    }
}
