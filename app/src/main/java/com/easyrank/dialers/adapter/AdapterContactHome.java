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
    private boolean hasNativeAd = false;

    
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
        if (hasNativeAd && i == 0) {
            return 2; // Native ad type
        }
        int adjustedIndex = hasNativeAd ? i - 1 : i;
        return this.arrFilter.get(adjustedIndex).alphaB != null ? 1 : 0;
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 2) {
            return new HolderNativeAd(nativeAdView);
        }
        if (i == 1) {
            return new HolderAlphaB(new ViewAlphaB(viewGroup.getContext()));
        }
        return new HolderContact(new ViewListContact(viewGroup.getContext()));
    }

    @Override 
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HolderNativeAd) {
            // Native ad doesn't need binding
            return;
        }
        int adjustedIndex = hasNativeAd ? i - 1 : i;
        if (viewHolder instanceof HolderAlphaB) {
            ((HolderAlphaB) viewHolder).viewAlphaB.setAlphaB(this.arrFilter.get(adjustedIndex).alphaB);
        } else {
            ((HolderContact) viewHolder).vContact.setContact(this.arrFilter.get(adjustedIndex).itemContact, this.theme);
        }
    }

    @Override 
    public int getItemCount() {
        return this.arrFilter.size() + (hasNativeAd ? 1 : 0);
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
                return this.arrFilter.indexOf(next) + (hasNativeAd ? 1 : 0);
            }
        }
        return -1;
    }
    
    public void addNativeAd(View adView) {
        this.nativeAdView = adView;
        this.hasNativeAd = true;
        notifyItemInserted(0);
    }
    
    public void removeNativeAd() {
        this.hasNativeAd = false;
        this.nativeAdView = null;
        notifyItemRemoved(0);
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
