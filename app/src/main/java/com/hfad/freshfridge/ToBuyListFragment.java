package com.hfad.freshfridge;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.freshfridge.data.DBFridgeListHelper;
import com.hfad.freshfridge.data.DBTables;


public class ToBuyListFragment extends ListFragment {
    private DBFridgeListHelper dbHelper;
    private Cursor cursor;
    CustomToBuyCursorAdapter adapter;

    public static ToBuyListFragment newInstance(int visibility) {
        Bundle bundle = new Bundle();
        String query = "SELECT * FROM " + DBTables.ToBuyList.TABLE_NAME;
        bundle.putInt("visibility",visibility);
        bundle.putString("query",query);

        ToBuyListFragment fragment = new ToBuyListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public ToBuyListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null){
            dbHelper = new DBFridgeListHelper(getContext());
            setCursor(getArguments().getString("query"));
            int visibility  = getArguments().getInt("visibility",0);
            adapter = new CustomToBuyCursorAdapter(getActivity(),cursor,visibility);
            setListAdapter(adapter);
        }
        if (getActivity() != null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.to_buy_list));
        }
        View view = inflater.inflate(R.layout.fragment_to_buy_list, container, false);
        return view;
    }

    private void setCursor(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(query,null);
    }

    @Override
    public void onDestroy() {
        if (cursor != null){
            cursor.close();
        }
        super.onDestroy();
    }

}
