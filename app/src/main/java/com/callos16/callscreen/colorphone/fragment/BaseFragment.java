package com.callos16.callscreen.colorphone.fragment;

import androidx.fragment.app.Fragment;
import com.callos16.callscreen.colorphone.ActivityHome;
import com.callos16.callscreen.colorphone.item.ItemContact;
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
