package com.hfad.freshfridge;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.hfad.freshfridge.data.DBFridgeListHelper;
import com.hfad.freshfridge.data.DBTables;


public class FridgeListFragment extends ListFragment {
    private DBFridgeListHelper dbHelper;
    private Cursor cursor;

    CustomFridgeCursorAdapter adapter;

    public FridgeListFragment() {
        // Required empty public constructor
    }

    public static FridgeListFragment newInstance(int visibility) {
        Bundle bundle = new Bundle();
        String query = "SELECT * FROM " + DBTables.Fridge.TABLE_NAME;
        bundle.putInt("visibility",visibility);
        bundle.putString("query",query);
        FridgeListFragment fragment = new FridgeListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null){
            dbHelper = new DBFridgeListHelper(getContext());
            int visibility  = getArguments().getInt("visibility",0);
            setCursor(getArguments().getString("query"));
            adapter = new CustomFridgeCursorAdapter(getActivity(),getCursor(),visibility );
            setListAdapter(adapter);
        }
        if (getActivity() != null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fridge_list));
        }
        View view = inflater.inflate(R.layout.fragment_fridge_list, container, false);
        return view;
    }

    private void setCursor(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(query,null);
    }

    public Cursor getCursor(){
        return cursor;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);
        int fridgeID = cursor.getInt(cursor.getColumnIndex(DBTables.Fridge._ID));
        if(getParentFragment() != null && fridgeID != 0){
            ((FridgesFragment)getParentFragment()).setSelectedFridgeID(fridgeID);
            ((FridgesFragment)getParentFragment()).createProductListFragment(fridgeID);

            if (getParentFragment().getActivity() != null){
                ((MainActivity)getParentFragment().getActivity())
                        .getSupportActionBar()
                        .setTitle(cursor.getString(cursor.getColumnIndex(DBTables.Fridge.COLUMN_NAME)));
                ((MainActivity)getParentFragment().getActivity()).toolbarSetFridgesState();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fridge_list));
        }
    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }
}

