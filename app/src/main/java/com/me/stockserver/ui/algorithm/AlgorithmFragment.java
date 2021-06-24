package com.me.stockserver.ui.algorithm;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;

import com.google.gson.Gson;
import com.me.stockserver.MainActivity;
import com.me.stockserver.R;
import com.me.stockserver.data.structs.algorithm.Algorithm;
import com.me.stockserver.data.structs.algorithm.filters.ADXCheck;
import com.me.stockserver.data.structs.algorithm.filters.AccumulationDistributionCheck;
import com.me.stockserver.data.structs.algorithm.filters.AverageVolumeCheck;
import com.me.stockserver.data.structs.algorithm.filters.BBCheck;
import com.me.stockserver.data.structs.algorithm.filters.CloseGreaterThanPreviousCheck;
import com.me.stockserver.data.structs.algorithm.filters.EMACheck;
import com.me.stockserver.data.structs.algorithm.filters.FilterCheck;
import com.me.stockserver.data.structs.algorithm.filters.LastCloseCheck;
import com.me.stockserver.data.structs.algorithm.filters.Purchasable;
import com.me.stockserver.data.structs.algorithm.filters.RSICheck;
import com.me.stockserver.data.structs.algorithm.filters.SMACheck;
import com.me.stockserver.data.structs.algorithm.filters.Sell;
import com.me.stockserver.data.structs.algorithm.filters.Viable;
import com.me.stockserver.data.structs.algorithm.filters.VolumeJumpCheck;
import com.me.stockserver.data.structs.sendIns.AlgorithmSendIn;
import com.me.stockserver.data.structs.sendIns.BasicUserSendIn;
import com.me.stockserver.httpRequests.HTTPRequestParams;
import com.me.stockserver.httpRequests.HTTPRequests;

import java.util.ArrayList;

import static com.me.stockserver.MainActivity.algorithm;
import static com.me.stockserver.MainActivity.username;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlgorithmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlgorithmFragment extends Fragment {

    private static View view;
    private static Context c;
    public static Algorithm a;
    public static LinearLayout llViable;
    public static LinearLayout llViableChecks;
    public static LinearLayout llPurchasable;
    public static LinearLayout llPurchasableChecks;
    public static LinearLayout llSell;
    public static LinearLayout llSellChecks;
    public static Button btnStart;
    public static Button btnStop;
    public static Button btnRunViable;
    public static Button btnRunPurchasable;
    public static Button btnRunSell;

    public AlgorithmFragment() {
        // Required empty public constructor
    }

    public static AlgorithmFragment newInstance(String param1, String param2) {
        AlgorithmFragment fragment = new AlgorithmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_algorithm, container, false);
        c = this.getContext();
        llViable = view.findViewById(R.id.llViable);
        llPurchasable = view.findViewById(R.id.llPurchasable);
        llSell = view.findViewById(R.id.llSell);
        llViableChecks = view.findViewById(R.id.llViableChecks);
        llPurchasableChecks = view.findViewById(R.id.llPurchasableChecks);
        llSellChecks = view.findViewById(R.id.llSellChecks);

        btnStart = view.findViewById(R.id.btnStartAlgorithm);
        btnStop = view.findViewById(R.id.btnStopAlgorithm);
        btnRunViable = view.findViewById(R.id.btnRunViable);
        btnRunPurchasable = view.findViewById(R.id.btnRunPurchasable);
        btnRunSell = view.findViewById(R.id.btnRunSell);

        Button btnSave = view.findViewById(R.id.btnSave);

        ImageButton btnDeleteAlgorithm = view.findViewById(R.id.btnDeleteAlgorithm);
        ImageButton btnShowViableFilter = view.findViewById(R.id.btnShowViableFilter);
        ImageButton btnShowPurchasableFilter = view.findViewById(R.id.btnShowPurchasableFilter);
        ImageButton btnShowSellFilter = view.findViewById(R.id.btnShowSellFilter);
        ImageButton btnAddCheckToViable = view.findViewById(R.id.btnAddCheckToViable);
        ImageButton btnAddCheckToPurchasable = view.findViewById(R.id.btnAddCheckToPurchasable);
        ImageButton btnAddChecksToSell = view.findViewById(R.id.btnAddChecksToSell);

        Button btnRunViable = view.findViewById(R.id.btnRunViable);
        Button btnRunPurchasable = view.findViewById(R.id.btnRunPurchasable);
        Button btnRunSell = view.findViewById(R.id.btnRunSell);

        btnDeleteAlgorithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
                basicUserSendIn.Algorithm = algorithm;
                basicUserSendIn.Username = username;
                Gson gson = new Gson();
                String send = gson.toJson(basicUserSendIn);

                HTTPRequestParams rp = new HTTPRequestParams("/delete_algorithm", send, view, c, MainActivity::algorithmsChanged);
                new HTTPRequests().execute(rp);
            }
        });
        btnShowViableFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llViable.getVisibility() == View.VISIBLE) {
                    llViable.setVisibility(View.GONE);
                    btnShowViableFilter.setImageDrawable(c.getResources().getDrawable(android.R.drawable.arrow_up_float));
                } else {
                    llViable.setVisibility(View.VISIBLE);
                    btnShowViableFilter.setImageDrawable(c.getResources().getDrawable(android.R.drawable.arrow_down_float));
                }
            }
        });
        btnShowPurchasableFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llPurchasable.getVisibility() == View.VISIBLE) {
                    llPurchasable.setVisibility(View.GONE);
                    btnShowPurchasableFilter.setImageDrawable(c.getResources().getDrawable(android.R.drawable.arrow_up_float));
                } else {
                    llPurchasable.setVisibility(View.VISIBLE);
                    btnShowPurchasableFilter.setImageDrawable(c.getResources().getDrawable(android.R.drawable.arrow_down_float));
                }
            }
        });
        btnShowSellFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llSell.getVisibility() == View.VISIBLE) {
                    llSell.setVisibility(View.GONE);
                    btnShowSellFilter.setImageDrawable(c.getResources().getDrawable(android.R.drawable.arrow_up_float));
                } else {
                    llSell.setVisibility(View.VISIBLE);
                    btnShowSellFilter.setImageDrawable(c.getResources().getDrawable(android.R.drawable.arrow_down_float));
                }
            }
        });
        btnAddCheckToViable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStub stub = new ViewStub(v.getContext());
                stub.setLayoutResource(R.layout.algorithm_checks);
                llViableChecks.addView(stub);
                View cl = stub.inflate();

                ImageButton btnDeleteCheck = (ImageButton) cl.findViewById(R.id.btnDeleteCheck);
                btnDeleteCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });
                Spinner spIndicator = (Spinner) cl.findViewById(R.id.spIndicator);
                spIndicator.setSelection(0, true);
                spIndicator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String s = spIndicator.getSelectedItem().toString();
                        LinearLayout indicators = cl.findViewById(R.id.indicators);
                        ViewStub stub = getIndicatorLayout(s, cl.getContext());
                        if (stub == null) {
                            return;
                        }
                        indicators.addView(stub);
                        View c2 = stub.inflate();
                        spIndicator.setSelection(0, true);
                        ImageButton btnDeleteIndicator = (ImageButton) c2.findViewById(R.id.btnDeleteIndicator);
                        btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout ll1 = (LinearLayout) v.getParent();
                                TableRow tr = (TableRow) ll1.getParent();
                                LinearLayout ll2 = (LinearLayout) tr.getParent();
                                ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                                LinearLayout ll3 = (LinearLayout) cl.getParent();
                                ll3.removeView(cl);
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Spinner spTimeFrame = (Spinner) cl.findViewById(R.id.spTimeFrame);
                spTimeFrame.setSelection(0, true);
                spTimeFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                       spTimeFrame.setSelection(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
        btnAddCheckToPurchasable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStub stub = new ViewStub(v.getContext());
                stub.setLayoutResource(R.layout.algorithm_checks);
                llPurchasableChecks.addView(stub);
                View cl = stub.inflate();

                ImageButton btnDeleteCheck = (ImageButton) cl.findViewById(R.id.btnDeleteCheck);
                btnDeleteCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });
                Spinner spIndicator = (Spinner) cl.findViewById(R.id.spIndicator);
                spIndicator.setSelection(0, true);
                spIndicator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String s = spIndicator.getSelectedItem().toString();
                        LinearLayout indicators = cl.findViewById(R.id.indicators);
                        ViewStub stub = getIndicatorLayout(s, v.getContext());
                        if (stub == null) {
                            return;
                        }
                        indicators.addView(stub);
                        View c2 = stub.inflate();
                        spIndicator.setSelection(0, true);
                        ImageButton btnDeleteIndicator = (ImageButton) c2.findViewById(R.id.btnDeleteIndicator);
                        btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout ll1 = (LinearLayout) v.getParent();
                                TableRow tr = (TableRow) ll1.getParent();
                                LinearLayout ll2 = (LinearLayout) tr.getParent();
                                ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                                LinearLayout ll3 = (LinearLayout) cl.getParent();
                                ll3.removeView(cl);
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Spinner spTimeFrame = (Spinner) cl.findViewById(R.id.spTimeFrame);
                spTimeFrame.setSelection(0, true);
                spTimeFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        spTimeFrame.setSelection(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
        btnAddChecksToSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStub stub = new ViewStub(v.getContext());
                stub.setLayoutResource(R.layout.algorithm_checks);
                llSellChecks.addView(stub);
                View cl = stub.inflate();

                ImageButton btnDeleteCheck = (ImageButton) cl.findViewById(R.id.btnDeleteCheck);
                btnDeleteCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });
                Spinner spIndicator = (Spinner) cl.findViewById(R.id.spIndicator);
                spIndicator.setSelection(0, true);
                spIndicator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String s = spIndicator.getSelectedItem().toString();
                        LinearLayout indicators = cl.findViewById(R.id.indicators);
                        ViewStub stub = getIndicatorLayout(s, v.getContext());
                        if (stub == null) {
                            return;
                        }
                        indicators.addView(stub);
                        View c2 = stub.inflate();
                        spIndicator.setSelection(0, true);
                        ImageButton btnDeleteIndicator = (ImageButton) c2.findViewById(R.id.btnDeleteIndicator);
                        btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout ll1 = (LinearLayout) v.getParent();
                                TableRow tr = (TableRow) ll1.getParent();
                                LinearLayout ll2 = (LinearLayout) tr.getParent();
                                ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                                LinearLayout ll3 = (LinearLayout) cl.getParent();
                                ll3.removeView(cl);
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Spinner spTimeFrame = (Spinner) cl.findViewById(R.id.spTimeFrame);
                spTimeFrame.setSelection(0, true);
                spTimeFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        spTimeFrame.setSelection(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetButtonsEnabled(true);
                BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
                basicUserSendIn.Algorithm = algorithm;
                basicUserSendIn.Username = username;
                Gson gson = new Gson();
                String send = gson.toJson(basicUserSendIn);

                HTTPRequestParams rp = new HTTPRequestParams("/start_algorithm", send, view, c, AlgorithmFragment::ButtonCallBack);
                new HTTPRequests().execute(rp);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetButtonsEnabled(false);

                BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
                basicUserSendIn.Algorithm = algorithm;
                basicUserSendIn.Username = username;
                Gson gson = new Gson();
                String send = gson.toJson(basicUserSendIn);

                HTTPRequestParams rp = new HTTPRequestParams("/stop_algorithm", send, view, c, AlgorithmFragment::ButtonCallBack);
                new HTTPRequests().execute(rp);
            }
        });
        btnRunViable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
                basicUserSendIn.Algorithm = algorithm;
                basicUserSendIn.Username = username;
                Gson gson = new Gson();
                String send = gson.toJson(basicUserSendIn);

                HTTPRequestParams rp = new HTTPRequestParams("/run_viable", send, view, c, AlgorithmFragment::ButtonCallBack);
                new HTTPRequests().execute(rp);
            }
        });
        btnRunPurchasable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
                basicUserSendIn.Algorithm = algorithm;
                basicUserSendIn.Username = username;
                Gson gson = new Gson();
                String send = gson.toJson(basicUserSendIn);

                HTTPRequestParams rp = new HTTPRequestParams("/run_purchasable", send, view, c, AlgorithmFragment::ButtonCallBack);
                new HTTPRequests().execute(rp);
            }
        });
        btnRunSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
                basicUserSendIn.Algorithm = algorithm;
                basicUserSendIn.Username = username;
                Gson gson = new Gson();
                String send = gson.toJson(basicUserSendIn);

                HTTPRequestParams rp = new HTTPRequestParams("/run_sell", send, view, c, AlgorithmFragment::ButtonCallBack);
                new HTTPRequests().execute(rp);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Algorithm algo = getAlgorithmFromScreen();
                AlgorithmSendIn algorithmSendIn = new AlgorithmSendIn();
                algorithmSendIn.Username = username;
                algorithmSendIn.Algo = algo;
                Gson gson = new Gson();
                String send = gson.toJson(algorithmSendIn);

                HTTPRequestParams rp = new HTTPRequestParams("/update_algorithm", send, view, c, MainActivity::algorithmsChanged);
                new HTTPRequests().execute(rp);
            }
        });

        getAlgorithm();
        // Inflate the layout for this fragment
        return view;
    }
    public static void getAlgorithm () {
        BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
        basicUserSendIn.Algorithm = algorithm;
        basicUserSendIn.Username = username;
        Gson gson = new Gson();
        String send = gson.toJson(basicUserSendIn);

        HTTPRequestParams rp = new HTTPRequestParams("/send_algorithm", send, view, c, AlgorithmFragment::addAlgorithmToScreen);
        new HTTPRequests().execute(rp);
    }

    public static void addAlgorithmToScreen (HTTPRequestParams ret) {
        if (ret.ret == null) {
            return;
        }
        Gson gson = new Gson();
        a = gson.fromJson(ret.ret, Algorithm.class);
        if (a == null){
            return;
        }
        llViableChecks.removeAllViews();
        llPurchasableChecks.removeAllViews();
        llSellChecks.removeAllViews();

        EditText etName = ret.v.findViewById(R.id.etName);
        SwitchCompat sRunning = ret.v.findViewById(R.id.sRunning);
        SwitchCompat sRunOnStart = ret.v.findViewById(R.id.sRunOnStart);
        SwitchCompat sStraightToMonitoring = ret.v.findViewById(R.id.sStraightToMonitoring);

        etName.setText(a.Name);
        sRunning.setChecked(a.IsRunning);
        sRunOnStart.setChecked(a.RunOnStart);
        sStraightToMonitoring.setChecked(a.StraightToMonitoring);

        if (a.IsRunning == true){
            SetButtonsEnabled(true);
        } else {
            SetButtonsEnabled(false);
        }

        setFilters(a.ViableFilter.Checks, llViableChecks, c);
        setFilters(a.PurchasableFilter.Checks, llPurchasableChecks, c);
        setFilters(a.SellFilter.Checks, llSellChecks, c);

        EditText etCheckViableHrStart = ret.v.findViewById(R.id.etCheckViableHrStart);
        etCheckViableHrStart.setText(String.valueOf(a.CheckViableHrStart));

        EditText etCheckViableMinStart = ret.v.findViewById(R.id.etCheckViableMinStart);
        etCheckViableMinStart.setText(String.valueOf(a.CheckViableMinStart));

        EditText etCheckViableHrIncrement = ret.v.findViewById(R.id.etCheckViableHrIncrement);
        etCheckViableHrIncrement.setText(String.valueOf(a.CheckViableHrIncrement));

        EditText etCheckViableMinIncrement = ret.v.findViewById(R.id.etCheckViableMinIncrement);
        etCheckViableMinIncrement.setText(String.valueOf(a.CheckViableMinIncrement));

        EditText etCheckSellHrStart = ret.v.findViewById(R.id.etCheckSellHrStart);
        etCheckSellHrStart.setText(String.valueOf(a.CheckSellHrStart));

        EditText etCheckSellMinStart = ret.v.findViewById(R.id.etCheckSellMinStart);
        etCheckSellMinStart.setText(String.valueOf(a.CheckSellMinStart));

        EditText etCheckSellHrIncrement = ret.v.findViewById(R.id.etCheckSellHrIncrement);
        etCheckSellHrIncrement.setText(String.valueOf(a.CheckSellHrIncrement));

        EditText etCheckSellMinIncrement = ret.v.findViewById(R.id.etCheckSellMinIncrement);
        etCheckSellMinIncrement.setText(String.valueOf(a.CheckSellMinIncrement));

        Button btnStart = ret.v.findViewById(R.id.btnStartAlgorithm);
        if (a.IsRunning == true) {
            btnStart.setEnabled(false);
        }
    }

    public static Algorithm getAlgorithmFromScreen () {
        EditText etName = view.findViewById(R.id.etName);
        SwitchCompat sRunning = view.findViewById(R.id.sRunning);
        SwitchCompat sRunOnStart = view.findViewById(R.id.sRunOnStart);
        SwitchCompat sStraightToMonitoring = view.findViewById(R.id.sStraightToMonitoring);

        EditText etCheckViableHrStart =  view.findViewById(R.id.etCheckViableHrStart);
        EditText etCheckViableMinStart =  view.findViewById(R.id.etCheckViableMinStart);
        EditText etCheckViableHrIncrement =  view.findViewById(R.id.etCheckViableHrIncrement);
        EditText etCheckViableMinIncrement =  view.findViewById(R.id.etCheckViableMinIncrement);
        EditText etCheckSellHrStart =  view.findViewById(R.id.etCheckSellHrStart);
        EditText etCheckSellMinStart =  view.findViewById(R.id.etCheckSellMinStart);
        EditText etCheckSellHrIncrement =  view.findViewById(R.id.etCheckSellHrIncrement);
        EditText etCheckSellMinIncrement =  view.findViewById(R.id.etCheckSellMinIncrement);

        Algorithm algo = new Algorithm();
        algo.UniqueID = algorithm;
        algo.UserName = username;
        algo.Name = etName.getText().toString();
        if (sRunning.isChecked()) {
            algo.IsRunning = true;
        } else {
            algo.IsRunning = false;
        }
        if (sRunOnStart.isChecked()) {
            algo.RunOnStart = true;
        } else {
            algo.RunOnStart = false;
        }
        if (sStraightToMonitoring.isChecked()) {
            algo.StraightToMonitoring = true;
        } else {
            algo.StraightToMonitoring = false;
        }

        algo.ViableFilter = new Viable();
        algo.ViableFilter.Checks = new ArrayList<>();
        algo.ViableFilter.Checks = getFilters(llViableChecks, view);

        algo.PurchasableFilter = new Purchasable();
        algo.PurchasableFilter.Checks = new ArrayList<>();
        algo.PurchasableFilter.Checks = getFilters(llPurchasableChecks, view);

        algo.SellFilter = new Sell();
        algo.SellFilter.Checks = new ArrayList<>();
        algo.SellFilter.Checks = getFilters(llSellChecks, view);

        try {
            algo.CheckViableHrStart = Integer.parseInt(etCheckViableHrStart.getText().toString());
            algo.CheckViableMinStart = Integer.parseInt(etCheckViableMinStart.getText().toString());
            algo.CheckViableHrIncrement = Integer.parseInt(etCheckViableHrIncrement.getText().toString());
            algo.CheckViableMinIncrement = Integer.parseInt(etCheckViableMinIncrement.getText().toString());
            algo.CheckSellHrStart = Integer.parseInt(etCheckSellHrStart.getText().toString());
            algo.CheckSellMinStart = Integer.parseInt(etCheckSellMinStart.getText().toString());
            algo.CheckSellHrIncrement = Integer.parseInt(etCheckSellHrIncrement.getText().toString());
            algo.CheckSellMinIncrement = Integer.parseInt(etCheckSellMinIncrement.getText().toString());
        } catch (Exception e) {

        }
        return algo;
    }

    public static void setFilters(ArrayList<FilterCheck> checks, LinearLayout linearLayout, Context c) {
        if (checks == null) {
            return;
        }
        for (int i = 0; i < checks.size(); i++) {
            FilterCheck check = checks.get(i);
            ViewStub stub = new ViewStub(c);
            stub.setLayoutResource(R.layout.algorithm_checks);
            linearLayout.addView(stub);
            View s = stub.inflate();

            EditText etPeriod = s.findViewById(R.id.etPeriod);
            etPeriod.setText(String.valueOf(check.Period));

            ImageButton btnDeleteCheck = (ImageButton) s.findViewById(R.id.btnDeleteCheck);
            btnDeleteCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll1 = (LinearLayout) v.getParent();
                    TableRow tr = (TableRow) ll1.getParent();
                    LinearLayout ll2 = (LinearLayout) tr.getParent();
                    ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                    LinearLayout ll3 = (LinearLayout) cl.getParent();
                    ll3.removeView(cl);
                }
            });

            Spinner spIndicator = (Spinner) s.findViewById(R.id.spIndicator);
            spIndicator.setSelection(0, true);
            spIndicator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String s = spIndicator.getSelectedItem().toString();
                    LinearLayout ll = (LinearLayout) parentView.getParent();
                    TableRow tr = (TableRow) ll.getParent();
                    LinearLayout ll2 = (LinearLayout) tr.getParent();
                    LinearLayout indicators = ll2.findViewById(R.id.indicators);
                    ViewStub stub = getIndicatorLayout(s, linearLayout.getContext());
                    if (stub == null) {
                        return;
                    }
                    indicators.addView(stub);
                    View c2 = stub.inflate();
                    spIndicator.setSelection(0, true);
                    ImageButton btnDeleteIndicator = (ImageButton) c2.findViewById(R.id.btnDeleteIndicator);
                    btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LinearLayout ll1 = (LinearLayout) v.getParent();
                            TableRow tr = (TableRow) ll1.getParent();
                            LinearLayout ll2 = (LinearLayout) tr.getParent();
                            ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                            LinearLayout ll3 = (LinearLayout) cl.getParent();
                            ll3.removeView(cl);
                        }
                    });
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Spinner spTimeFrame = s.findViewById(R.id.spTimeFrame);
            switch (check.TimePeriod) {
                case "oneWeek":
                    spTimeFrame.setSelection(1);
                    break;
                case "oneDay":
                    spTimeFrame.setSelection(2);
                    break;
                case "fourHour":
                    spTimeFrame.setSelection(3);
                    break;
                case "oneHour":
                    spTimeFrame.setSelection(4);
                    break;
            }

            if (check.AccumulationDistribution != null && check.AccumulationDistribution.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_accumulation_distribution);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                if (check.AccumulationDistribution.Increasing != 0) {
                    SwitchCompat sAccumulationDistributionIncreasing = s2.findViewById(R.id.sAccumulationDistributionIncreasing);
                    CheckBox enabled = s2.findViewById(R.id.cbAccumulationDistributionIncreasing);
                    enabled.setChecked(true);
                    if (check.AccumulationDistribution.Increasing == 1) {
                        sAccumulationDistributionIncreasing.setChecked(true);
                    }
                }

            }
            if (check.ADX != null && check.ADX.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_adx);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                if (check.ADX.ADXIncreasing != 0) {
                    SwitchCompat sADXIncreasing = s2.findViewById(R.id.sADXIncreasing);
                    CheckBox enabled = s2.findViewById(R.id.cbADXIncreasing);
                    enabled.setChecked(true);
                    if (check.ADX.ADXIncreasing == 1) {
                        sADXIncreasing.setChecked(true);
                    }
                }
                if (check.ADX.PDIIncreasing != 0) {
                    SwitchCompat sPDIIncreasing = s2.findViewById(R.id.sPDIIncreasing);
                    CheckBox enabled = s2.findViewById(R.id.cbPDIIncreasing);
                    enabled.setChecked(true);
                    if (check.ADX.PDIIncreasing == 1) {
                        sPDIIncreasing.setChecked(true);
                    }
                }
                if (check.ADX.MDIIncreasing != 0) {
                    SwitchCompat sMDIIncreasing = s2.findViewById(R.id.sMDIIncreasing);
                    CheckBox enabled = s2.findViewById(R.id.cbMDIIncreasing);
                    enabled.setChecked(true);
                    if (check.ADX.MDIIncreasing == 1) {
                        sMDIIncreasing.setChecked(true);
                    }
                }

            }
            if (check.AverageVolume != null && check.AverageVolume.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_average_volume);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                EditText etAverageVolumeGreaterThan = s2.findViewById(R.id.etAverageVolumeGreaterThan);
                etAverageVolumeGreaterThan.setText(Float.toString(check.AverageVolume.GreaterThan));

            }
            if (check.BB != null && check.BB.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_bb);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                EditText etBBGreaterThan = s2.findViewById(R.id.etBBGreaterThan);
                etBBGreaterThan.setText(Float.toString(check.BB.GreaterThan));

                EditText etBBLessThan = s2.findViewById(R.id.etBBLessThan);
                etBBLessThan.setText(Float.toString(check.BB.LessThan));

            }
            if (check.EMA != null && check.EMA.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_ema);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });
                if (check.EMA.Increasing != 0) {
                    SwitchCompat sEMAIncreasing = s2.findViewById(R.id.sEMAIncreasing);
                    CheckBox enabled = s2.findViewById(R.id.cbEMAIncresaing);
                    enabled.setChecked(true);
                    if (check.EMA.Increasing == 1) {
                        sEMAIncreasing.setChecked(true);
                    }
                }
                if (check.EMA.GreaterThan0 != 0) {
                    SwitchCompat sEMAGreaterThan0 = s2.findViewById(R.id.sEMAGreaterThan0);
                    CheckBox enabled = s2.findViewById(R.id.cbEMAGreaterThan0);
                    enabled.setChecked(true);
                    if (check.EMA.GreaterThan0 == 1) {
                        sEMAGreaterThan0.setChecked(true);
                    }
                }
                if (check.EMA.GreaterThanSMA != 0) {
                    SwitchCompat sGreaterThanSMA = s2.findViewById(R.id.sGreaterThanSMA);
                    CheckBox enabled = s2.findViewById(R.id.cEMAGreaterThanSMA);
                    enabled.setChecked(true);
                    if (check.EMA.GreaterThanSMA == 1) {
                        sGreaterThanSMA.setChecked(true);
                    }
                }
            }
            if (check.LastClose != null && check.LastClose.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_last_close);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                EditText etLastCloseGreaterThan = s2.findViewById(R.id.etLastCloseGreaterThan);
                etLastCloseGreaterThan.setText(Float.toString(check.LastClose.GreaterThan));

                EditText etLastCloseLessThan = s2.findViewById(R.id.etLastCloseLessThan);
                etLastCloseLessThan.setText(Float.toString(check.LastClose.LessThan));
            }
            if (check.SMA != null && check.SMA.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_sma);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });
                if (check.SMA.Increasing != 0) {
                    SwitchCompat sSMAIncreasing = s2.findViewById(R.id.sSMAIncreasing);
                    CheckBox enabled = s2.findViewById(R.id.cbSMAIncreasing);
                    enabled.setChecked(true);
                    if (check.SMA.Increasing == 1) {
                        sSMAIncreasing.setChecked(true);
                    }
                }
                if (check.SMA.GreaterThan0 != 0) {
                    SwitchCompat sSMAGreaterThan0 = s2.findViewById(R.id.sSMAGreaterThan0);
                    CheckBox enabled = s2.findViewById(R.id.cbSMAGreaterThan0);
                    enabled.setChecked(true);
                    if (check.SMA.GreaterThan0 == 1) {
                        sSMAGreaterThan0.setChecked(true);
                    }
                }
            }
            if (check.RSI != null && check.RSI.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_rsi);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                if (check.RSI.Increasing != 0) {
                    SwitchCompat sRSIIncreasing = s2.findViewById(R.id.sRSIIncreasing);
                    CheckBox enabled = s2.findViewById(R.id.cbRSIIncreasing);
                    enabled.setChecked(true);
                    if (check.RSI.Increasing == 1) {
                        sRSIIncreasing.setChecked(true);
                    }
                }

                EditText etRSIGreaterThan = s2.findViewById(R.id.etRSIGreaterThan);
                etRSIGreaterThan.setText(Float.toString(check.RSI.GreaterThan));

                EditText etRSILessThan = s2.findViewById(R.id.etRSILessThan);
                etRSILessThan.setText(Float.toString(check.RSI.LessThan));
            }
            if (check.VolumeJump != null && check.VolumeJump.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_volume_jump);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                EditText etMultiplier = s2.findViewById(R.id.etMultiplier);
                etMultiplier.setText(Float.toString(check.VolumeJump.Multiplier));
            }
            if (check.CloseGreaterThanPrevious != null && check.CloseGreaterThanPrevious.Set == true) {
                LinearLayout indicators = s.findViewById(R.id.indicators);
                ViewStub stub2 = new ViewStub(c);
                stub2.setLayoutResource(R.layout.indicator_close_greater_than_previous);

                indicators.addView(stub2);
                View s2 = stub2.inflate();
                ImageButton btnDeleteIndicator = (ImageButton) s2.findViewById(R.id.btnDeleteIndicator);
                btnDeleteIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ll1 = (LinearLayout) v.getParent();
                        TableRow tr = (TableRow) ll1.getParent();
                        LinearLayout ll2 = (LinearLayout) tr.getParent();
                        ConstraintLayout cl = (ConstraintLayout) ll2.getParent();
                        LinearLayout ll3 = (LinearLayout) cl.getParent();
                        ll3.removeView(cl);
                    }
                });

                if (check.CloseGreaterThanPrevious.Greater != 0) {
                    SwitchCompat sCloseGreaterThanPrevious = s2.findViewById(R.id.sCloseGreaterThanPrevious);
                    CheckBox enabled = s2.findViewById(R.id.cbCloseGreaterThanPrevious);
                    enabled.setChecked(true);
                    if (check.CloseGreaterThanPrevious.Greater == 1) {
                        sCloseGreaterThanPrevious.setChecked(true);
                    }
                }

            }
        }
    }

    public static ArrayList<FilterCheck> getFilters (LinearLayout linearLayout, View v) {
        ArrayList<FilterCheck> checks = new ArrayList<>();

        int filters = linearLayout.getChildCount();

        if (filters > 0) {
            for (int i = 0; i < filters; i++){
                FilterCheck check = new FilterCheck();
                ConstraintLayout constraintLayout = (ConstraintLayout) linearLayout.getChildAt(i);

                EditText etPeriod = constraintLayout.findViewById(R.id.etPeriod);
                try {
                    check.Period = Integer.parseInt(etPeriod.getText().toString());
                } catch (Exception e) {

                }
                Spinner spTimeFrame = constraintLayout.findViewById(R.id.spTimeFrame);
                int pos = spTimeFrame.getSelectedItemPosition();
                switch (pos) {
                    case 1:
                        check.TimePeriod = "oneWeek";
                        break;
                    case 2:
                        check.TimePeriod = "oneDay";
                        break;
                    case 3:
                        check.TimePeriod = "fourHour";
                        break;
                    case 4:
                        check.TimePeriod = "oneHour";
                        break;
                }

                ConstraintLayout indicator_accumulation_distribution = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_accumulation_distribution);
                ConstraintLayout indicator_adx = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_adx);
                ConstraintLayout indicator_average_volume = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_average_volume);
                ConstraintLayout indicator_bb = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_bb);
                ConstraintLayout indicator_ema = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_ema);
                ConstraintLayout indicator_last_close = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_last_close);
                ConstraintLayout indicator_rsi = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_rsi);
                ConstraintLayout indicator_sma = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_sma);
                ConstraintLayout indicator_close_greater_than_previous = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_close_greater_than_previous);
                ConstraintLayout indicator_volume_jump = (ConstraintLayout) constraintLayout.findViewById(R.id.indicator_volume_jump);

                if (indicator_accumulation_distribution != null) {
                    check.AccumulationDistribution = new AccumulationDistributionCheck();
                    check.AccumulationDistribution.Set = true;
                    SwitchCompat sAccumulationDistributionIncreasing = constraintLayout.findViewById(R.id.sAccumulationDistributionIncreasing);
                    CheckBox enabled = constraintLayout.findViewById(R.id.cbAccumulationDistributionIncreasing);
                    if (enabled.isChecked()) {
                        if (sAccumulationDistributionIncreasing.isChecked()) {
                            check.AccumulationDistribution.Increasing = 1;
                        } else {
                            check.AccumulationDistribution.Increasing = 2;
                        }
                    } else {
                        check.AccumulationDistribution.Increasing = 0;
                    }
                }
                if (indicator_adx != null) {
                    check.ADX = new ADXCheck();
                    check.ADX.Set = true;
                    SwitchCompat sADXIncreasing = constraintLayout.findViewById(R.id.sADXIncreasing);
                    CheckBox enabled = constraintLayout.findViewById(R.id.cbADXIncreasing);
                    if (enabled.isChecked()) {
                        if (sADXIncreasing.isChecked()) {
                            check.ADX.ADXIncreasing = 1;
                        } else {
                            check.ADX.ADXIncreasing = 2;
                        }
                    } else {
                        check.ADX.ADXIncreasing = 0;
                    }

                    SwitchCompat sPDIIncreasing = constraintLayout.findViewById(R.id.sPDIIncreasing);
                    enabled = constraintLayout.findViewById(R.id.cbPDIIncreasing);
                    if (enabled.isChecked()) {
                        if (sPDIIncreasing.isChecked()) {
                            check.ADX.PDIIncreasing = 1;
                        } else {
                            check.ADX.PDIIncreasing = 2;
                        }
                    } else {
                        check.ADX.PDIIncreasing = 0;
                    }

                    SwitchCompat sMDIIncreasing = constraintLayout.findViewById(R.id.sMDIIncreasing);
                    enabled = constraintLayout.findViewById(R.id.cbMDIIncreasing);
                    if (enabled.isChecked()) {
                        if (sMDIIncreasing.isChecked()) {
                            check.ADX.MDIIncreasing = 1;
                        } else {
                            check.ADX.MDIIncreasing = 2;
                        }
                    } else {
                        check.ADX.MDIIncreasing = 0;
                    }
                }
                if (indicator_average_volume != null) {
                    check.AverageVolume = new AverageVolumeCheck();
                    check.AverageVolume.Set = true;
                    EditText etAverageVolumeGreaterThan = constraintLayout.findViewById(R.id.etAverageVolumeGreaterThan);
                    try {
                        check.AverageVolume.GreaterThan = Float.parseFloat(etAverageVolumeGreaterThan.getText().toString());
                    } catch (Exception e) {

                    }
                }
                if (indicator_bb != null) {
                    check.BB = new BBCheck();
                    check.BB.Set = true;
                    EditText etBBGreaterThan = constraintLayout.findViewById(R.id.etBBGreaterThan);
                    EditText etBBLessThan = constraintLayout.findViewById(R.id.etBBLessThan);

                    try {
                        check.BB.GreaterThan = Float.parseFloat(etBBGreaterThan.getText().toString());
                        check.BB.LessThan = Float.parseFloat((etBBLessThan.getText().toString()));
                    } catch (Exception e) {

                    }
                }
                if (indicator_ema != null) {
                    check.EMA = new EMACheck();
                    check.EMA.Set = true;
                    SwitchCompat sEMAIncreasing = constraintLayout.findViewById(R.id.sEMAIncreasing);
                    CheckBox enabled = constraintLayout.findViewById(R.id.cbEMAIncresaing);
                    if (enabled.isChecked()) {
                        if (sEMAIncreasing.isChecked()) {
                            check.EMA.Increasing = 1;
                        } else {
                            check.EMA.Increasing = 2;
                        }
                    } else {
                        check.EMA.Increasing = 0;
                    }

                    SwitchCompat sEMAGreaterThan0 = constraintLayout.findViewById(R.id.sEMAGreaterThan0);
                    enabled = constraintLayout.findViewById(R.id.cbEMAGreaterThan0);
                    if (enabled.isChecked()) {
                        if (sEMAGreaterThan0.isChecked()) {
                            check.EMA.GreaterThan0 = 1;
                        } else {
                            check.EMA.GreaterThan0 = 2;
                        }
                    } else {
                        check.EMA.GreaterThan0 = 0;
                    }

                    SwitchCompat sGreaterThanSMA = constraintLayout.findViewById(R.id.sGreaterThanSMA);
                    enabled = constraintLayout.findViewById(R.id.cEMAGreaterThanSMA);
                    if (enabled.isChecked()) {
                        if (sGreaterThanSMA.isChecked()) {
                            check.EMA.GreaterThanSMA = 1;
                        } else {
                            check.EMA.GreaterThanSMA = 2;
                        }
                    } else {
                        check.EMA.GreaterThanSMA = 0;
                    }
                }
                if (indicator_last_close != null) {
                    check.LastClose = new LastCloseCheck();
                    check.LastClose.Set = true;
                    EditText etLastCloseGreaterThan = constraintLayout.findViewById(R.id.etLastCloseGreaterThan);
                    EditText etLastCloseLessThan = constraintLayout.findViewById(R.id.etLastCloseLessThan);

                    try {
                        check.LastClose.GreaterThan = Float.parseFloat(etLastCloseGreaterThan.getText().toString());
                        check.LastClose.LessThan = Float.parseFloat(etLastCloseLessThan.getText().toString());
                    } catch (Exception e) {

                    }
                }
                if (indicator_sma != null) {
                    check.SMA = new SMACheck();
                    check.SMA.Set = true;
                    SwitchCompat sSMAIncreasing = constraintLayout.findViewById(R.id.sSMAIncreasing);
                    CheckBox enabled = constraintLayout.findViewById(R.id.cbSMAIncreasing);
                    if (enabled.isChecked()) {
                        if (sSMAIncreasing.isChecked()) {
                            check.SMA.Increasing = 1;
                        } else {
                            check.SMA.Increasing = 2;
                        }
                    } else {
                        check.SMA.Increasing = 0;
                    }

                    SwitchCompat sSMAGreaterThan0 = constraintLayout.findViewById(R.id.sSMAGreaterThan0);
                    enabled = constraintLayout.findViewById(R.id.cbSMAGreaterThan0);
                    if (enabled.isChecked()) {
                        if (sSMAGreaterThan0.isChecked()) {
                            check.SMA.GreaterThan0 = 1;
                        } else {
                            check.SMA.GreaterThan0 = 2;
                        }
                    } else {
                        check.SMA.GreaterThan0 = 0;
                    }
                }
                if (indicator_rsi != null) {
                    check.RSI = new RSICheck();
                    check.RSI.Set = true;
                    SwitchCompat sRSIIncreasing = constraintLayout.findViewById(R.id.sRSIIncreasing);
                    CheckBox enabled = constraintLayout.findViewById(R.id.cbRSIIncreasing);
                    EditText etRSIGreaterThan = constraintLayout.findViewById(R.id.etRSIGreaterThan);
                    EditText etRSILessThan = constraintLayout.findViewById(R.id.etRSILessThan);
                    if (enabled.isChecked()) {
                        if (sRSIIncreasing.isChecked()) {
                            check.RSI.Increasing = 1;
                        } else {
                            check.RSI.Increasing = 2;
                        }
                    } else {
                        check.RSI.Increasing = 0;
                    }
                    try {
                        check.RSI.GreaterThan = Float.parseFloat(etRSIGreaterThan.getText().toString());
                        check.RSI.LessThan = Float.parseFloat(etRSILessThan.getText().toString());
                    } catch (Exception e) {

                    }
                }
                if (indicator_close_greater_than_previous != null){
                    check.CloseGreaterThanPrevious = new CloseGreaterThanPreviousCheck();
                    check.CloseGreaterThanPrevious.Set = true;
                    SwitchCompat sCloseGreaterThanPrevious = constraintLayout.findViewById(R.id.sCloseGreaterThanPrevious);
                    CheckBox enabled = constraintLayout.findViewById(R.id.cbCloseGreaterThanPrevious);
                    if (enabled.isChecked()) {
                        if (sCloseGreaterThanPrevious.isChecked()) {
                            check.CloseGreaterThanPrevious.Greater = 1;
                        } else {
                            check.CloseGreaterThanPrevious.Greater = 2;
                        }
                    } else {
                        check.CloseGreaterThanPrevious.Greater = 0;
                    }
                }
                if (indicator_volume_jump != null) {
                    check.VolumeJump = new VolumeJumpCheck();
                    check.VolumeJump.Set = true;
                    EditText etMultiplier = constraintLayout.findViewById(R.id.etMultiplier);
                    try {
                        check.VolumeJump.Multiplier = Float.parseFloat(etMultiplier.getText().toString());
                    } catch (Exception e) {

                    }
                }
                checks.add(check);
            }
        }

        return checks;
    }

    public static ViewStub getIndicatorLayout (String indicator, Context c) {
        ViewStub stub = new ViewStub(c);

        switch (indicator) {
            case "AccumulationDistribution":
                stub.setLayoutResource(R.layout.indicator_accumulation_distribution);
                break;
            case "ADX":
                stub.setLayoutResource(R.layout.indicator_adx);
                break;
            case "AverageVolume":
                stub.setLayoutResource(R.layout.indicator_average_volume);
                break;
            case "Bollinger Bands":
                stub.setLayoutResource(R.layout.indicator_bb);
                break;
            case "CloseGreaterThanPrevious":
                stub.setLayoutResource(R.layout.indicator_close_greater_than_previous);
                break;
            case "LastClose":
                stub.setLayoutResource(R.layout.indicator_last_close);
                break;
            case "EMA":
                stub.setLayoutResource(R.layout.indicator_ema);
                break;
            case "RSI":
                stub.setLayoutResource(R.layout.indicator_rsi);
                break;
            case "SMA":
                stub.setLayoutResource(R.layout.indicator_sma);
                break;
            case "VolumeJump":
                stub.setLayoutResource(R.layout.indicator_volume_jump);
                break;
            default:
                return null;
        }

        return stub;
    }

    public static void ButtonCallBack (HTTPRequestParams ret) {
        getAlgorithm();
    }
    public static void SetButtonsEnabled (Boolean running) {
        if (running == true) {
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            btnRunViable.setEnabled(true);
            btnRunPurchasable.setEnabled(true);
            btnRunSell.setEnabled(true);
        } else {
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            btnRunViable.setEnabled(false);
            btnRunPurchasable.setEnabled(false);
            btnRunSell.setEnabled(false);

        }
    }

}