package com.hfad.freshfridge;


import android.content.Context;
import android.database.Cursor;
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


public class CustomToBuyCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private boolean visibleCheckboxes;
    private DBFridgeListHelper dbHelper;

    ArrayList<Integer> productIDs;
    private int ID;

    public CustomToBuyCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        productIDs = new ArrayList<>();
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
        View view = cursorInflater.inflate(R.layout.item, parent, false);
        TextView textViewLabel = (TextView) view.findViewById(R.id.label);
        TextView textViewSubtitle = (TextView) view.findViewById(R.id.subtitle);
        String label = cursor.getString(cursor.getColumnIndex(DBTables.ToBuyList.COLUMN_NAME));
        textViewLabel.setText(label);
        textViewSubtitle.setText("");
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.isChecked);
        if (visibleCheckboxes){
            checkBox.setVisibility(View.VISIBLE);
        }
        else {
            checkBox.setVisibility(View.INVISIBLE);
        }
        checkBox.setOnCheckedChangeListener(myCheckChangeList);
        return view;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.isChecked);
        ID = cursor.getInt(cursor.getColumnIndex(DBTables.Product._ID));
        checkBox.setTag(ID);
        if (productIDs.contains(ID)){
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }
    }

    private CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // меняем данные товара (в корзине или нет)
            int position = (int) buttonView.getTag();
            if (isChecked){
                if (!productIDs.contains(position)){
                    productIDs.add(position);
                }
            }
            else {
                productIDs.remove((Integer)position);
            }
        }
    };

    public ArrayList<Integer> getIDsOFCheckedProducts() {
        return productIDs;
    }
}
