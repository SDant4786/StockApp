package com.me.stockserver.ui.notifications;

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
import com.me.stockserver.data.structs.sendIns.BasicUserSendIn;
import com.me.stockserver.data.structs.sendIns.DeleteNotificationsSendIn;
import com.me.stockserver.httpRequests.HTTPRequestParams;
import com.me.stockserver.httpRequests.HTTPRequests;
import com.me.stockserver.ui.StockDisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.me.stockserver.MainActivity.algorithm;
import static com.me.stockserver.MainActivity.username;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private static View view;
    private static Context c;
    public static ArrayList<LinearLayout> notifications;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        c = this.getContext();
        notifications = new ArrayList<>();

        getNotifications();

        FloatingActionButton btnSoldStocks = view.findViewById(R.id.fltbtnSoldStocks);
        btnSoldStocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Notification> deleteNotifications = new ArrayList<>();
                for (int i = 0; i < notifications.size() ; i++) {
                    TableRow tr = (TableRow) notifications.get(i).getChildAt(1);
                    LinearLayout ll = (LinearLayout) tr.getChildAt(1);
                    CheckBox cb = (CheckBox) ll.getChildAt(0);
                    if (cb.isChecked() == true) {
                        deleteNotifications.add((Notification) notifications.get(i).getTag());
                    }
                }
                if (deleteNotifications.size() != 0) {
                    DeleteNotificationsSendIn deleteNotificationsSendIn = new DeleteNotificationsSendIn();
                    deleteNotificationsSendIn.UserName = username;
                    deleteNotificationsSendIn.Notifications = deleteNotifications;
                    deleteNotificationsSendIn.Algorithm = algorithm;
                    Gson gson = new Gson();
                    String send = gson.toJson(deleteNotificationsSendIn);
                    HTTPRequestParams rp = new HTTPRequestParams("/delete_notifications",
                            send,
                            c,
                            NotificationsFragment::sendSoldStocks,
                            "");
                    new HTTPRequests().execute(rp);
                    getNotifications();
                }
            }
        });
        return view;
    }

    public static void getNotifications() {
        Gson gson = new Gson();
        BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
        basicUserSendIn.Username = username;
        basicUserSendIn.Algorithm = algorithm;

        String send = gson.toJson(basicUserSendIn);

        HTTPRequestParams rp = new HTTPRequestParams("/send_notifications", send, view, c, NotificationsFragment::addNotificationsToNotificationsFragment);
        new HTTPRequests().execute(rp);
    }

    public static void addNotificationsToNotificationsFragment(HTTPRequestParams ret) {
        if (ret.ret == null) {
            return;
        }
        notifications = new ArrayList<>();
        ArrayList<Notification> n;
        Gson gson = new Gson();
        LinearLayout scrollNotifications = ret.v.findViewById(R.id.scrollNotifications);
        scrollNotifications.removeAllViews();

        TypeToken<ArrayList<Notification>> token = new TypeToken<ArrayList<Notification>>() {};
        n = gson.fromJson(ret.ret, token.getType());

        if (n == null) {
            return;
        }
        for (int i = 0; i < n.size(); i++) {
            notifications.add(StockDisplay.CreateNotificationDisplay(n.get(i), ret.c));
            scrollNotifications.addView(notifications.get(i));
        }
    }

    public static void sendSoldStocks (HTTPRequestParams ret) {
        getNotifications();
    }
}