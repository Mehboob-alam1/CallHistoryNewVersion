package com.easyranktools.easyranktools.fragment;

import androidx.fragment.app.Fragment;
import com.easyranktools.easyranktools.ActivityHome;
import com.easyranktools.easyranktools.item.ItemContact;
import java.util.ArrayList;


public abstract class BaseFragment extends Fragment {
    ArrayList<ItemContact> arrAllContact;
    ContactResult contactResult;

    public void getArrContact() {
        if (getActivity() instanceof ActivityHome) {
            this.arrAllContact = ((ActivityHome) getActivity()).getArrAllContact();
        } else {
            this.arrAllContact = new ArrayList<>();
        }
    }

    public void setContactResult(ContactResult contactResult) {
        this.contactResult = contactResult;
    }
}
