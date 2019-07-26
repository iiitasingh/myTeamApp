package com.ashish.myteam;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import static com.ashish.myteam.SplashScreen.CrtTransList;



public class Trans_Credit extends Fragment {


    public Trans_Credit() {
        // Required empty public constructor
    }

    ListView transListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trans_credit, container, false);
        transListView = (ListView)v.findViewById(R.id.transCreditList);
        if (CrtTransList.isEmpty()) {
            transListView.setAdapter(new Adapter_Transaction_List(getActivity(), R.layout.list_transaction, CrtTransList));
            Toast.makeText(getActivity(), "No Credit Transactions", Toast.LENGTH_LONG).show();
        } else {
            transListView.setAdapter(new Adapter_Transaction_List(getActivity(), R.layout.list_transaction, CrtTransList));
        }
        return v;
    }

}
