package com.me.stockserver.data.structs;

import com.me.stockserver.data.structs.stocks.Bar;
import com.me.stockserver.data.structs.stocks.filterResults.FilterResults;

import java.io.Serializable;
import java.util.ArrayList;

public class Stock implements Serializable {

    public String Symbol;
    public ArrayList<Bar> WeekBars;
    public ArrayList<Bar> DayBars;
    public ArrayList<Bar> FourHourBars;
    public ArrayList<Bar> HourBars;
    public FilterResults OneWeek;
    public FilterResults OneDay;
    public FilterResults OneHour;
    public FilterResults FourHour;

    public boolean PossibleTop;
    public boolean Sell;
}
