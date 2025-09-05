package com.callos16.callscreen.colorphone.utils;

import com.callos16.callscreen.colorphone.item.ItemContact;
import java.util.Comparator;



public final  class ReadContact$$ExternalSyntheticLambda2 implements Comparator {
    public static final  ReadContact$$ExternalSyntheticLambda2 INSTANCE = new ReadContact$$ExternalSyntheticLambda2();

    private  ReadContact$$ExternalSyntheticLambda2() {
    }

    @Override 
    public final int compare(Object obj, Object obj2) {
        int compareTo;
        compareTo = ((ItemContact) obj).getName().toLowerCase().compareTo(((ItemContact) obj2).getName().toLowerCase());
        return compareTo;
    }
}
