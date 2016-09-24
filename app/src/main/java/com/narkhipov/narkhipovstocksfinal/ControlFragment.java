package com.narkhipov.narkhipovstocksfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ControlFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment implements VolleyClassString.OnInfoListener<String>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private final static String formatKey = "format";
    private final static String formatType = "json";
    private VolleyClassString volleyClassString;
    StockParser parseJsonInfo;
    String stockSymbol;

    String queryBase = "http://query.yahooapis.com/v1/public/yql?q=Select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"+stockSymbol+"%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    Stock stock;

    public ControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button saveButton =
                (Button) getActivity().findViewById(R.id.stock_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onSave();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onStockInteraction(Stock stock);
    }

    public void showMissingInfoAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialogBuilder
                .setMessage(getResources().getString(R.string.alert_message))
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void onSave() {
        stock = new Stock();
        EditText stockSymbolEditText = (EditText) getActivity().findViewById(R.id.stockSymbolEditText);
        stockSymbol = stockSymbolEditText.getText().toString();

        if (TextUtils.isEmpty(stockSymbol)) {
            showMissingInfoAlert();
        } else {
            getNetworkInfo();

            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    @Override
    public void onInfoAvailable(String responseString) {
        if (parseJsonInfo == null){
            parseJsonInfo = new StockParser();
        }

        parseJsonInfo.parseTheData(responseString, new StockParser.stockCallback() {
            @Override
            public void onStock(Stock stock) {
                    if (mListener != null)
                        mListener.onStockInteraction(stock);
            }
        });
    }

    public void getNetworkInfo() {
        String queryBase = "http://query.yahooapis.com/v1/public/yql?q=Select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"+stockSymbol+"%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";


        final String queryString = Uri.parse(queryBase).buildUpon().appendQueryParameter(formatKey,formatType).build().toString();
        if (volleyClassString == null) {
            volleyClassString = new VolleyClassString(getActivity(), this);
        }
        volleyClassString.makeNetworkRequests(queryString);
    }

}
