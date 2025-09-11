package com.easyranktools.easyranktools.fragment;

import com.easyranktools.easyranktools.item.ItemContact;
import com.easyranktools.easyranktools.item.ItemRecentGroup;


public interface ContactResult {
    void onAddNewContact(ItemRecentGroup itemRecentGroup, ItemContact itemContact);

    void onBack();

    void onContactChange();

    void onFavoritesChange();
}
