package com.me.stockserver.data.structs.algorithm;

import com.me.stockserver.data.structs.algorithm.filters.Sell;
import com.me.stockserver.data.structs.algorithm.filters.Purchasable;
import com.me.stockserver.data.structs.algorithm.filters.Viable;

public class Algorithm {
    public String UserName;
    public String Name;
    public int UniqueID;
    public boolean IsRunning;
    public boolean RunOnStart;
    public boolean ShutDown;
    public boolean StraightToMonitoring;
    public boolean DailyAnalysis;
    public Viable ViableFilter;
    public Purchasable PurchasableFilter;
    public Sell SellFilter;
    public int CheckViableHrStart;
    public int CheckViableMinStart;
    public int CheckViableHrIncrement;
    public int CheckViableMinIncrement;
    public int CheckSellHrStart;
    public int CheckSellMinStart;
    public int CheckSellHrIncrement;
    public int CheckSellMinIncrement;
}
