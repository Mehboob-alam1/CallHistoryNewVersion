package com.easyrank.dialers.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.easyrank.dialers.custom.ViewAlphaB;
import com.easyrank.dialers.custom.ViewListContact;
import com.easyrank.dialers.item.ItemContact;
import com.easyrank.dialers.item.ItemPhone;
import com.easyrank.dialers.item.ItemShowContact;
import com.easyrank.dialers.utils.OtherUtils;
import java.util.ArrayList;
import java.util.Iterator;


public class AdapterContactHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<ItemContact> arrContact;
    private final ArrayList<ItemShowContact> arrFilter = new ArrayList<>();
    private final ContactResult contactResult;
    private final boolean theme;
    private View nativeAdView;
    private com.google.android.gms.ads.nativead.NativeAd nativeAd;
    private boolean hasNativeAd = false;
    private static final int NATIVE_AD_INTERVAL = 10; // Show ad after every 10 items

    
    public interface ContactResult {
        void onItemClick(ItemContact itemContact);

        void onLongClick(ItemContact itemContact);
    }

    public AdapterContactHome(ArrayList<ItemContact> arrayList, boolean z, ContactResult contactResult) {
        this.arrContact = arrayList;
        this.contactResult = contactResult;
        this.theme = z;
        makeArr("");
    }

    @Override 
    public int getItemViewType(int i) {
        if (hasNativeAd && shouldShowNativeAdAtPosition(i)) {
            return 2; // Native ad type
        }
        int adjustedIndex = getAdjustedIndex(i);
        return this.arrFilter.get(adjustedIndex).alphaB != null ? 1 : 0;
    }
    
    private boolean shouldShowNativeAdAtPosition(int position) {
        if (!hasNativeAd) return false;
        // Show native ad after every 10 items (at positions 10, 20, 30, etc.)
        return (position + 1) % (NATIVE_AD_INTERVAL + 1) == 0;
    }
    
    private int getAdjustedIndex(int position) {
        if (!hasNativeAd) return position;
        // Count how many native ads appear before this position
        int nativeAdCount = (position + 1) / (NATIVE_AD_INTERVAL + 1);
        return position - nativeAdCount;
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 2) {
            // Create a new native ad view for each ViewHolder
            View adView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.native_ad_list_item, viewGroup, false);
            return new HolderNativeAd(adView);
        }
        if (i == 1) {
            return new HolderAlphaB(new ViewAlphaB(viewGroup.getContext()));
        }
        return new HolderContact(new ViewListContact(viewGroup.getContext()));
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
        int adjustedIndex = getAdjustedIndex(i);
        if (viewHolder instanceof HolderAlphaB) {
            ((HolderAlphaB) viewHolder).viewAlphaB.setAlphaB(this.arrFilter.get(adjustedIndex).alphaB);
        } else {
            ((HolderContact) viewHolder).vContact.setContact(this.arrFilter.get(adjustedIndex).itemContact, this.theme);
        }
    }

    @Override 
    public int getItemCount() {
        if (!hasNativeAd) return this.arrFilter.size();
        // Calculate how many native ads should be shown
        int nativeAdCount = this.arrFilter.size() / NATIVE_AD_INTERVAL;
        return this.arrFilter.size() + nativeAdCount;
    }

    public void filter(String str) {
        makeArr(str);
        notifyDataSetChanged();
    }

    private void makeArr(String str) {
        if (str.isEmpty()) {
            addData(this.arrContact);
            return;
        }
        ArrayList<ItemContact> arrayList = new ArrayList<>();
        Iterator<ItemContact> it = this.arrContact.iterator();
        while (it.hasNext()) {
            ItemContact next = it.next();
            if (next.getName() != null && next.getName().toLowerCase().contains(str)) {
                arrayList.add(next);
            } else if (next.getArrPhone() != null && !next.getArrPhone().isEmpty()) {
                Iterator<ItemPhone> it2 = next.getArrPhone().iterator();
                while (true) {
                    if (it2.hasNext()) {
                        ItemPhone next2 = it2.next();
                        if (next2.getNumber() != null && next2.getNumber().toLowerCase().contains(str)) {
                            arrayList.add(next);
                            break;
                        }
                    }
                }
            }
        }
        addData(arrayList);
    }

    private void addData(ArrayList<ItemContact> arrayList) {
        this.arrFilter.clear();
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        String[] arrAlphaB = OtherUtils.arrAlphaB();
        int i = -1;
        Iterator<ItemContact> it = arrayList.iterator();
        while (it.hasNext()) {
            ItemContact next = it.next();
            int posAlphaB = getPosAlphaB(arrAlphaB, next.getName());
            if (i != posAlphaB) {
                this.arrFilter.add(new ItemShowContact(arrAlphaB[posAlphaB]));
                i = posAlphaB;
            }
            this.arrFilter.add(new ItemShowContact(next));
        }
    }

    private int getPosAlphaB(String[] strArr, String str) {
        String substring = (str == null || str.isEmpty()) ? "#" : str.substring(0, 1);
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equalsIgnoreCase(substring)) {
                return i;
            }
        }
        return strArr.length - 1;
    }

    public int getLocationAlphaB(String str) {
        Iterator<ItemShowContact> it = this.arrFilter.iterator();
        while (it.hasNext()) {
            ItemShowContact next = it.next();
            if (next.alphaB != null && next.alphaB.equalsIgnoreCase(str)) {
                int originalIndex = this.arrFilter.indexOf(next);
                // Adjust for native ads that appear before this position
                int nativeAdCount = originalIndex / NATIVE_AD_INTERVAL;
                return originalIndex + nativeAdCount;
            }
        }
        return -1;
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

    
    class HolderNativeAd extends RecyclerView.ViewHolder {
        public HolderNativeAd(View view) {
            super(view);
        }
    }
    
    class HolderAlphaB extends RecyclerView.ViewHolder {
        ViewAlphaB viewAlphaB;

        public HolderAlphaB(ViewAlphaB viewAlphaB) {
            super(viewAlphaB);
            this.viewAlphaB = viewAlphaB;
        }
    }

    
    
    public class HolderContact extends RecyclerView.ViewHolder {
        ViewListContact vContact;

        public HolderContact(ViewListContact viewListContact) {
            super(viewListContact);
            this.vContact = viewListContact;
            viewListContact.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public final void onClick(View view) {
                    HolderContact.this.m55x8cab3be4(view);
                }
            });
            viewListContact.setOnLongClickListener(new View.OnLongClickListener() { 
                @Override 
                public final boolean onLongClick(View view) {
                    return HolderContact.this.m56x8d79ba65(view);
                }
            });
        }

        
        
        public  void m55x8cab3be4(View view) {
            AdapterContactHome.this.contactResult.onItemClick(((ItemShowContact) AdapterContactHome.this.arrFilter.get(getLayoutPosition())).itemContact);
        }

        
        
        public  boolean m56x8d79ba65(View view) {
            AdapterContactHome.this.contactResult.onLongClick(((ItemShowContact) AdapterContactHome.this.arrFilter.get(getLayoutPosition())).itemContact);
            return true;
        }
    }
}
