package com.easyranktools.easyranktools.custom;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import com.easyranktools.easyranktools.utils.OtherUtils;


public class ViewItemThemeNull extends RelativeLayout {
    public ViewItemThemeNull(Context context) {
        super(context);
        addView(new View(context), -1, (OtherUtils.getWidthScreen(context) * 15) / 100);
    }
}
