package com.narkhipov.narkhipovstocksfinal;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockDialog extends DialogFragment {
    private static String DIALOG_STRING = "DIALOG_STRING";

    public StockDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_dialog, container, false);
        String title = getArguments().getString(DIALOG_STRING);
        TextView tv = (TextView)view.findViewById(R.id.dialogID);
        tv.setText(title);
        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getResources().getString(R.string.stock_details));
        return dialog;
    }


    public static StockDialog newInstance(Stock stock) {
        StockDialog fragment = new StockDialog();
        Bundle args = new Bundle();
        String stockText = "Last Trade Date:  "  + stock.getLastTradeDate() + "\n" + "Open Price:           " +stock.getOpenPrice() + "\n"+
        "Day's low:              " +stock.getDaysLow() + "\n" +"Day's high:            " +stock.getDaysHigh() +
                "\n" + "Stock Exchange:  " +stock.getStockExchange();
        args.putString(DIALOG_STRING, stockText);
        fragment.setArguments(args);
        return fragment;
    }

}
