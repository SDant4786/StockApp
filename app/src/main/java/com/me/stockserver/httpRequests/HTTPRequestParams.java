package com.me.stockserver.httpRequests;

import android.content.Context;
import android.view.View;

import java.util.function.Consumer;

public class HTTPRequestParams {
    String url;
    public View v;
    Consumer<HTTPRequestParams> f;
    public String ret;
    public Context c;
    public String send;

    public HTTPRequestParams(String _url, String _send, View _v, Context _c, Consumer<HTTPRequestParams> _func){
        this.url = _url;
        this.v = _v;
        this.c = _c;
        this.f = _func;
        this.ret = "";
        this.send = _send;
    }

    public HTTPRequestParams(String _url, String _send, Context _c, Consumer<HTTPRequestParams> _func, String asdf){
        this.url = _url;
        this.send = _send;
        this.c = _c;
        this.f = _func;
    }
}
