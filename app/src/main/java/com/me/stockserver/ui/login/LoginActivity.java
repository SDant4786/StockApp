package com.me.stockserver.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.me.stockserver.MainActivity;
import com.me.stockserver.R;
import com.me.stockserver.data.structs.Notification;
import com.me.stockserver.data.structs.sendIns.LoginSendBack;
import com.me.stockserver.data.structs.sendIns.UserAlgorithm;
import com.me.stockserver.httpRequests.HTTPRequestParams;
import com.me.stockserver.httpRequests.HTTPRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class LoginActivity extends AppCompatActivity {

    private static LoginViewModel loginViewModel;
    private static Context c;
    private static View view;
    private static EditText usernameEditText;
    private static EditText passwordEditText;
    private static TextView failedLoginText;
    private static ProgressBar loadingProgressBar;
    private static ArrayList<UserAlgorithm> algorithms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        view = getWindow().getDecorView().findViewById(android.R.id.content);
        c = view.getContext();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        failedLoginText = findViewById(R.id.tvFailedLogIn);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        final Button createuser = findViewById(R.id.btnCreateUser);
        setSSLContext();
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                createuser.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                String user = usernameEditText.getText().toString();
                bundle.putString("username", user);
                bundle.putSerializable("algorithms", algorithms);
                i.putExtras(bundle);
                startActivity(i);

                finish();

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                JSONObject jo = new JSONObject();
                try {
                    jo.put("UserName", username);
                    jo.put("Password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String send = jo.toString();

                HTTPRequestParams rp = new HTTPRequestParams("/login",
                        send,
                        view,
                        c,
                        LoginActivity::loginCallBack);

                new HTTPRequests().execute(rp);
            }
        });

        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                JSONObject jo = new JSONObject();
                try {
                    jo.put("UserName", username);
                    jo.put("Password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String send = jo.toString();

                HTTPRequestParams rp = new HTTPRequestParams("/create_user",
                        send,
                        view,
                        c,
                        LoginActivity::createNewUserCallBack);

                new HTTPRequests().execute(rp);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public static void createNewUserCallBack (HTTPRequestParams ret) {
        LoginSendBack loginSendBack = new LoginSendBack();
        Gson gson = new Gson();
        loginSendBack = gson.fromJson(ret.ret, LoginSendBack.class);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        if (loginSendBack.Successful == true) {
            algorithms = loginSendBack.Algorithms;
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        } else {
            failedLoginText.setText("User already exists \n Pick another one");
        }

    }

    public static void loginCallBack (HTTPRequestParams ret ) {
        LoginSendBack loginSendBack = new LoginSendBack();
        Gson gson = new Gson();
        loginSendBack = gson.fromJson(ret.ret, LoginSendBack.class);

        loadingProgressBar.setVisibility(View.INVISIBLE);
        if (loginSendBack.Successful == true) {
            algorithms = loginSendBack.Algorithms;
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        } else {
            failedLoginText.setText("Incorrect user name or password");
        }
    }

    private void setSSLContext () {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        InputStream caInput = null;
        try {
            caInput = c.getAssets().open("https-server.crt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null, null);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            trustManagerFactory.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        try {
            HTTPRequests.sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            HTTPRequests.sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}