package com.me.stockserver.data.structs.stocks.filterResults;

import java.io.Serializable;

public class SMAResult implements Serializable {

    public float SMA1;
    public float SMA2;
    public float SMA3;
    public float SMASlopeCurrent;
    public float SMASlopePrevious;
    public float SMASlopeIncreasePercent;
}
