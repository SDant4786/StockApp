package com.me.stockserver.data.model;

import java.util.ArrayList;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String username;
    private String token;
    private ArrayList<String> stocksBought;
    private ArrayList<String> stocksToSell;

    public LoggedInUser(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return username;
    }

    public void setStocksBought(ArrayList<String> stocksBought) { this.stocksBought = stocksBought; }

    public ArrayList<String> getStocksBought() { return this.stocksBought; }

    public void setStocksToSell(ArrayList<String> stocksToSell){ this.stocksToSell = stocksToSell; }

    public ArrayList<String> getStocksToSell() {return this.stocksToSell; }

    public void setToken(String token){  this.token = token; }
}