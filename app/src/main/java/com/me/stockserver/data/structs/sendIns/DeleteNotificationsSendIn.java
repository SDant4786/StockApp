package com.me.stockserver.data.structs.sendIns;

import com.me.stockserver.data.structs.Notification;

import java.util.ArrayList;

public class DeleteNotificationsSendIn {
    public String UserName;
    public ArrayList<Notification> Notifications;
    public int Algorithm;
}
