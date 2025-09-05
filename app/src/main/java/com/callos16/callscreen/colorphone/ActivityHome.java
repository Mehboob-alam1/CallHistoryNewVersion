package com.callos16.callscreen.colorphone;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.callos16.callscreen.colorphone.custom.LayoutChooseContact;
import com.callos16.callscreen.colorphone.custom.ViewTabMode;
import com.callos16.callscreen.colorphone.dialog.DialogRate;
import com.callos16.callscreen.colorphone.dialog.DialogResult;
import com.callos16.callscreen.colorphone.dialog.FavResult;
import com.callos16.callscreen.colorphone.fragment.ContactResult;
import com.callos16.callscreen.colorphone.fragment.FragmentContact;
import com.callos16.callscreen.colorphone.fragment.FragmentFavorites;
import com.callos16.callscreen.colorphone.fragment.FragmentInfo;
import com.callos16.callscreen.colorphone.fragment.FragmentPad;
import com.callos16.callscreen.colorphone.fragment.FragmentRecents;
import com.callos16.callscreen.colorphone.fragment.FragmentSetting;
import com.callos16.callscreen.colorphone.item.ItemContact;
import com.callos16.callscreen.colorphone.item.ItemRecentGroup;
import com.callos16.callscreen.colorphone.utils.MyShare;
import com.callos16.callscreen.colorphone.utils.OtherUtils;
import com.callos16.callscreen.colorphone.utils.ReadContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class ActivityHome extends AppCompatActivity {
    private ArrayList<ItemContact> arrAllContact;
    private final ContactResult contactResult = new AnonymousClass1();
    private FragmentContact fragmentContact;
    private FragmentFavorites fragmentFavorites;
    private FragmentPad fragmentPad;
    private FragmentRecents fragmentRecents;
    private FragmentSetting fragmentSetting;
    private LayoutChooseContact layoutChooseContact;
    private int layoutPos;
    private LinearLayout llFragment;
    private String number;
    private int pos;
    private RelativeLayout rlMain;
    private boolean showAdsFist;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        getDataCall();
        initContact();
        initView();


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.FullscreenAd( this);

    }

    private void getDataCall() {
        String action;
        this.layoutPos = MyShare.getLayout(this);
        Intent intent = getIntent();
        if (intent == null || (action = intent.getAction()) == null) {
            return;
        }
        if ((action.equals("android.intent.action.DIAL") || action.equals("android.intent.action.VIEW")) && intent.getData() != null && intent.getDataString() != null && intent.getDataString().contains("tel:")) {
            String dataString = intent.getDataString();
            this.number = Uri.decode(dataString).substring(dataString.indexOf("tel:") + 4);
            this.layoutPos = 3;
        }
    }

    private void initContact() {
        this.arrAllContact = new ArrayList<>();
        final Handler handler = new Handler(new Handler.Callback() { 
            @Override 
            public final boolean handleMessage(Message message) {
                return ActivityHome.this.m44xb144c5a4(message);
            }
        });
        new Thread(new Runnable() { 
            @Override 
            public final void run() {
                ActivityHome.this.m45xb0ce5fa5(handler);
            }
        }).start();
    }

    
    
    public  boolean m44xb144c5a4(Message message) {
        FragmentContact fragmentContact = this.fragmentContact;
        if (fragmentContact != null) {
            fragmentContact.updateList();
        }
        FragmentFavorites fragmentFavorites = this.fragmentFavorites;
        if (fragmentFavorites != null) {
            fragmentFavorites.updateList();
            return true;
        }
        return true;
    }

    
    
    public  void m45xb0ce5fa5(Handler handler) {
        this.arrAllContact.addAll(ReadContact.getAllContact(this));
        handler.sendEmptyMessage(1);
    }

    private void initView() {
        rlMain = findViewById(R.id.ll_main);
        int widthScreen = OtherUtils.getWidthScreen(this);
        Log.e("rlMain", "" + rlMain);
        rlMain.setBackgroundColor(-16777216);
        this.llFragment = (LinearLayout) findViewById(R.id.ll_fragment);
        boolean theme = MyShare.getTheme(this);
        getWindow().setStatusBarColor(0);

        if (theme) {
            getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
            getWindow().getDecorView().setSystemUiVisibility(Build.VERSION.SDK_INT >= 26 ? 9232 : 9216);
            this.llFragment.setBackground(OtherUtils.bgMain(-1, widthScreen / 50.0f));
        } else {
            getWindow().setNavigationBarColor(Color.parseColor("#2C2C2C"));
            getWindow().getDecorView().setSystemUiVisibility(1024);
            this.llFragment.setBackground(OtherUtils.bgMain(Color.parseColor("#2C2C2C"), widthScreen / 50.0f));
        }
        ViewTabMode viewTabMode = (ViewTabMode) findViewById(R.id.v_tab);
        viewTabMode.setTabResult(new ViewTabMode.TabResult() { 
            @Override 
            public final void onTapClick(int i) {
                ActivityHome.this.onTabClick(i);
            }
        });
        viewTabMode.setTabDefault(this.layoutPos);
        this.showAdsFist = true;
    }

    
    public void onTabClick(int i) {
        if (this.pos == i) {
            showTab();
            return;
        }
        this.pos = i;
        if (this.showAdsFist) {
            this.showAdsFist = false;
            ActivityHome.this.showTab();

            return;
        }
        showTab();
    }

    
    public void showTab() {
        MyShare.putLayout(this, this.pos);
        int i = this.pos;
        if (i == 0) {
            if (this.fragmentFavorites == null) {
                FragmentFavorites fragmentFavorites = new FragmentFavorites();
                this.fragmentFavorites = fragmentFavorites;
                fragmentFavorites.setContactResult(this.contactResult);
            }
            showFragment(this.fragmentFavorites, false);
        } else if (i == 1) {
            if (this.fragmentRecents == null) {
                FragmentRecents fragmentRecents = new FragmentRecents();
                this.fragmentRecents = fragmentRecents;
                fragmentRecents.setContactResult(this.contactResult);
            }
            showFragment(this.fragmentRecents, false);
        } else if (i == 2) {
            if (this.fragmentContact == null) {
                FragmentContact fragmentContact = new FragmentContact();
                this.fragmentContact = fragmentContact;
                fragmentContact.setContactResult(this.contactResult);
            }
            showFragment(this.fragmentContact, false);
        } else if (i == 3) {
            if (this.fragmentPad == null) {
                FragmentPad fragmentPad = new FragmentPad();
                this.fragmentPad = fragmentPad;
                fragmentPad.setContactResult(this.contactResult);
            }
            showFragment(this.fragmentPad, false);
        } else {
            if (this.fragmentSetting == null) {
                this.fragmentSetting = new FragmentSetting();
            }
            showFragment(this.fragmentSetting, false);
        }
    }

    public void showFragment(Fragment fragment, boolean z) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.setReorderingAllowed(true);
        if (z) {
            beginTransaction.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_pop_in, R.anim.anim_fragment_pop_out);
            beginTransaction.addToBackStack(fragment.getTag());
        } else if (!getSupportFragmentManager().isStateSaved()) {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
        }
        beginTransaction.replace(R.id.frame, fragment);
        try {
            beginTransaction.commit();
        } catch (IllegalStateException unused) {
            Toast.makeText(this, (int) R.string.error, Toast.LENGTH_SHORT).show();
        }
    }

    public void onChangeTheme() {
        startActivity(new Intent(this, ActivityApplyTheme.class));
        finish();
    }

    public void addNewContact(ItemContact itemContact) {
        this.arrAllContact.add(itemContact);

        Collections.sort(this.arrAllContact, new Comparator<ItemContact>() {
            @Override
            public int compare(ItemContact o1, ItemContact o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());

            }
        });
    }

    public void updateContact(ItemContact itemContact) {
        Iterator<ItemContact> it = this.arrAllContact.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ItemContact next = it.next();
            if (next.getId().equals(itemContact.getId())) {
                this.arrAllContact.remove(next);
                break;
            }
        }
        addNewContact(itemContact);
    }

    public void removeContact(String str) {
        Iterator<ItemContact> it = this.arrAllContact.iterator();
        while (it.hasNext()) {
            ItemContact next = it.next();
            if (next.getId().equals(str)) {
                this.arrAllContact.remove(next);
                return;
            }
        }
    }

    public ArrayList<ItemContact> getArrAllContact() {
        return this.arrAllContact;
    }

    public String getNumber() {
        return this.number;
    }

    public void showLayoutContact(FavResult favResult) {
        if (this.arrAllContact.size() == 0) {
            Toast.makeText(this, (int) R.string.empty_contact, Toast.LENGTH_SHORT).show();
            return;
        }
        if (this.layoutChooseContact == null) {
            this.layoutChooseContact = new LayoutChooseContact(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.setMargins(0, (OtherUtils.getWidthScreen(this) * 13) / 100, 0, 0);
            this.rlMain.addView(this.layoutChooseContact, layoutParams);
        }
        this.layoutChooseContact.setFavResult(favResult);
        this.layoutChooseContact.show(this.llFragment, this.arrAllContact);
    }

    
    
    
    public class AnonymousClass1 implements ContactResult {
        AnonymousClass1() {
        }

        @Override 
        public void onContactChange() {
            if (ActivityHome.this.fragmentContact != null) {
                ActivityHome.this.fragmentContact.updateList();
            }
            if (ActivityHome.this.fragmentPad != null) {
                ActivityHome.this.fragmentPad.checkNum();
            }
        }

        @Override 
        public void onFavoritesChange() {
            if (ActivityHome.this.fragmentFavorites != null) {
                ActivityHome.this.fragmentFavorites.updateList();
            }
        }

        @Override 
        public void onAddNewContact(final ItemRecentGroup itemRecentGroup, final ItemContact itemContact) {
            onContactChange();
            new Handler().postDelayed(new Runnable() { 
                @Override 
                public final void run() {
                    AnonymousClass1.this.m46x22192003(itemContact, itemRecentGroup);
                }
            }, 1000L);
        }

        
        
        public  void m46x22192003(ItemContact itemContact, ItemRecentGroup itemRecentGroup) {
            FragmentInfo newInstance = FragmentInfo.newInstance(itemContact, itemRecentGroup, R.string.recents);
            newInstance.setContactResult(ActivityHome.this.contactResult);
            ActivityHome.this.showFragment(newInstance, true);
        }

        @Override 
        public void onBack() {
            ActivityHome.this.onBackPressed();
        }
    }

    @Override 
    public void onBackPressed() {
        LayoutChooseContact layoutChooseContact = this.layoutChooseContact;
        if (layoutChooseContact != null && layoutChooseContact.getVisibility() == View.VISIBLE) {
            this.layoutChooseContact.hide();
        } else {
            super.onBackPressed();
        }
    }

    
    @Override 
    public void onResume() {
        super.onResume();

        if (OtherUtils.checkPer(this) || !OtherUtils.checkPermission(this)) {
            startActivity(new Intent(this, ActivityRequestPermission.class));
            finish();
        }
    }

    
    @Override 
    public void onPause() {
        super.onPause();

    }

    @Override
    
    protected void onDestroy() {

        super.onDestroy();
    }


}
