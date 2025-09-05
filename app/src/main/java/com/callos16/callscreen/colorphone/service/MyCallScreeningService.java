package com.callos16.callscreen.colorphone.service;

import android.net.Uri;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.telephony.PhoneNumberUtils;
import com.callos16.callscreen.colorphone.item.ItemContact;
import com.callos16.callscreen.colorphone.item.ItemPhone;
import com.callos16.callscreen.colorphone.utils.MyShare;
import com.callos16.callscreen.colorphone.utils.ReadContact;
import java.util.Iterator;


public class MyCallScreeningService extends CallScreeningService {
    @Override 
    public void onScreenCall(Call.Details details) {
        String decode = Uri.decode(details.getHandle().toString());
        String substring = (decode == null || !decode.startsWith("tel:")) ? "" : decode.substring(decode.indexOf("tel:") + 4);
        String idWithNumber = ReadContact.getIdWithNumber(this, substring);
        boolean z = false;
        if (!idWithNumber.isEmpty()) {
            Iterator<ItemContact> it = MyShare.getArrBlock(this).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ItemContact next = it.next();
                if (idWithNumber.equals(next.getId())) {
                    z = true;
                    break;
                } else if (!next.getArrPhone().isEmpty()) {
                    Iterator<ItemPhone> it2 = next.getArrPhone().iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        ItemPhone next2 = it2.next();
                        if (!substring.isEmpty() && next2.getNumber() != null && PhoneNumberUtils.compare(substring, next2.getNumber())) {
                            z = true;
                            break;
                        }
                    }
                    if (z) {
                        break;
                    }
                }
            }
        }
        respondToCall(details, new CallResponse.Builder().setDisallowCall(z).setRejectCall(z).setSkipCallLog(z).setSkipNotification(z).build());
    }
}
