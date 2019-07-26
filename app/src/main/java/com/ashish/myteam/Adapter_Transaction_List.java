package com.ashish.myteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import static com.ashish.myteam.SplashScreen.EventMap;
import static com.ashish.myteam.SplashScreen.UserMap;

import java.util.ArrayList;

public class Adapter_Transaction_List extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Class_Transaction> transCard;
    String type = "debit";

    //constructor initializing the values
    public Adapter_Transaction_List(Context context, int resource, ArrayList<Class_Transaction> eventCardDetail) {
        this.context = context;
        this.layout = resource;
        this.transCard = eventCardDetail;
    }

    @Override
    public int getCount() {
        return transCard.size();
    }

    @Override
    public Object getItem(int position) {
        return transCard.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView transTypeImg;
        TextView transFor,transForName,transAmount,transDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Adapter_Transaction_List.ViewHolder holder = new Adapter_Transaction_List.ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);


        holder.transTypeImg = view.findViewById(R.id.debitIimageView);
        holder.transFor = view.findViewById(R.id.TextPaidFor);
        holder.transForName = view.findViewById(R.id.textEventName);
        holder.transAmount = view.findViewById(R.id.textAmount);
        holder.transDate = view.findViewById(R.id.textDate);
        Class_Transaction card = transCard.get(position);

        if(card.tType.equals(type))
        {
            holder.transTypeImg.setImageResource(R.drawable.ic_up_arrow);
            holder.transFor.setText(card.tDesc);
            if(EventMap.get(card.eventId) != null) {
                holder.transForName.setText(EventMap.get(card.eventId).eName);
            }
            else {
                holder.transForName.setText("");
            }
        }
        else {
            holder.transTypeImg.setImageResource(R.drawable.ic_down_arrow);
            if(EventMap.get(card.eventId) != null) {
                holder.transFor.setText(EventMap.get(card.eventId).eName);
            }else {
                holder.transFor.setText("");
            }
            if(UserMap.get(card.memberId) != null) {
                holder.transForName.setText(UserMap.get(card.memberId).name);
            }else {
                holder.transForName.setText("");
            }
        }

        holder.transAmount.setText(String.valueOf(card.tAmount));
        holder.transDate.setText(card.tDate);
        return view;
    }
}
