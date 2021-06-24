package com.me.stockserver.ui.userstocks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.me.stockserver.R;
import com.me.stockserver.data.structs.Stock;
import com.me.stockserver.data.structs.sendIns.AddSingleStockSendIn;
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

public class UserStocksFragment extends Fragment {

    private UserStocksViewModel userStocksViewModel;
    private static View view;
    private static Context c;
    public static ArrayList<TableRow> userStocksDisplay;
    public static EditText addSingleStock;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userStocksViewModel =
                ViewModelProviders.of(this).get(UserStocksViewModel.class);
        view = inflater.inflate(R.layout.fragment_user_stocks, container, false);
        c = this.getContext();
        userStocksDisplay = new ArrayList<>();
        addSingleStock = view.findViewById(R.id.etAddStock);

        getBoughtStocks();
        Button btnAddSingleStock = view.findViewById(R.id.btnAddSingleStock);
        FloatingActionButton btnRemoveStocks = view.findViewById(R.id.fltbtnDeleteBoughtStocks);
        btnRemoveStocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Stock> stocksToRemove = new ArrayList<>();
                for (int i = 0; i < userStocksDisplay.size(); i++) {
                    LinearLayout ll = (LinearLayout) userStocksDisplay.get(i).getChildAt(3);
                    CheckBox cb = (CheckBox) ll.getChildAt(0);
                    if (cb.isChecked() == true) {
                        stocksToRemove.add((Stock) userStocksDisplay.get(i).getTag());
                    }
                }
                if (stocksToRemove.size() != 0) {
                    MultipleStocksSendIn multipleStocksSendIn = new MultipleStocksSendIn();
                    multipleStocksSendIn.UserStocks = stocksToRemove;
                    multipleStocksSendIn.UserName = username;
                    multipleStocksSendIn.Algorithm = algorithm;
                    Gson gson = new Gson();

                    String send = gson.toJson(multipleStocksSendIn);
                    HTTPRequestParams rp = new HTTPRequestParams("/remove_user_stocks",
                            send,
                            c,
                            UserStocksFragment::removeBoughtStocks,
                            "");
                    new HTTPRequests().execute(rp);
                }
            }
        });
        btnAddSingleStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stockToAdd = addSingleStock.getText().toString();
                Gson gson = new Gson();
                AddSingleStockSendIn addSingleStockSendIn = new AddSingleStockSendIn();
                addSingleStockSendIn.Algorithm = algorithm;
                addSingleStockSendIn.SingleStock = stockToAdd;
                addSingleStockSendIn.UserName = username;
                String send = gson.toJson(addSingleStockSendIn);
                HTTPRequestParams rp = new HTTPRequestParams("/add_single_stock",
                        send,
                        c,
                        UserStocksFragment::removeBoughtStocks,
                        "");
                new HTTPRequests().execute(rp);

            }
        });

        return view;
    }

    public static void getBoughtStocks() {
        BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
        basicUserSendIn.Username = username;
        basicUserSendIn.Algorithm = algorithm;
        Gson gson = new Gson();
        String send = gson.toJson(basicUserSendIn);

        HTTPRequestParams rp = new HTTPRequestParams("/send_user_stocks", send, view, c, UserStocksFragment::addBoughtStocksToDashboardFragment);
        new HTTPRequests().execute(rp);
    }

    public static void addBoughtStocksToDashboardFragment (HTTPRequestParams ret) {
        if (ret.ret == null) {
            return;
        }
        userStocksDisplay = new ArrayList<>();
        ArrayList<Stock> stocks = new ArrayList<>();
        Gson gson = new Gson();
        LinearLayout scrollBought = ret.v.findViewById(R.id.scrollBought);
        scrollBought.removeAllViews();

        try {
            JSONArray ja = new JSONArray(ret.ret);
            for (int i = 0; i < ja.length(); i++) {
                stocks.add(gson.fromJson(ja.get(i).toString(), Stock.class));
            }
        } catch (Exception e) {
            Log.d("Json Parser Error:", e.toString());
        }

        userStocksDisplay = StockDisplay.getStockDisplayForUserStocks(ret.c, stocks);

        for (int i = 0; i < userStocksDisplay.size(); i++) {
            scrollBought.addView(userStocksDisplay.get(i));
        }
    }

    public static void removeBoughtStocks (HTTPRequestParams ret) {
        getBoughtStocks();
    }
}