package com.hfad.freshfridge;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hfad.freshfridge.data.DBFridgeListHelper;
import com.hfad.freshfridge.data.DBTables;


public class ProductListFragment extends ListFragment {
    private DBFridgeListHelper dbHelper;
    private Cursor cursor;
    CustomProductCursorAdapter adapter;

    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(int visibility,int id) {
        Bundle bundle = new Bundle();
        String query = "SELECT * FROM " + DBTables.Product.TABLE_NAME
                + " WHERE " + DBTables.Product.COLUMN_FRIDGE_ID + " = " + String.valueOf(id);
        String fridgeName = "SELECT " + DBTables.Fridge.COLUMN_NAME
                + " FROM " + DBTables.Fridge.TABLE_NAME
                + " WHERE " + DBTables.Fridge._ID + " = " + String.valueOf(id);
        bundle.putInt("visibility",visibility);
        bundle.putString("query",query);
        bundle.putString("fridgeName",fridgeName);
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null){
            dbHelper = new DBFridgeListHelper(getContext());
            int visibility  = getArguments().getInt("visibility",0);
            cursor = setCursor(getArguments().getString("query"));
            adapter = new CustomProductCursorAdapter(getActivity(),cursor,visibility,1);
            setListAdapter(adapter);
            cursor = setCursor(getArguments().getString("fridgeName"));
            cursor.moveToNext();
            String fridgeName = cursor.getString(cursor.getColumnIndex(DBTables.Fridge.COLUMN_NAME));
            if (getActivity() != null) {
                ((MainActivity) getActivity())
                        .getSupportActionBar()
                        .setTitle(fridgeName);
            }
        }
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        return view;
    }

    private Cursor setCursor(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }
}