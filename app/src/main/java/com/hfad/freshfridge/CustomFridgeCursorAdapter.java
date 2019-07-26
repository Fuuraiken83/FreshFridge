package com.hfad.freshfridge;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.hfad.freshfridge.data.DBFridgeListHelper;
import com.hfad.freshfridge.data.DBTables;

import java.util.ArrayList;


public class CustomFridgeCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private boolean visibleCheckboxes;
    private DBFridgeListHelper dbHelper;

    ArrayList<Integer> fridgeIDs;
    private int ID;

    // Default constructor
    public CustomFridgeCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        fridgeIDs = new ArrayList<>();
        if (flags == 1){
            visibleCheckboxes = true;
        }
        else{
            visibleCheckboxes = false;
        }
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbHelper = new DBFridgeListHelper(context);
        // R.layout.list_row is your xml layout for each row
        View view = cursorInflater.inflate(R.layout.item, parent, false);

        TextView textViewLabel = (TextView) view.findViewById(R.id.label);
        TextView textViewSubtitle = (TextView) view.findViewById(R.id.subtitle);
        String label = cursor.getString(cursor.getColumnIndex(DBTables.Fridge.COLUMN_NAME));


        String productCount = getProductCountInFridge(context,ID);

        textViewLabel.setText(label);
        textViewSubtitle.setText(productCount);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.isChecked);

        if (visibleCheckboxes){
            checkBox.setVisibility(View.VISIBLE);
        }
        else {
            checkBox.setVisibility(View.INVISIBLE);
        }
        // присваиваем чекбоксу обработчик
        checkBox.setOnCheckedChangeListener(myCheckChangeList);

        return view;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.isChecked);
        ID = cursor.getInt(cursor.getColumnIndex(DBTables.Fridge._ID));
        TextView textViewSubtitle = (TextView) view.findViewById(R.id.subtitle);
        String subtitle = getProductCountInFridge(context,ID);
        textViewSubtitle.setText(subtitle);
        checkBox.setTag(ID);
        if (fridgeIDs.contains(ID)){
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }



    }
    private String getProductCountInFridge(Context context,int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.Product.TABLE_NAME + " WHERE fridge_id = "+String.valueOf(id);
        Cursor cursor = db.rawQuery(query,null);
        return context.getResources().getString(R.string.product_count) +" "+ String.valueOf(cursor.getCount());
    }

    private CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // меняем данные товара (в корзине или нет)
            int position = (int) buttonView.getTag();
            if (isChecked){
                if (!fridgeIDs.contains(position)){
                    fridgeIDs.add(position);
                }
            }
            else {
                fridgeIDs.remove((Integer)position);
            }
        }
    };


    public ArrayList<Integer> getIDsOFCheckedFridges() {
        return fridgeIDs;
    }


}
