package com.narkhipov.narkhipovstocksfinal;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nikolayarkhipov on 8/5/16.
 */
public class StockParser {

    private final static String TAG = "StockParser";
    Stock stock = null;

    public StockParser() {
    }

    public Stock getStock() {
        return stock;
    }


    public interface stockCallback {

        void onStock(Stock stock);
    }

    public int parseTheData(String responseStr, stockCallback callback) {
        int numEarthquakes = 0;
        stock = new Stock();

        try {
            JSONObject jObject = new JSONObject(responseStr);
            JSONObject jQuery = jObject.getJSONObject(Stock.TAG_QUERY);

            JSONObject jResultsObj = jQuery.getJSONObject(Stock.TAG_RESULTS);
            JSONObject jQuoteObj = jResultsObj.getJSONObject(Stock.TAG_QUOTE);

            stock.setStockSymbol(jQuoteObj.getString(Stock.TAG_SYMBOL).toUpperCase());

            if(jQuoteObj.getString(Stock.TAG_NAME).equals("null")) {
                stock.setCompany("");

            }
            else {
                stock.setCompany(jQuoteObj.getString(Stock.TAG_NAME));
            }
            Log.d(TAG, "Parsing: " + stock.getCompany());

            try {
                stock.setLastTradePrice(jQuoteObj.getDouble(Stock.TAG_LASTTRADEPRICE));
            }
            catch (Exception e) {
                stock.setLastTradePrice(0);

            }
            if(jQuoteObj.getString(Stock.TAG_CHANGE).equals("null")) {
                stock.setChange("");
            }
            else {
                stock.setChange(jQuoteObj.getString(Stock.TAG_CHANGE));
            }
            if(jQuoteObj.getString(Stock.TAG_PERCENTCHANGE).equals("null")) {
                stock.setPercentChange("");
            }
            else {
                stock.setPercentChange(jQuoteObj.getString(Stock.TAG_PERCENTCHANGE));

            }

            stock.setLastTradeDate(jQuoteObj.getString(Stock.TAG_LASTTRADEDATE));
            stock.setOpenPrice(jQuoteObj.getDouble(Stock.TAG_OPENPRICE));
            stock.setDaysLow(jQuoteObj.getDouble(Stock.TAG_DAYSLOW));
            stock.setDaysHigh(jQuoteObj.getDouble(Stock.TAG_DAYSHIGH));
            stock.setStockExchange(jQuoteObj.getString(Stock.TAG_STOCKEXCHANGE));

            Log.d(TAG, "Parsing change: " + stock.getChange());
            Log.d(TAG, "Parsing percentChangeFromPreferences: " + stock.getPercentChange());
            Log.d(TAG, "Parsing lastTradePrice: " + stock.getLastTradePrice());

            if (callback != null) {
                callback.onStock(stock);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return numEarthquakes;
    }
}

