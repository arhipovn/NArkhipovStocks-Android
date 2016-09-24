package com.narkhipov.narkhipovstocksfinal;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.view.View.*;


public class StockRecyclerViewAdapter extends RecyclerView.Adapter<StockRecyclerViewAdapter.ViewHolder>  {

    private final List<Stock> mValues;
    private final OnAdapterItemInteraction mListener;
    public final static String TAG = "StockRecyclerFragment";

    public StockRecyclerViewAdapter(List<Stock> items, OnAdapterItemInteraction listener) {
        mValues = items;
        mListener = listener;
    }

    double newPercentChange;
    public double percentChangeFromPreferences;
    public String percentChangeColor;
    public boolean highlightFromPreferences;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.txtStockSymbol.setText(mValues.get(position).getStockSymbol());
        holder.txtLastTradePrice.setText(String.valueOf(mValues.get(position).getLastTradePrice()));
        holder.txtCompany.setText(mValues.get(position).getCompany());
        holder.percentChange.setText(String.valueOf(mValues.get(position).getPercentChange()));
        newPercentChange = Double.parseDouble(mValues.get(position).getPercentChange().substring(0, mValues.get(position).getPercentChange().length() - 1));

        if (newPercentChange > percentChangeFromPreferences || newPercentChange < -percentChangeFromPreferences) {

            if(highlightFromPreferences) {

                holder.percentChange.setTextColor(Color.parseColor(percentChangeColor));
                holder.change.setTextColor(Color.parseColor(percentChangeColor));
                holder.txtLastTradePrice.setTextColor(Color.parseColor(percentChangeColor));
            }
        }
        else {
            Log.d(TAG, "!!!! highlightFromPreferences in onBindView" + highlightFromPreferences);
            holder.percentChange.setTextColor(Color.WHITE);
            holder.change.setTextColor(Color.YELLOW);
            holder.txtLastTradePrice.setTextColor(Color.YELLOW);

        }
        holder.change.setText(String.valueOf(mValues.get(position).getChange()));


        holder.mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemSelected(holder.mItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                mListener.onItemSelectedToDelete(holder.mItem);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtStockSymbol;
        public final TextView txtLastTradePrice;
        public final TextView txtCompany;
        public final TextView percentChange;
        public final TextView change;


        public Stock mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtStockSymbol = (TextView) view.findViewById(R.id.stockSymbolTv);
            txtCompany = (TextView) view.findViewById(R.id.companyTv);
            txtLastTradePrice = (TextView) view.findViewById(R.id.lastTradePriceTv);
            percentChange = (TextView) view.findViewById(R.id.percentChangeTv);
            change = (TextView) view.findViewById(R.id.changeTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtStockSymbol.getText() + "'";
        }

    }

    public interface OnAdapterItemInteraction {
        void onItemSelected(Stock stock);
        void onItemSelectedToDelete(Stock stock);
    }

    public void add(Stock stock) {
        mValues.add(stock);
        notifyDataSetChanged();
    }

    public void replaceInfo(Stock stock) {

        for(Stock stock1: mValues) {
            if(stock1.getStockSymbol().equals(stock.getStockSymbol())) {
                stock1.setCompany(stock.getCompany());
                stock1.setLastTradePrice(stock.getLastTradePrice());
                stock1.setPercentChange(stock.getPercentChange());
                stock1.setChange(stock.getChange());
                stock1.setLastTradeDate(stock.getLastTradeDate());
                stock1.setOpenPrice(stock.getOpenPrice());
                stock1.setDaysLow(stock.getDaysLow());
                stock1.setDaysHigh(stock.getDaysHigh());
                stock1.setStockExchange(stock.getStockExchange());
                notifyDataSetChanged();
                Log.d(TAG, "Stock  got Updated " );

            }

        }
    }

    public void colorForPercentChange(String colorForPercentChange, double percentCh, boolean highlight) {
        percentChangeColor = colorForPercentChange;
        percentChangeFromPreferences = percentCh;
        highlightFromPreferences = highlight;


    }

    public void add(Stock stock, int position) {
        mValues.add(stock);
        notifyItemInserted(mValues.size() - 1);
    }

    public void remove(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(Stock item) {
        int position = mValues.indexOf(item);
        remove(position);
    }


}
