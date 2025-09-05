package com.callos16.callscreen.colorphone.fragment;

import com.callos16.callscreen.colorphone.item.ItemContact;
import com.callos16.callscreen.colorphone.item.ItemRecentGroup;


public interface ContactResult {
    void onAddNewContact(ItemRecentGroup itemRecentGroup, ItemContact itemContact);

    void onBack();

    void onContactChange();

    void onFavoritesChange();
}
