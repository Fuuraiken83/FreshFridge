package com.f83.freshfridge;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f83.freshfridge.data.DBFridgeListHelper;
import com.f83.freshfridge.data.DBTables;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllFoodFragment extends ListFragment {
    private DBFridgeListHelper dbHelper;
    private Cursor cursor;
    CustomProductCursorAdapter adapter;

    public AllFoodFragment() {
        // Required empty public constructor
    }

    public static AllFoodFragment newInstance() {
        Bundle bundle = new Bundle();
        String query = "SELECT * FROM " + DBTables.Product.TABLE_NAME;
        bundle.putString("query",query);
        AllFoodFragment fragment = new AllFoodFragment();
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
            setCursor(getArguments().getString("query"));
            adapter = new CustomProductCursorAdapter(getActivity(),cursor,0,0);
            setListAdapter(adapter);
        }
        if (getActivity() != null){
            ((MainActivity) getActivity())
                    .getSupportActionBar()
                    .setTitle(getString(R.string.all_food));
        }
        View view = inflater.inflate(R.layout.fragment_all_food, container, false);
        return view;
    }

    private void setCursor(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(query,null);
        return;
    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }
}
