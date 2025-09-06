package com.easyrank.dialers.fragment;

import com.easyrank.dialers.item.ItemContact;
import com.easyrank.dialers.item.ItemRecentGroup;


public interface ContactResult {
    void onAddNewContact(ItemRecentGroup itemRecentGroup, ItemContact itemContact);

    void onBack();

    void onContactChange();

    void onFavoritesChange();
}
