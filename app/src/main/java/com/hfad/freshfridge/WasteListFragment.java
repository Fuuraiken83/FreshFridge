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


public class WasteListFragment extends ListFragment {
    private DBFridgeListHelper dbHelper;
    private Cursor cursor;
    CustomProductCursorAdapter adapter;


    public static WasteListFragment newInstance(int visibility) {
        Bundle bundle = new Bundle();
        bundle.putInt("visibility",visibility);
        String query = "SELECT * FROM " + DBTables.Product.TABLE_NAME
                + " WHERE " + "strftime('%Y-%m-%d', " + DBTables.Product.COLUMN_EXPIRATION_DATE + ") - strftime('%Y-%m-%d','now') <= 0";
        bundle.putString("query",query);
        WasteListFragment fragment = new WasteListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public WasteListFragment() {
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
            adapter = new CustomProductCursorAdapter(getActivity(),cursor,visibility,0);
            setListAdapter(adapter);
        }
        if (getActivity() != null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.waste));
        }
        View view = inflater.inflate(R.layout.fragment_waste_list, container, false);
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
