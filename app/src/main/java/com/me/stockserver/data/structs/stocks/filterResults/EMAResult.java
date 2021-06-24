package com.me.stockserver.data.structs.stocks.filterResults;

import java.io.Serializable;

public class EMAResult implements Serializable {
    public float EMA1;
    public float EMA2;
    public float EMA3;
    public float EMASlopeCurrent;
    public float EMASlopePrevious;
    public float EMASlopeIncreasePercent;
}
