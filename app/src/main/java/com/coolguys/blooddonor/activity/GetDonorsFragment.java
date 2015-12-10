package com.coolguys.blooddonor.activity;

/**
 * Created by Raj on 05/12/15.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import info.androidhive.materialdesign.R;


public class GetDonorsFragment extends Fragment {

    public GetDonorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static String [] donorsNames={"Raj", "Sam", "Maddy", "Satya", "Naresh", "Nvn"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ListView donorsList = (ListView) rootView.findViewById(R.id.listView);

        donorsList.setAdapter(new CustomListViewAdapter(getActivity(), donorsNames));
        // Inflate the layout for this fragment


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
