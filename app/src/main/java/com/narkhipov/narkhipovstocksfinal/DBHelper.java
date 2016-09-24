package com.narkhipov.narkhipovstocksfinal;

/**
 * Created by llasslo on 10/28/14.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public final class DBHelper {
    private static final String LOGTAG = "DBHelper";

    private static final String DATABASE_NAME = "stocks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "stocksdata";

    // Column Names
    public static final String KEY_ID = "_id";
    public static final String KEY_STOCKSYMBOL = "stockSymbol";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_LASTTRADEPRICE = "lastTradePrice";
    public static final String KEY_PERCENTCHANGE = "percentChange";
    public static final String KEY_CHANGE = "change";
    public static final String KEY_LASTTRADEDATE = "lastTradeDate";
    public static final String KEY_OPENPRICE = "openPrice";
    public static final String KEY_DAYSLOW = "daysLow";
    public static final String KEY_DAYSHIGH = "daysHigh";
    public static final String KEY_STOCKEXCHANGE = "stockExchange";


    // Column indexes
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_STOCKSYMBOL = 1;
    public static final int COLUMN_COMPANY = 2;
    public static final int COLUMN_LASTTRADEPRICE = 3;
    public static final int COLUMN_PERCENTCHANGE = 4;
    public static final int COLUMN_CHANGE = 5;
    public static final int COLUMN_LASTTRADEDATE = 6;
    public static final int COLUMN_OPENPRICE = 7;
    public static final int COLUMN_DAYSLOW = 8;
    public static final int COLUMN_DAYSHIGH = 9;
    public static final int COLUMN_STOCKEXCHANGE = 10;




    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;

    private static final String INSERT =
            "INSERT INTO " + TABLE_NAME + "(" +
                    KEY_STOCKSYMBOL + ", " +
                    KEY_COMPANY + ", " +
                    KEY_LASTTRADEPRICE + ", " + KEY_PERCENTCHANGE + ", "+ KEY_CHANGE +", "+ KEY_LASTTRADEDATE +", "+ KEY_OPENPRICE +", "+ KEY_DAYSLOW +", "+ KEY_DAYSHIGH +", "+ KEY_STOCKEXCHANGE +") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    public DBHelper(Context context) throws Exception {
        this.context = context;
        try {
            OpenHelper openHelper = new OpenHelper(this.context);
            db = openHelper.getWritableDatabase();
            insertStmt = db.compileStatement(INSERT);

        } catch (Exception e) {
            Log.e(LOGTAG, " DBHelper constructor:  could not get database " + e);
            throw (e);
        }
    }

    public long insert(Stock stockInfo) {
        insertStmt.bindString(COLUMN_STOCKSYMBOL, stockInfo.getStockSymbol());
        insertStmt.bindString(COLUMN_COMPANY, stockInfo.getCompany());
        insertStmt.bindDouble(COLUMN_LASTTRADEPRICE, stockInfo.getLastTradePrice());
        insertStmt.bindString(COLUMN_PERCENTCHANGE, stockInfo.getPercentChange());
        insertStmt.bindString(COLUMN_CHANGE, stockInfo.getChange());
        insertStmt.bindString(COLUMN_LASTTRADEDATE, stockInfo.getLastTradeDate());
        insertStmt.bindDouble(COLUMN_OPENPRICE, stockInfo.getOpenPrice());
        insertStmt.bindDouble(COLUMN_DAYSLOW, stockInfo.getDaysLow());
        insertStmt.bindDouble(COLUMN_DAYSHIGH, stockInfo.getDaysHigh());
        insertStmt.bindString(COLUMN_STOCKEXCHANGE, stockInfo.getStockExchange());

        long value = -1;
        try {
            value = insertStmt.executeInsert();
        } catch (Exception e) {
            Log.e(LOGTAG, " executeInsert problem: " + e);
        }
        Log.d(LOGTAG, "value=" + value);
        return value;
    }

    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }

    public boolean deleteRecord(long rowId) {
        return db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
    }

    public List<Stock> selectAll() {
        List<Stock> list = new ArrayList<Stock>();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KEY_ID, KEY_STOCKSYMBOL, KEY_COMPANY, KEY_LASTTRADEPRICE, KEY_PERCENTCHANGE, KEY_CHANGE, KEY_LASTTRADEDATE, KEY_OPENPRICE, KEY_DAYSLOW, KEY_DAYSHIGH, KEY_STOCKEXCHANGE},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Stock stockInfo = new Stock();
                stockInfo.setStockSymbol(cursor.getString(COLUMN_STOCKSYMBOL));
                stockInfo.setCompany(cursor.getString(COLUMN_COMPANY));
                stockInfo.setLastTradePrice(cursor.getInt(COLUMN_LASTTRADEPRICE));
                stockInfo.setPercentChange(cursor.getString(COLUMN_PERCENTCHANGE));
                stockInfo.setChange(cursor.getString(COLUMN_CHANGE));
                stockInfo.setId(cursor.getLong(COLUMN_ID));
                stockInfo.setLastTradeDate(cursor.getString(COLUMN_LASTTRADEDATE));

                stockInfo.setOpenPrice(cursor.getDouble(COLUMN_OPENPRICE));
                stockInfo.setDaysLow(cursor.getDouble(COLUMN_DAYSLOW));
                stockInfo.setDaysHigh(cursor.getDouble(COLUMN_DAYSHIGH));
                stockInfo.setStockExchange(cursor.getString(COLUMN_STOCKEXCHANGE));

                list.add(stockInfo);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    private static class OpenHelper extends SQLiteOpenHelper {
        private static final String LOGTAG = "OpenHelper";

        private static final String CREATE_TABLE =
                "CREATE TABLE " +
                        TABLE_NAME +
                        " (" + KEY_ID + " integer primary key autoincrement, " +
                        KEY_STOCKSYMBOL + " TEXT, " +
                        KEY_COMPANY + " TEXT, " +
                        KEY_LASTTRADEPRICE + " integer, " + KEY_PERCENTCHANGE + " TEXT, " + KEY_CHANGE + " TEXT, " + KEY_LASTTRADEDATE + " TEXT, " +
                        KEY_OPENPRICE + " integer, " + KEY_DAYSLOW + " integer, " + KEY_DAYSHIGH + " integer, " +KEY_STOCKEXCHANGE+" TEXT);";



        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOGTAG, " onCreate");
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(LOGTAG, " onCreate:  Could not create SQL database: " + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOGTAG, "Upgrading database, this will drop tables and recreate.");
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e) {
                Log.e(LOGTAG, " onUpgrade:  Could not update SQL database: " + e);
            }
        }
    }

    public boolean update(Stock stock) {
        ContentValues args = new ContentValues();
        args.put(KEY_STOCKSYMBOL, stock.getStockSymbol());
        args.put(KEY_COMPANY, stock.getCompany());
        args.put(KEY_LASTTRADEPRICE, stock.getLastTradePrice());
        args.put(KEY_PERCENTCHANGE, stock.getPercentChange());
        args.put(KEY_CHANGE, stock.getPercentChange());
        args.put(KEY_LASTTRADEDATE, stock.getLastTradeDate());
        args.put(KEY_OPENPRICE, stock.getOpenPrice());
        args.put(KEY_DAYSLOW, stock.getDaysLow());
        args.put(KEY_DAYSHIGH, stock.getDaysHigh());
        args.put(KEY_STOCKEXCHANGE, stock.getStockExchange());

        Log.e(LOGTAG, " Update");

        return db.update(TABLE_NAME, args, KEY_STOCKSYMBOL + "=\"" + stock.getStockSymbol().toLowerCase()+ "\"", null) > 0;

    }

    public boolean checkIfExistInDatabase(String stockSymbol) {

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KEY_ID, KEY_STOCKSYMBOL, KEY_COMPANY, KEY_LASTTRADEPRICE, KEY_PERCENTCHANGE, KEY_CHANGE, KEY_LASTTRADEDATE, KEY_OPENPRICE, KEY_DAYSLOW, KEY_DAYSHIGH, KEY_STOCKEXCHANGE}, KEY_STOCKSYMBOL + "=?",
                new String[] { stockSymbol },
                null, null, null, null);


        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }

    }

}