package com.easyranktools.easyranktools.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.telecom.PhoneAccountHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyranktools.callhistoryforanynumber.R;
import com.easyranktools.easyranktools.ActivityHome;
import com.easyranktools.easyranktools.adapter.AdapterContactHome;
import com.easyranktools.easyranktools.custom.TextW;
import com.easyranktools.easyranktools.custom.ViewScrollAlphaB;
import com.easyranktools.easyranktools.custom.ViewSearch;
import com.easyranktools.easyranktools.dialog.DialogAddFav;
import com.easyranktools.easyranktools.dialog.FavResult;
import com.easyranktools.easyranktools.item.ItemContact;
import com.easyranktools.easyranktools.item.ItemFavorites;
import com.easyranktools.easyranktools.item.ItemSimInfo;
import com.easyranktools.easyranktools.utils.ActionUtils;
import com.easyranktools.easyranktools.utils.MyShare;
import com.easyranktools.easyranktools.utils.OtherUtils;
import com.easyranktools.easyranktools.utils.ReadContact;
import com.easyranktools.easyranktools.utils.SimUtils;
import com.easyranktools.easyranktools.ads.AdManager;
import com.easyranktools.easyranktools.ads.NativeAdListManager;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.ArrayList;


public class FragmentContact extends BaseFragment {
    private ViewContact viewContact;

    @Override 
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getArrContact();
        if (this.viewContact == null) {
            this.viewContact = new ViewContact(layoutInflater.getContext());
        }
        return this.viewContact;
    }

    @Override 
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (getContext() == null || i2 != -1 || intent == null || intent.getData() == null) {
            return;
        }
        Cursor query = getContext().getContentResolver().query(intent.getData(), null, null, null, null);
        if (query != null && query.moveToFirst()) {
            int columnIndex = query.getColumnIndex("contact_id");
            if (columnIndex < 0) {
                columnIndex = query.getColumnIndex("_id");
            }
            ItemContact contact = ReadContact.getContact(getContext(), query.getString(columnIndex));
            if (contact != null) {
                if (getActivity() instanceof ActivityHome) {
                    ((ActivityHome) getActivity()).addNewContact(contact);
                }
                if (this.contactResult != null) {
                    this.contactResult.onContactChange();
                }
            }
        }
        if (query != null) {
            query.close();
        }
    }

    public void updateList() {
        ViewContact viewContact = this.viewContact;
        if (viewContact != null) {
            viewContact.loadContactDone();
        }
    }

    
    
    public class ViewContact extends RelativeLayout implements AdapterContactHome.ContactResult {
        private final AdapterContactHome adapterContactHome;
        private final boolean theme;
        private final TextW tvEmpty;
        private final ViewScrollAlphaB viewScrollAlphaB;
        
        // Ad Managers
        private AdManager adManager;
        private NativeAdListManager nativeAdListManager;
        private View nativeAdView;
        private NativeAd nativeAd;

        public ViewContact(Context context) {
            super(context);
            int widthScreen = OtherUtils.getWidthScreen(context);
            int i = widthScreen / 25;
            boolean theme = MyShare.getTheme(context);
            this.theme = theme;
            
            // Initialize ads
            initAds(context);
            ImageView imageView = new ImageView(context);
            imageView.setId(1111);
            imageView.setImageResource(R.drawable.ic_add_contact);
            imageView.setPadding(i, i, i, i);
            imageView.setOnClickListener(new OnClickListener() { 
                @Override 
                public final void onClick(View view) {
                    ViewContact.this.m107x8d827b30(view);
                }
            });
            int i2 = (int) (i * 3.2f);
            LayoutParams layoutParams = new LayoutParams(i2, i2);
            layoutParams.addRule(21);
            layoutParams.setMargins(0, MyShare.getSizeNotification(context), 0, 0);
            addView(imageView, layoutParams);
            TextW textW = new TextW(context);
            textW.setupText(600, 4.3f);
            textW.setText(R.string.contacts);
            textW.setGravity(17);
            LayoutParams layoutParams2 = new LayoutParams(-1, -1);
            layoutParams2.addRule(6, imageView.getId());
            layoutParams2.addRule(8, imageView.getId());
            addView(textW, layoutParams2);
            ViewSearch viewSearch = new ViewSearch(context);
            viewSearch.setId(1112);
            viewSearch.setListenerTextChange(new ViewSearch.TextResult() { 
                @Override 
                public void onTextChange(String str) {
                    ViewContact.this.adapterContactHome.filter(str);
                }
            });
            LayoutParams layoutParams3 = new LayoutParams(-1, -2);
            layoutParams3.setMargins(i, 0, i, i);
            layoutParams3.addRule(3, imageView.getId());
            addView(viewSearch, layoutParams3);
            RelativeLayout relativeLayout = new RelativeLayout(context);
            LayoutParams layoutParams4 = new LayoutParams(-1, -1);
            layoutParams4.addRule(3, viewSearch.getId());
            addView(relativeLayout, layoutParams4);
            TextW textW2 = new TextW(context);
            this.tvEmpty = textW2;
            textW2.setupText(400, 3.8f);
            textW2.setText(R.string.empty_contact);
            textW2.setVisibility(View.GONE);
            textW2.setGravity(1);
            textW2.setTextColor(Color.parseColor("#aaaaaa"));
            LayoutParams layoutParams5 = new LayoutParams(-2, -2);
            layoutParams5.setMargins(i, 0, i, 0);
            layoutParams5.addRule(13);
            relativeLayout.addView(textW2, layoutParams5);
            final RecyclerView recyclerView = new RecyclerView(context);
            AdapterContactHome adapterContactHome = new AdapterContactHome(FragmentContact.this.arrAllContact, theme, this);
            this.adapterContactHome = adapterContactHome;
            recyclerView.setAdapter(adapterContactHome);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            LayoutParams layoutParams6 = new LayoutParams(-1, -1);
            layoutParams6.addRule(3, imageView.getId());
            relativeLayout.addView(recyclerView, layoutParams6);
            ViewScrollAlphaB viewScrollAlphaB = new ViewScrollAlphaB(context);
            this.viewScrollAlphaB = viewScrollAlphaB;
            viewScrollAlphaB.setAlphaBResult(new ViewScrollAlphaB.AlphaBResult() { 
                @Override 
                public final void onAlphaBResult(String str) {
                    ViewContact.this.m108x548e6231(recyclerView, str);
                }
            });
            LayoutParams layoutParams7 = new LayoutParams((widthScreen * 8) / 100, viewScrollAlphaB.makeHeightView());
            layoutParams7.addRule(15);
            layoutParams7.addRule(21);
            relativeLayout.addView(viewScrollAlphaB, layoutParams7);
            if (theme) {
                textW.setTextColor(-16777216);
                setBackgroundColor(-1);
            } else {
                textW.setTextColor(-1);
                setBackgroundColor(Color.parseColor("#2C2C2C"));
            }
            onCheckList();
        }

        
        
        public  void m107x8d827b30(View view) {
            ActionUtils.onNewContact(FragmentContact.this, "");
        }

        
        
        public  void m108x548e6231(RecyclerView recyclerView, String str) {
            int locationAlphaB = this.adapterContactHome.getLocationAlphaB(str);
            if (locationAlphaB != -1) {
                recyclerView.scrollToPosition(locationAlphaB);
            }
        }

        private void initAds(Context context) {
            // Initialize AdMob
            adManager = AdManager.getInstance(context);
            adManager.initialize();
            
            // Setup Native Ad List Manager
            nativeAdListManager = new NativeAdListManager(context);
            nativeAdListManager.setListener(new NativeAdListManager.NativeAdListener() {
                @Override
                public void onAdLoaded(NativeAd nativeAd, View adView) {
                    post(() -> {
                        addNativeAdToList(adView, nativeAd);
                    });
                }
                
                @Override
                public void onAdFailedToLoad() {
                    // Ad failed to load, continue without ad
                }
            });
            
            // Load native ad
            nativeAdListManager.loadNativeAd();
        }
        
        private void addNativeAdToList(View adView, NativeAd nativeAd) {
            Log.d("FragmentContact", "addNativeAdToList called");
            Log.d("FragmentContact", "adManager.shouldShowAds(): " + adManager.shouldShowAds());
            Log.d("FragmentContact", "adapterContactHome != null: " + (adapterContactHome != null));
            
            if (!adManager.shouldShowAds()) {
                Log.d("FragmentContact", "Ads disabled, not adding native ad");
                return;
            }
            
            // Store the native ad data
            this.nativeAdView = adView;
            this.nativeAd = nativeAd;
            
            // Add the ad to the contacts adapter
            if (adapterContactHome != null) {
                Log.d("FragmentContact", "Adding native ad to adapter");
                adapterContactHome.addNativeAd(adView, nativeAd);
            } else {
                Log.d("FragmentContact", "adapterContactHome is null, cannot add native ad");
            }
        }

        private void onCheckList() {
            if (FragmentContact.this.arrAllContact.size() == 0) {
                this.tvEmpty.setVisibility(View.VISIBLE);
                this.viewScrollAlphaB.setVisibility(View.GONE);
                return;
            }
            this.tvEmpty.setVisibility(View.GONE);
            this.viewScrollAlphaB.setVisibility(View.VISIBLE);
        }

        void loadContactDone() {
            this.adapterContactHome.filter("");
            onCheckList();
        }

        @Override 
        public void onItemClick(ItemContact itemContact) {
            if (FragmentContact.this.getActivity() instanceof ActivityHome) {
                FragmentInfo newInstance = FragmentInfo.newInstance(itemContact, R.string.contacts);
                newInstance.setContactResult(FragmentContact.this.contactResult);
                ((ActivityHome) FragmentContact.this.getActivity()).showFragment(newInstance, true);
            }
        }

        @Override 
        public void onLongClick(ItemContact itemContact) {
            new DialogAddFav(getContext(), this.theme, false, itemContact, new FavResult() { 
                @Override 
                public final void onFavResult(ItemFavorites itemFavorites) {
                    ViewContact.this.m109x406d299f(itemFavorites);
                }
            }).show();
        }

        
        
        public  void m109x406d299f(ItemFavorites itemFavorites) {
            PhoneAccountHandle phoneAccountHandle;
            if (itemFavorites.type == 0) {
                OtherUtils.sendMessage(getContext(), itemFavorites.number);
                return;
            }
            ArrayList<ItemSimInfo> availableSIMCardLabels = SimUtils.getAvailableSIMCardLabels(getContext());
            if (availableSIMCardLabels.size() == 0) {
                return;
            }
            int posSim = MyShare.getPosSim(getContext());
            if (posSim < availableSIMCardLabels.size()) {
                phoneAccountHandle = availableSIMCardLabels.get(posSim).handle;
            } else {
                phoneAccountHandle = availableSIMCardLabels.get(0).handle;
            }
            OtherUtils.call(getContext(), itemFavorites.number, phoneAccountHandle);
        }
    }
}
