package com.me.stockserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.me.stockserver.data.structs.sendIns.BasicUserSendIn;
import com.me.stockserver.data.structs.sendIns.UserAlgorithm;
import com.me.stockserver.httpRequests.HTTPRequestParams;
import com.me.stockserver.httpRequests.HTTPRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String token;
    public static String username;
    public static int algorithm;
    private BroadcastReceiver broadcastReceiver;
    public static AlertDialog alertDialog;
    private static DrawerLayout mDrawer;
    private static Toolbar toolbar;
    private static NavigationView nvDrawer;
    private static ActionBarDrawerToggle drawerToggle;
    private static Menu sideMenu;
    private static ArrayList<UserAlgorithm> algorithms;
    private static View view;
    private static NavController navController;
    private static Context context;
    private static Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = this.getCurrentFocus();
        context = getApplicationContext();
        activity = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_purchasable, R.id.navigation_user_stocks, R.id.navigation_notifications, R.id.navigation_algorithm, R.id.fragment_stock_data)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        algorithms = (ArrayList<UserAlgorithm>)bundle.getSerializable("algorithms");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();


        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        mDrawer.addDrawerListener(drawerToggle);

        Menu menu = nvDrawer.getMenu();
        menu.add(Menu.NONE, 0, Menu.NONE, "Create Algorithm").setContentDescription(String.valueOf(0)).setIcon(getResources().getDrawable(android.R.drawable.ic_input_add));
        if (algorithms != null) {
            for (int i = 0; i < algorithms.size(); i++) {
                UserAlgorithm userAlgorithm = algorithms.get(i);
                if (i == 0) {
                    algorithm = userAlgorithm.UniqueId;
                    menu.add(Menu.NONE, i + 1, Menu.NONE, userAlgorithm.Name).setContentDescription(String.valueOf(userAlgorithm.UniqueId)).setChecked(true);
                } else {
                    menu.add(Menu.NONE, i + 1, Menu.NONE, userAlgorithm.Name).setContentDescription(String.valueOf(userAlgorithm.UniqueId));
                }
            }
        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra("title");
                String message = intent.getStringExtra("message");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialog = alertDialogBuilder
                        .setMessage(message)
                        .setTitle(title)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.hide();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        };

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token: ", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        token = task.getResult();

                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("UserName", MainActivity.username);
                            jo.put("FirebaseId", token.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String send = jo.toString();
                        HTTPRequestParams rp = new HTTPRequestParams("/receive_firebase_id",
                                send,
                                null,
                                null,
                                "");
                        new HTTPRequests().execute(rp);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(send_message_const);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    private static void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void selectDrawerItem(MenuItem menuItem) {
        Menu menu = nvDrawer.getMenu();
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            menu.getItem(i).setChecked(false);
        }
        int i = Integer.parseInt((String) menuItem.getContentDescription());
        if (i == 0) {
            Gson gson = new Gson();
            BasicUserSendIn basicUserSendIn = new BasicUserSendIn();
            basicUserSendIn.Username = username;
            String send = gson.toJson(basicUserSendIn).toString();
            HTTPRequestParams rp = new HTTPRequestParams("/create_algorithm", send, null, null, MainActivity::algorithmsChanged);
            new HTTPRequests().execute(rp);
        } else {
            algorithm = Integer.parseInt((String) menuItem.getContentDescription());
            menuItem.setChecked(true);
        }
        mDrawer.closeDrawers();
        navController.navigate(R.id.navigation_user_stocks);
    }
    private ActionBarDrawerToggle setupDrawerToggle() {

        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it

        // and will not render the hamburger icon without it.

        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.

        drawerToggle.syncState();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles

        drawerToggle.onConfigurationChanged(newConfig);

    }

    public static void createAlgorithmMenu(HTTPRequestParams ret) {
    }
    public static String send_message_const = "SEND_MESSAGE";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void algorithmsChanged (HTTPRequestParams ret) {
        Gson gson = new Gson();
        TypeToken<ArrayList<UserAlgorithm>> token = new TypeToken<ArrayList<UserAlgorithm>>() {};
        algorithms = gson.fromJson(ret.ret, token.getType());
        Menu menu = nvDrawer.getMenu();
        menu.clear();
        menu.add(Menu.NONE, 0, Menu.NONE, "Create Algorithm").setContentDescription(String.valueOf(0)).setIcon(context.getResources().getDrawable(android.R.drawable.ic_input_add));
        for (int i = 0; i < algorithms.size(); i++){
            UserAlgorithm userAlgorithm = algorithms.get(i);
            if (i == 0) {
                algorithm = userAlgorithm.UniqueId;
                menu.add(Menu.NONE, i+1, Menu.NONE, userAlgorithm.Name)
                        .setContentDescription(String.valueOf(userAlgorithm.UniqueId))
                        .setChecked(true);
            } else {
                menu.add(Menu.NONE, i+1, Menu.NONE, userAlgorithm.Name).setContentDescription(String.valueOf(userAlgorithm.UniqueId));
            }
        }
        navController.navigate(R.id.navigation_user_stocks);
    }
}