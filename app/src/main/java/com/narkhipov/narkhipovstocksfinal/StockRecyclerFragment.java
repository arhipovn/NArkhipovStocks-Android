package com.narkhipov.narkhipovstocksfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StockRecyclerFragment extends Fragment implements StockRecyclerViewAdapter.OnAdapterItemInteraction, VolleyClassString.OnInfoListener<String>  {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public final static String TAG = "StockRecyclerFragment";
    List<Stock> stocks=new ArrayList<Stock>();
    String stockSymbol;
    private DBHelper dbHelper = null;
    StockRecyclerViewAdapter stockAdapter;
    String queryBase = "http://query.yahooapis.com/v1/public/yql?q=Select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"+stockSymbol+"%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private final static String formatKey = "format";
    private final static String formatType = "json";
    private VolleyClassString volleyClassString;
    StockParser parseJsonInfo;
    public static final int STOCK_PREFERENCES = 1;
    public double percentChange ;
    public boolean highlightPercentChange = false;
    public String colorForPercentChange;
    private OnListFragmentInteractionListener mListener;


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StockRecyclerFragment newInstance(int columnCount) {
        StockRecyclerFragment fragment = new StockRecyclerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public StockRecyclerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_list, container, false);


        try {
            dbHelper = new DBHelper(getActivity());
            Log.d(TAG, " DB get activity: ");

            stocks = dbHelper.selectAll();
            Log.d(TAG, " DB select all : ");

        } catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();
        }

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_stocklist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(getActivity(), SettingsActivity.class), STOCK_PREFERENCES);
                return true;
            case R.id.action_refresh:
                Log.d(TAG, "onOptionsItemSelected: action_refresh");
                getNetworkInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        Log.d(TAG, "onActivityResult reqCode: " + reqCode + " resCode: " + resCode);
        switch(reqCode) {
            case STOCK_PREFERENCES: {
                //updateStockes();
                getNetworkInfo();
                break;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        RecyclerView recycleView = (RecyclerView) getActivity().findViewById(R.id.recycleView);
        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);

        stockAdapter = new StockRecyclerViewAdapter(stocks, this);
        recycleView.setAdapter(stockAdapter);

        getNetworkInfo();
    }

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach () {
        super.onDetach();
        mListener = null;
    }

    public void onItemSelected(Stock stock) {
        if (mListener != null) {
            mListener.onStockFragmentInteraction(stock);
        }
    }

    public void onItemSelectedToDelete(Stock stock) {
        deleteStock(stock);

    }


    public void getNetworkInfo() {
        updateFromPreferences();

        for(Stock stock:stocks) {
            stockSymbol = stock.getStockSymbol();

            String queryBase = "http://query.yahooapis.com/v1/public/yql?q=Select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"+stockSymbol+"%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

            final String queryString = Uri.parse(queryBase).buildUpon().appendQueryParameter(formatKey, formatType).build().toString();
            if (volleyClassString == null) {
                volleyClassString = new VolleyClassString(getActivity(), this);
            }
            volleyClassString.makeNetworkRequests(queryString);
        }
    }

    @Override
    public void onInfoAvailable(String responseString) {
        Log.d(TAG, "onInfoAvailableFromFragment " + responseString);

        if (parseJsonInfo == null){
            parseJsonInfo = new StockParser();
        }
        parseJsonInfo.parseTheData(responseString, new StockParser.stockCallback() {
            @Override
            public void onStock(Stock stock) {

                for (Stock stock1 : stocks) {
                    if (stock1.getStockSymbol().equals(stock.getStockSymbol())) {

                        stockAdapter.colorForPercentChange(colorForPercentChange, percentChange, highlightPercentChange );
                        dbHelper.update(stock);
                        stockAdapter.replaceInfo(stock);

                    }
                }
                }
           // }
        });
    }


    private void updateFromPreferences() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        percentChange = Double.parseDouble(prefs.getString(Constants.PREF_MIN_PERCENTAGE, "0.1"));
       // colorForPercentChange = Integer.parseInt(prefs.getString(Constants.PREF_COLOR, String.valueOf(Color.RED)));
        colorForPercentChange = prefs.getString(Constants.PREF_COLOR, String.valueOf(Color.RED));

        highlightPercentChange = prefs.getBoolean(Constants.PREF_SHOULD_BE_HIGHLIGHTED_OR_NOT, false);

    }

    public void updateStockes() {

        updateFromPreferences();
        if (volleyClassString == null){
            volleyClassString = new VolleyClassString(getActivity(), this);
        }
        volleyClassString.makeNetworkRequests(queryBase);
    }


    public interface OnListFragmentInteractionListener {
        void onStockFragmentInteraction(Stock stock);
    }

    public void addStock(Stock stock) {

                long stockId = 0;
                if (dbHelper != null) {
                    if (!dbHelper.checkIfExistInDatabase(stock.getStockSymbol())) {
                        Log.d(TAG, " DB is not null:  ");

                        stockId = dbHelper.insert(stock);
                        Log.d(TAG, " Stock added to DB: ");

                        stock.setId(stockId);

                        stockAdapter.add(stock);
                        Log.d(TAG, " addStock: ");
                        stockAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, " Replace info: ");
                        stockAdapter.replaceInfo(stock);
                        dbHelper.update(stock);

                    }
                }
    }


    public void deleteStock(Stock stock) {
        if (stock != null) {
            if (dbHelper != null)
                dbHelper.deleteRecord(stock.getId());
            stockAdapter.remove(stock);
            stockAdapter.notifyDataSetChanged();

        }
    }
}