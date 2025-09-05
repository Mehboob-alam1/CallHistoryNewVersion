package com.callos16.callscreen.colorphone.dialog;

import android.content.Context;
import android.os.Bundle;

import com.callos16.callscreen.colorphone.R;
import com.callos16.callscreen.colorphone.custom.LayoutListSim;
import com.callos16.callscreen.colorphone.item.ItemSimInfo;


import java.util.ArrayList;


public class DialogShowSim extends BaseDialog {
    private final ArrayList<ItemSimInfo> arrSim;
    private final int marTop;
    private final LayoutListSim.SimItemClick simItemClick;

    public DialogShowSim(Context context, ArrayList<ItemSimInfo> arrayList, int i, LayoutListSim.SimItemClick simItemClick) {
        super(context);
        this.arrSim = arrayList;
        this.simItemClick = simItemClick;
        this.marTop = i;
        setCancelable(true);
    }

    @Override 
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_choose_sim);
        LayoutListSim layoutListSim = (LayoutListSim) findViewById(R.id.ll_sim);
        layoutListSim.setSimItemClick(new LayoutListSim.SimItemClick() { 
            @Override 
            public final void onSimClick(int i) {
                DialogShowSim.this.m106xfdce9568(i);
            }
        });
        layoutListSim.setSimInfo(this.arrSim);
        layoutListSim.showView(this.marTop);
    }

    
    
    public  void m106xfdce9568(int i) {
        cancel();
        if (i != -1) {
            this.simItemClick.onSimClick(i);
        }
    }
}
