package com.me.stockserver.ui.stockData;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
//import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.me.stockserver.R;
import com.me.stockserver.data.structs.Stock;
import com.me.stockserver.data.structs.stocks.filterResults.FilterResults;
import com.me.stockserver.ui.StockDisplay;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StockData extends Fragment {
    private static View view;
    private static Context c;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        Stock stock = (Stock) bundle.getSerializable("stockData");


        view = inflater.inflate(R.layout.fragment_stock_data, container, false);
        c = this.getContext();

        updateDisplay(stock);

        return view;
    }

    public void updateDisplay (Stock stock) {

        TextView title = view.findViewById(R.id.symbol2);
        TextView sell = view.findViewById(R.id.sellSignal);

        LinearLayout oneHr = view.findViewById(R.id.oneHourLayout);
        LinearLayout fourHr = view.findViewById(R.id.fourHourLayout);
        LinearLayout oneDay = view.findViewById(R.id.oneDayLayout);
        LinearLayout oneWeek = view.findViewById(R.id.oneWeekLayout);

        title.setText(stock.Symbol);
        if (stock.Sell == true) {
            sell.setText("SELL");
            sell.setBackgroundColor(Color.RED);
        } else if (stock.PossibleTop == true) {
            sell.setText("POSSIBLE TOP");
            sell.setBackgroundColor((Color.YELLOW));
        } else {
            sell.setText("RISING");
        }

        if (stock.OneWeek != null){
            addResult(oneWeek, stock.OneWeek, c);
        }
        if (stock.OneDay != null){
            addResult(oneDay, stock.OneDay, c);
        }
        if (stock.FourHour != null){
            addResult(fourHr, stock.FourHour, c);
        }
        if (stock.OneHour != null){
            addResult(oneHr, stock.OneHour, c);
        }
    }

    public void addResult(LinearLayout linearLayout, FilterResults filterResults, Context c) {
        if (filterResults.Adx != null && (filterResults.Adx.get(0).Adx != 0 || filterResults.Adx.get(0).Pdi != 0 ||filterResults.Adx.get(0).Mdi != 0)){
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.result_adx);
            linearLayout.addView(stub);
            View s = stub.inflate();

            TextView tvADX = s.findViewById(R.id.tvADX);
            TextView tvPDI = s.findViewById(R.id.tvPDI);
            TextView tvMDI = s.findViewById(R.id.tvMDI);

            int adxLength = filterResults.Adx.size() - 1;
            tvADX.setText(Float.toString(filterResults.Adx.get(adxLength).Adx));
            tvPDI.setText(Float.toString(filterResults.Adx.get(adxLength).Pdi));
            tvMDI.setText(Float.toString(filterResults.Adx.get(adxLength).Mdi));
        }
        if (filterResults.BB.LowerBand != 0) {
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.result_bb);
            linearLayout.addView(stub);
            View s = stub.inflate();

            TextView tvBBUpperBand = s.findViewById(R.id.tvBBUpperBand);
            TextView tvBBLowerBand = s.findViewById(R.id.tvBBLowerBand);

            tvBBUpperBand.setText(Float.toString(filterResults.BB.UpperBand));
            tvBBLowerBand.setText(Float.toString(filterResults.BB.LowerBand));
        }
        if (filterResults.EMA.EMA3 != 0) {
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.result_ema);
            linearLayout.addView(stub);
            View s = stub.inflate();

            TextView tvEMACurrent = s.findViewById(R.id.tvEMACurrent);
            TextView tvEMAPrevious = s.findViewById(R.id.tvEMAPrevious);
            TextView tvPrevious2 = s.findViewById(R.id.tvPrevious2);
            TextView tvEMASLopeCurrent = s.findViewById(R.id.tvEMASLopeCurrent);
            TextView tvEMASlopePrevious = s.findViewById(R.id.tvEMASlopePrevious);
            TextView tvEMASlopeIncreasePercent = s.findViewById(R.id.tvEMASlopeIncreasePercent);

            tvEMACurrent.setText(Float.toString(filterResults.EMA.EMA3));
            tvEMAPrevious.setText(Float.toString(filterResults.EMA.EMA2));
            tvPrevious2.setText(Float.toString(filterResults.EMA.EMA1));
            tvEMASLopeCurrent.setText(Float.toString(filterResults.EMA.EMASlopeCurrent));
            tvEMASlopePrevious.setText(Float.toString(filterResults.EMA.EMASlopePrevious));
            tvEMASlopeIncreasePercent.setText(Float.toString(filterResults.EMA.EMASlopeIncreasePercent));
        }
        if (filterResults.RSI.RSICurrent != 0) {
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.result_rsi);
            linearLayout.addView(stub);
            View s = stub.inflate();

            TextView tvRSICurrent = s.findViewById(R.id.tvRSICurrent);
            TextView tvRSIPrevious = s.findViewById(R.id.tvRSIPrevious);
            TextView tvRSIPercentIncrease = s.findViewById(R.id.tvRSIPercentIncrease);

            tvRSICurrent.setText(Float.toString(filterResults.RSI.RSICurrent));
            tvRSIPrevious.setText(Float.toString(filterResults.RSI.RSIPrevious));
            tvRSIPercentIncrease.setText(Float.toString(filterResults.RSI.RSIPercentIncrease));
        }
        if (filterResults.SMA.SMA3 != 0) {
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.result_sma);
            linearLayout.addView(stub);
            View s = stub.inflate();

            TextView tvSMACurrent = s.findViewById(R.id.tvSMACurrent);
            TextView tvSMAPrevious = s.findViewById(R.id.tvSMAPrevious);
            TextView tvPrevious2 = s.findViewById(R.id.tvPrevious2);
            TextView tvSMASLopeCurrent = s.findViewById(R.id.tvSMASLopeCurrent);
            TextView tvSMASlopePrevious = s.findViewById(R.id.tvSMASlopePrevious);
            TextView tvSMASlopeIncreasePercent = s.findViewById(R.id.tvSMASlopeIncreasePercent);

            tvSMACurrent.setText(Float.toString(filterResults.SMA.SMA3));
            tvSMAPrevious.setText(Float.toString(filterResults.SMA.SMA2));
            tvPrevious2.setText(Float.toString(filterResults.SMA.SMA1));
            tvSMASLopeCurrent.setText(Float.toString(filterResults.SMA.SMASlopeCurrent));
            tvSMASlopePrevious.setText(Float.toString(filterResults.SMA.SMASlopePrevious));
            tvSMASlopeIncreasePercent.setText(Float.toString(filterResults.SMA.SMASlopeIncreasePercent));

        }
        if (filterResults.Volume != null && (filterResults.Volume.AverageVolume != 0 || filterResults.Volume.IncreasingVolume !=0 || filterResults.Volume.VolumeJump != 0)) {
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.result_volume);
            linearLayout.addView(stub);
            View s = stub.inflate();

            TextView tvAverageVolume = s.findViewById(R.id.tvAverageVolume);
            TextView tvIncreasingVolume = s.findViewById(R.id.tvIncreasingVolume);
            TextView tvVolumeJump = s.findViewById(R.id.tvVolumeJump);

            tvAverageVolume.setText(Float.toString(filterResults.Volume.AverageVolume));
            if (filterResults.Volume.IncreasingVolume == 1) {
                tvIncreasingVolume.setText("True");
            } else if (filterResults.Volume.IncreasingVolume == 2) {
                tvIncreasingVolume.setText("False");
            } else {
                tvIncreasingVolume.setText("Not filtered for");
            }
            tvVolumeJump.setText(Float.toString(filterResults.Volume.VolumeJump));

        }
        if (filterResults.Close != null && (filterResults.Close.LastClose != 0 || filterResults.Close.GreaterThanLast != 0)) {
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.result_close);
            linearLayout.addView(stub);
            View s = stub.inflate();

            TextView tvLastClose = s.findViewById(R.id.tvLastClose);
            TextView tvGreaterThanLast = s.findViewById(R.id.tvGreaterThanLast);

            tvLastClose.setText(Float.toString(filterResults.Close.LastClose));
            if (filterResults.Close.GreaterThanLast == 1) {
                tvGreaterThanLast.setText("True");
            } else if (filterResults.Close.GreaterThanLast == 2) {
                tvGreaterThanLast.setText("False");
            } else {
                tvGreaterThanLast.setText("Not filtered for");
            }
        }
    }
}