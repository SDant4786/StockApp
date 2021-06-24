package com.me.stockserver.data.structs.sendIns;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginSendBack implements Serializable {
    public boolean Successful;
    public ArrayList<UserAlgorithm> Algorithms;

}
