package com.ashish.myteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_Contri_Status extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Class_Contri_Status> Contri_Status;

    //constructor initializing the values
    public Adapter_Contri_Status(Context context, int resource, ArrayList<Class_Contri_Status> eventCardDetail) {
        this.context = context;
        this.layout = resource;
        this.Contri_Status = eventCardDetail;
    }

    @Override
    public int getCount() {
        return Contri_Status.size();
    }

    @Override
    public Object getItem(int position) {
        return Contri_Status.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView status;
        TextView memName,count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Adapter_Contri_Status.ViewHolder holder = new Adapter_Contri_Status.ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);


        holder.status = view.findViewById(R.id.contriStatus);
        holder.memName = view.findViewById(R.id.memberName);
        holder.count = view.findViewById(R.id.count);
        Class_Contri_Status card = Contri_Status.get(position);

        if(card.isStatus())
        {
            holder.status.setImageResource(R.drawable.tick);
        }

        holder.memName.setText(card.getName());
        holder.count.setText(String.valueOf(position+1)+".");
        return view;
    }
}
