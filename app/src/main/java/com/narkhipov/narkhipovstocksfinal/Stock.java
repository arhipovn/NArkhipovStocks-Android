package com.narkhipov.narkhipovstocksfinal;


public class Stock {
    String stockSymbol;
    String company;
    double lastTradePrice;
    String change;
    String percentChange;
    protected long id = 0;
    double openPrice;
    double daysLow;
    double daysHigh;
    String stockExchange;
    String lastTradeDate;



    public String getLastTradeDate() {
        return lastTradeDate;
    }

    public void setLastTradeDate(String lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    public Stock() {

    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getDaysLow() {
        return daysLow;
    }

    public void setDaysLow(double daysLow) {
        this.daysLow = daysLow;
    }

    public double getDaysHigh() {
        return daysHigh;
    }

    public void setDaysHigh(double daysHigh) {
        this.daysHigh = daysHigh;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static final String TAG_RESULTS = "results";
    public static final String TAG_QUOTE = "quote";
    public static final String TAG_NAME = "Name";
    public static final String TAG_QUERY = "query";
    public static final String TAG_LASTTRADEPRICE = "LastTradePriceOnly";
    public static final String TAG_CHANGE = "Change";
    public static final String TAG_PERCENTCHANGE = "ChangeinPercent";
    public static final String TAG_LASTTRADEDATE = "LastTradeDate";
    public static final String TAG_OPENPRICE = "Open";
    public static final String TAG_DAYSLOW = "DaysLow";
    public static final String TAG_DAYSHIGH = "DaysHigh";
    public static final String TAG_STOCKEXCHANGE = "StockExchange";
    public static final String TAG_SYMBOL = "Symbol";

}
