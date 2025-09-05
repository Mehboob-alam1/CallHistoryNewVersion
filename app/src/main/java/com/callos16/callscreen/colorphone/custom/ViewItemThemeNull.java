package com.callos16.callscreen.colorphone.custom;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import com.callos16.callscreen.colorphone.utils.OtherUtils;


public class ViewItemThemeNull extends RelativeLayout {
    public ViewItemThemeNull(Context context) {
        super(context);
        addView(new View(context), -1, (OtherUtils.getWidthScreen(context) * 15) / 100);
    }
}
