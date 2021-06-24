package com.me.stockserver.data.structs.stocks.filterResults;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterResults implements Serializable {
    public ArrayList<ADXResult> Adx;
    public BBResult BB;
    public EMAResult EMA;
    public SMAResult SMA;
    public RSIResult RSI;
    public VolumeResult Volume;
    public CloseResult Close;
}
