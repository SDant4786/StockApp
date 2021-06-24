package com.me.stockserver.ui.purchasable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.me.stockserver.R;
import com.me.stockserver.data.structs.Notification;
import com.me.stockserver.data.structs.Stock;
import com.me.stockserver.data.structs.sendIns.BasicUserSendIn;
import com.me.stockserver.data.structs.sendIns.MultipleStocksSendIn;
import com.me.stockserver.httpRequests.HTTPRequestParams;
import com.me.stockserver.httpRequests.HTTPRequests;
import com.me.stockserver.ui.StockDisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.me.stockserver.MainActivity.algorithm;
import static com.me.stockserver.MainActivity.username;

public class PurchasableFragment extends Fragment {

    private PurchasableViewModel purchasableViewModel;
    private static View view;
    private static Context c;
    public static ArrayList<TableRow> purchasableStockDisplay;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        purchasableViewModel =
                ViewModelProviders.of(this).get(PurchasableViewModel.class);
        view = inflater.inflate(R.layout.fragment_purchaseable, container, false);
        c = this.getContext();
        purchasableStockDisplay = new ArrayList<>();

        getSelectedStocks();

        FloatingActionButton btnBoughtStocks = view.findViewById(R.id.fltbtnBoughtStocks);
       btnBoughtStocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Stock> NewBoughtStocks = new ArrayList<>();
                for (int i = 0; i < purchasableStockDisplay.size(); i++) {
                    LinearLayout ll = (LinearLayout) purchasableStockDisplay.get(i).getChildAt(3);
                    CheckBox cb = (CheckBox) ll.getChildAt(0);
                    if (cb.isChecked() == true) {
                        NewBoughtStocks.add((Stock) purchasableStockDisplay.get(i).getTag());
                    }
                }
                if (NewBoughtStocks.size() != 0) {
                    MultipleStocksSendIn multipleStocksSendIn = new MultipleStocksSendIn();
                    multipleStocksSendIn.Algorithm = algorithm;
                    multipleStocksSendIn.UserName = username;
                    multipleStocksSendIn.UserStocks = NewBoughtStocks;
                    Gson gson = new Gson();
                    String send = gson.toJson(multipleStocksSendIn);
                    HTTPRequestParams rp = new HTTPRequestParams("/add_to_user_stocks",
                            send,
                            c,
                            PurchasableFragment::sendBoughtStocks,
                            "");
                    new HTTPRequests().execute(rp);
                    getSelectedStocks();
                }
            }
        });

        return view;
    }

    public static void getSelectedStocks() {
        BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
        basicUserSendIn.Algorithm = algorithm;
        basicUserSendIn.Username = username;
        Gson gson = new Gson();
        String send = gson.toJson(basicUserSendIn);

        HTTPRequestParams rp = new HTTPRequestParams("/send_purchasable_stocks", send, view, c, PurchasableFragment::addSelectedStocksToHomeFragment);
        new HTTPRequests().execute(rp);
    }

    public static void addSelectedStocksToHomeFragment (HTTPRequestParams ret) {
        if (ret.ret == null) {
            return;
        }
        purchasableStockDisplay = new ArrayList<>();
        ArrayList<Stock> stocks = new ArrayList<>();
        Gson gson = new Gson();
        LinearLayout scrollHome = ret.v.findViewById(R.id.homeScroll);
        scrollHome.removeAllViews();

        TypeToken<ArrayList<Stock>> token = new TypeToken<ArrayList<Stock>>() {};
        stocks = gson.fromJson(ret.ret, token.getType());

        if (stocks == null) {
            return;
        }
        purchasableStockDisplay = StockDisplay.getStockDisplayForPurchasableStocks(ret.c, stocks, view);

        for (int i = 0; i < purchasableStockDisplay.size(); i++){
            scrollHome.addView(purchasableStockDisplay.get(i));
        }
    }
    public static void sendBoughtStocks (HTTPRequestParams ret) {
        getSelectedStocks();
    }
}