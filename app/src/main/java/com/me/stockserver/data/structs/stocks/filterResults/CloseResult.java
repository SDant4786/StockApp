package com.me.stockserver.data.structs.stocks.filterResults;

import java.io.Serializable;

public class CloseResult implements Serializable {
    public float LastClose;
    public int GreaterThanLast;
}
