package com.me.stockserver.ui;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.me.stockserver.R;
import com.me.stockserver.data.structs.Stock;
import com.me.stockserver.data.structs.Notification;

import java.util.ArrayList;

public class StockDisplay {
    private static View view;

    public static TableRow.LayoutParams trp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, 1f);
    public static LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    public static TableRow.LayoutParams ll1p = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, .25f);
    public static TableRow.LayoutParams ll2p = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, .25f);
    public static TableRow.LayoutParams ll3p = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, .25f);
    public static TableRow.LayoutParams ll4p = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, .25f);
    public static TableRow.LayoutParams ll5p = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, .5f);
    public static TableRow.LayoutParams ll6p = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

    public static ArrayList<TableRow> getStockDisplayForPurchasableStocks(Context c, ArrayList<Stock> stocks, View v) {
        ArrayList<TableRow> ret = new ArrayList<>();
        view = v;
        if (stocks == null) {
            return null;
        }
        for (int i = 0; i < stocks.size(); i++) {
            TableRow tr = new TableRow(c);
            LinearLayout ll1 = new LinearLayout(c);
            LinearLayout ll2 = new LinearLayout(c);
            LinearLayout ll3 = new LinearLayout(c);
            LinearLayout ll4 = new LinearLayout(c);
            TextView tv1 = new TextView(c);
            TextView tv2 = new TextView(c);
            ImageButton ib = new ImageButton(c);
            CheckBox cb = new CheckBox(c);
            tr.setTag(stocks.get(i));

            tr.setLayoutParams(trp);
            tr.setPadding(2,2,2,2);
            ll1.setLayoutParams(ll1p);
            ll2.setLayoutParams(ll2p);
            ll3.setLayoutParams(ll3p);
            ll4.setLayoutParams(ll4p);
            tv1.setLayoutParams(trp);
            tv2.setLayoutParams(trp);
            cb.setLayoutParams(trp);
            ib.setLayoutParams(trp);

            tv1.setGravity(Gravity.CENTER);
            tv2.setGravity(Gravity.CENTER);
            cb.setGravity(Gravity.CENTER);
            cb.setPadding(0,0,0,5);

            tv1.setTextSize(20);
            tv2.setTextSize(20);

            tv1.setText(stocks.get(i).Symbol);
            tv2.setText(GetLastClose(stocks.get(i)));

            ib.setBackgroundColor(Color.TRANSPARENT);
            ib.setImageDrawable(c.getResources().getDrawable(android.R.drawable.ic_menu_info_details));
            ib.setPadding(0,0,0,20);

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    TableRow tr = new TableRow(v.getContext());
                    LinearLayout ll = new LinearLayout(v.getContext());
                    ll = (LinearLayout) v.getParent();
                    tr = (TableRow) ll.getParent();
                    Stock stock = (Stock) tr.getTag();
                    bundle.putSerializable("stockData", stock);

                    Navigation.findNavController(view).navigate(R.id.fragment_stock_data, bundle);
                }
            });

            ll1.addView(tv1);
            ll2.addView(tv2);
            ll3.addView(ib);
            ll4.addView(cb);

            tr.addView(ll1);
            tr.addView(ll2);
            tr.addView(ll3);
            tr.addView(ll4);

            ret.add(tr);

        }
        return ret;
    }

    public static ArrayList<TableRow> getStockDisplayForUserStocks(Context c, ArrayList<Stock> stocks) {
        ArrayList<TableRow> ret = new ArrayList<>();


        for (int i = 0; i < stocks.size(); i++) {
            TableRow tr = new TableRow(c);
            LinearLayout ll1 = new LinearLayout(c);
            LinearLayout ll2 = new LinearLayout(c);
            LinearLayout ll3 = new LinearLayout(c);
            LinearLayout ll4 = new LinearLayout(c);
            TextView tv1 = new TextView(c);
            TextView tv2 = new TextView(c);
            ImageButton ib = new ImageButton(c);
            CheckBox cb = new CheckBox(c);
            tr.setTag(stocks.get(i));

            tr.setLayoutParams(trp);
            tr.setPadding(2,2,2,2);
            ll1.setLayoutParams(ll1p);
            ll2.setLayoutParams(ll2p);
            ll3.setLayoutParams(ll3p);
            ll4.setLayoutParams(ll4p);
            tv1.setLayoutParams(trp);
            tv2.setLayoutParams(trp);
            cb.setLayoutParams(trp);
            ib.setLayoutParams(trp);

            tv1.setGravity(Gravity.CENTER);
            tv2.setGravity(Gravity.CENTER);
            cb.setGravity(Gravity.CENTER);

            tv1.setTextSize(20);
            tv2.setTextSize(20);

            tv1.setText(stocks.get(i).Symbol);
            tv2.setText(GetLastClose(stocks.get(i)));


            ib.setBackgroundColor(Color.TRANSPARENT);
            ib.setPadding(0,0,0,20);

            if (stocks.get(i).Sell == true) {
                ib.setImageDrawable(c.getResources().getDrawable(android.R.drawable.ic_delete));
            } else if (stocks.get(i).PossibleTop == true) {
                ib.setImageDrawable(c.getResources().getDrawable(android.R.drawable.stat_sys_warning));
            } else {
                ib.setImageDrawable(c.getResources().getDrawable(android.R.drawable.ic_menu_info_details));
            }

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    TableRow tr = new TableRow(v.getContext());
                    LinearLayout ll = new LinearLayout(v.getContext());
                    ll = (LinearLayout) v.getParent();
                    tr = (TableRow) ll.getParent();
                    Stock stock = (Stock) tr.getTag();
                    bundle.putSerializable("stockData", stock);

                    Navigation.findNavController(v).navigate(R.id.fragment_stock_data, bundle);
                }
            });

            ll1.addView(tv1);
            ll2.addView(tv2);
            ll3.addView(ib);
            ll4.addView(cb);

            tr.addView(ll1);
            tr.addView(ll2);
            tr.addView(ll3);
            tr.addView(ll4);

            ret.add(tr);

        }
        return ret;
    }

    public static TableRow CreateLineForStockData(String title, String value, Context c) {

        TableRow tr = new TableRow(c);
        LinearLayout ll1 = new LinearLayout(c);
        LinearLayout ll2 = new LinearLayout(c);
        TextView tv1 = new TextView(c);
        TextView tv2 = new TextView(c);
        tr.setLayoutParams(trp);
        ll1.setLayoutParams(ll5p);
        ll2.setLayoutParams(ll5p);
        tv1.setLayoutParams(trp);
        tv2.setLayoutParams(ll6p);
        tv1.setText(title);
        tv1.setTextSize(18);
        tv2.setText(value);
        tv2.setTextSize(18);
        tv2.setGravity(Gravity.RIGHT);
        ll1.addView(tv1);
        ll2.addView(tv2);
        tr.addView(ll1);
        tr.addView(ll2);

        return tr;
    }

    public static LinearLayout CreateNotificationDisplay (Notification notification, Context c) {
        LinearLayout ll = new LinearLayout(c);
        ll.setLayoutParams(llp);
        TableRow tr = new TableRow(c);
        TableRow tr2 = new TableRow(c);
        LinearLayout ll1 = new LinearLayout(c);
        LinearLayout ll2 = new LinearLayout(c);
        LinearLayout ll3 = new LinearLayout(c);
        LinearLayout ll4 = new LinearLayout(c);
        RelativeLayout rl = new RelativeLayout(c);
        TextView tv1 = new TextView(c);
        TextView tv2 = new TextView(c);
        TextView tv3 = new TextView(c);
        CheckBox cb = new CheckBox(c);

        ll.setTag(notification);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll4.setGravity(Gravity.RIGHT);


        tr.setLayoutParams(trp);
        tr2.setLayoutParams(trp);
        ll1.setLayoutParams(ll5p);
        ll2.setLayoutParams(ll5p);
        ll3.setLayoutParams(ll5p);
        ll4.setLayoutParams(ll6p);
        tv1.setLayoutParams(trp);
        tv2.setLayoutParams(trp);
        tv3.setLayoutParams(trp);
        cb.setLayoutParams(trp);

        tv1.setGravity(Gravity.LEFT);
        tv2.setGravity(Gravity.RIGHT);
        tv3.setGravity(Gravity.LEFT);

        tv1.setText(notification.Title);
        tv1.setTextSize(15);
        tv2.setText(notification.Date.toString());
        tv2.setTextSize(15);
        tv3.setText(notification.Message);
        tv3.setTextSize(15);
        tv3.setMaxLines(100);

        ll1.addView(tv1);
        ll2.addView(tv2);
        ll3.addView(tv3);
        ll4.addView(cb);

        tr.addView(ll1);
        tr.addView(ll2);
        tr2.addView(ll3);
        tr2.addView(ll4);

        ll.addView(tr);
        ll.addView(tr2);
        return ll;
    }

    public static String GetLastClose (Stock stock) {
        if (stock.OneHour.Close.LastClose != 0){
            return Float.toString(stock.OneHour.Close.LastClose);
        }
        if (stock.FourHour.Close.LastClose != 0){
            return Float.toString(stock.FourHour.Close.LastClose);
        }
        if (stock.OneDay.Close.LastClose != 0){
            return Float.toString(stock.OneDay.Close.LastClose);
        }
        if (stock.OneWeek.Close.LastClose != 0){
            return Float.toString(stock.OneWeek.Close.LastClose);
        }
        return "";
    }
}
