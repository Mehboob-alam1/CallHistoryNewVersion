package com.easyrank.dialers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.aitsuki.swipe.SwipeLayout;
import com.easyrank.dialers.R;
import com.easyrank.dialers.custom.FavOnItemClick;
import com.easyrank.dialers.custom.LayoutItemTopRecent;
import com.easyrank.dialers.custom.LayoutRecent;
import com.easyrank.dialers.custom.TextW;
import com.easyrank.dialers.item.ItemRecentGroup;
import com.easyrank.dialers.item.ItemSimInfo;


import java.util.ArrayList;
import java.util.Iterator;


public class AdapterRecent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<ItemRecentGroup> arrGroup;
    private final ArrayList<ItemRecentGroup> arrShow;
    private final ArrayList<ItemSimInfo> arrSim;
    private boolean isChoose;
    private boolean isMiss;
    private final RecentItemClick recentItemClick;
    private final boolean theme;
    private View nativeAdView;
    private com.google.android.gms.ads.nativead.NativeAd nativeAd;
    private boolean hasNativeAd = false;
    private static final int NATIVE_AD_INTERVAL = 10; // Show ad after every 10 items

    
    public interface RecentItemClick {
        void onDel(ItemRecentGroup itemRecentGroup);

        void onInfo(ItemRecentGroup itemRecentGroup);

        void onItemClick(ItemRecentGroup itemRecentGroup);

        void onLongClick(ItemRecentGroup itemRecentGroup);
    }

    @Override 
    public int getItemViewType(int i) {
        if (i == 0) return 0; // Header
        
        if (hasNativeAd && shouldShowNativeAdAtPosition(i)) {
            return 2; // Native ad type
        }
        return 1; // Recent item
    }
    
    private boolean shouldShowNativeAdAtPosition(int position) {
        if (!hasNativeAd) return false;
        // Show native ad after every 10 items (at positions 11, 21, 31, etc.)
        // Position 0 is header, so we check position - 1 for recent items
        int recentItemPosition = position - 1;
        return recentItemPosition > 0 && recentItemPosition % NATIVE_AD_INTERVAL == 0;
    }
    
    private int getAdjustedIndex(int position) {
        if (position == 0) return -1; // Header
        if (!hasNativeAd) return position - 1;
        
        // Count how many native ads appear before this position
        int recentItemPosition = position - 1;
        int nativeAdCount = recentItemPosition / NATIVE_AD_INTERVAL;
        return recentItemPosition - nativeAdCount;
    }

    public AdapterRecent(ArrayList<ItemRecentGroup> arrayList, ArrayList<ItemSimInfo> arrayList2, boolean z, RecentItemClick recentItemClick) {
        this.arrGroup = arrayList;
        this.arrShow = new ArrayList<>(arrayList);
        this.recentItemClick = recentItemClick;
        this.theme = z;
        this.arrSim = arrayList2;
    }

    public void addNewData() {
        if (this.isMiss) {
            showMiss(true);
            return;
        }
        this.arrShow.clear();
        this.arrShow.addAll(this.arrGroup);
        notifyDataSetChanged();
    }

    public void setChoose(boolean z) {
        this.isChoose = z;
        notifyItemRangeChanged(0, getItemCount(), true);
    }

    public void showMiss(boolean z) {
        this.isMiss = z;
        this.arrShow.clear();
        if (z) {
            Iterator<ItemRecentGroup> it = this.arrGroup.iterator();
            while (it.hasNext()) {
                ItemRecentGroup next = it.next();
                if (next.arrRecent.get(0).type == 3) {
                    this.arrShow.add(next);
                }
            }
        } else {
            this.arrShow.addAll(this.arrGroup);
        }
        notifyDataSetChanged();
    }

    public void removeRecent(ItemRecentGroup itemRecentGroup) {
        this.arrGroup.remove(itemRecentGroup);
        this.arrShow.remove(itemRecentGroup);
        notifyItemRemoved(this.arrShow.indexOf(itemRecentGroup) + 1);
    }

    public void removeAll() {
        this.arrShow.clear();
        this.arrGroup.clear();
        this.isChoose = false;
        notifyDataSetChanged();
    }

    public boolean isChoose() {
        return this.isChoose;
    }
    
    public void addNativeAd(View adView, com.google.android.gms.ads.nativead.NativeAd nativeAd) {
        this.nativeAdView = adView;
        this.nativeAd = nativeAd;
        this.hasNativeAd = true;
        notifyDataSetChanged();
    }
    
    public void removeNativeAd() {
        this.hasNativeAd = false;
        this.nativeAdView = null;
        this.nativeAd = null;
        notifyDataSetChanged();
    }
    
    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, com.google.android.gms.ads.nativead.NativeAdView adView) {
        // Set the icon view
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        
        // Set other assets
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        
        // Populate the native ad view
        adView.setNativeAd(nativeAd);
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 2) {
            // Create a new native ad view for each ViewHolder
            View adView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.native_ad_list_item, viewGroup, false);
            return new HolderNativeAd(adView);
        }
        if (i == 0) {
            return new HolderTop(new LayoutItemTopRecent(viewGroup.getContext()));
        }
        return new HolderItem(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recent, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HolderNativeAd) {
            // Populate the native ad view with the loaded ad
            if (nativeAd != null) {
                populateNativeAdView(nativeAd, (com.google.android.gms.ads.nativead.NativeAdView) viewHolder.itemView);
            }
            return;
        }
        if (viewHolder instanceof HolderItem) {
            HolderItem holderItem = (HolderItem) viewHolder;
            int i2 = -1;
            int adjustedIndex = getAdjustedIndex(i);
            ItemRecentGroup itemRecentGroup = this.arrShow.get(adjustedIndex);
            String str = itemRecentGroup.arrRecent.get(0).simId;
            if (this.arrSim.size() > 1 && str != null && !str.isEmpty()) {
                int i3 = 0;
                while (true) {
                    if (i3 >= this.arrSim.size()) {
                        break;
                    } else if (str.equals(this.arrSim.get(i3).handle.getId())) {
                        i2 = i3;
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            holderItem.layoutRecent.setItemRecent(itemRecentGroup, i2, this.isChoose, this.theme);
            if (this.isChoose) {
                holderItem.sw.setSwipeFlags(0);
                holderItem.sw.closeRightMenu(true);
                return;
            }
            holderItem.sw.setSwipeFlags(1);
        }
    }

    @Override 
    public int getItemCount() {
        if (!hasNativeAd) return this.arrShow.size() + 1; // +1 for header
        
        // Calculate how many native ads should be shown
        int nativeAdCount = this.arrShow.size() / NATIVE_AD_INTERVAL;
        return this.arrShow.size() + 1 + nativeAdCount; // +1 for header
    }

    
    class HolderNativeAd extends RecyclerView.ViewHolder {
        public HolderNativeAd(View view) {
            super(view);
        }
    }
    
    class HolderTop extends RecyclerView.ViewHolder {
        public HolderTop(LayoutItemTopRecent layoutItemTopRecent) {
            super(layoutItemTopRecent);
        }
    }

    
    
    public class HolderItem extends RecyclerView.ViewHolder {
        LayoutRecent layoutRecent;
        SwipeLayout sw;

        public HolderItem(View view) {
            super(view);
            this.sw = (SwipeLayout) view;
            LayoutRecent layoutRecent = (LayoutRecent) view.findViewById(R.id.content);
            this.layoutRecent = layoutRecent;
            layoutRecent.setFavOnItemClick(new FavOnItemClick() { 
                @Override 
                public void onLongClick() {
                    if (AdapterRecent.this.isChoose) {
                        return;
                    }
                    AdapterRecent.this.recentItemClick.onLongClick((ItemRecentGroup) AdapterRecent.this.arrShow.get(HolderItem.this.getLayoutPosition() - 1));
                }

                @Override 
                public void onItemClick() {
                    if (AdapterRecent.this.isChoose) {
                        return;
                    }
                    AdapterRecent.this.recentItemClick.onItemClick((ItemRecentGroup) AdapterRecent.this.arrShow.get(HolderItem.this.getLayoutPosition() - 1));
                }

                @Override 
                public void onDel() {
                    AdapterRecent.this.recentItemClick.onDel((ItemRecentGroup) AdapterRecent.this.arrShow.get(HolderItem.this.getLayoutPosition() - 1));
                }

                @Override 
                public void onInfo() {
                    AdapterRecent.this.recentItemClick.onInfo((ItemRecentGroup) AdapterRecent.this.arrShow.get(HolderItem.this.getLayoutPosition() - 1));
                }
            });
            TextW textW = (TextW) view.findViewById(R.id.right_menu);
            textW.setupText(400, 4.0f);
            textW.setTextColor(-1);
            textW.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public final void onClick(View view2) {
                    HolderItem.this.m59xc5a92fdd(view2);
                }
            });
            this.layoutRecent.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public final void onClick(View view2) {
                    HolderItem.this.m60xb6fabf5e(view2);
                }
            });
        }

        
        
        public  void m59xc5a92fdd(View view) {
            AdapterRecent.this.recentItemClick.onDel((ItemRecentGroup) AdapterRecent.this.arrShow.get(getLayoutPosition() - 1));
        }

        
        
        public  void m60xb6fabf5e(View view) {
            if (this.sw.isRightMenuOpened()) {
                return;
            }
            AdapterRecent.this.recentItemClick.onItemClick((ItemRecentGroup) AdapterRecent.this.arrShow.get(getLayoutPosition() - 1));
        }
    }
}
