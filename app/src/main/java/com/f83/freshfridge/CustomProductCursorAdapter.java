package com.f83.freshfridge;


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

import com.f83.freshfridge.data.DBFridgeListHelper;
import com.f83.freshfridge.data.DBTables;

import java.util.ArrayList;


public class CustomProductCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private boolean visibleCheckboxes;
    private int showSubtitle;
    private DBFridgeListHelper dbHelper;
    ArrayList<Integer> productIDs;
    private int ID;

    // Default constructor
    public CustomProductCursorAdapter(Context context, Cursor cursor, int flags,int show) {
        super(context, cursor, flags);
        productIDs = new ArrayList<>();
        if (flags == 1){
            visibleCheckboxes = true;
        }
        else{
            visibleCheckboxes = false;
        }
        showSubtitle = show;

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
        String label = cursor.getString(cursor.getColumnIndex(DBTables.Product.COLUMN_NAME));
        if (showSubtitle == 1){
            String expirationDate = cursor.getString(cursor.getColumnIndex(DBTables.Product.COLUMN_EXPIRATION_DATE));
            String[] array = new String[3];
            array = expirationDate.split("-");
            expirationDate = context.getResources().getString(R.string.cursor_product_subtitle_expiration_date) + " "
                    + array[2]+"."+array[1]+"."+array[0];
            textViewLabel.setText(label);
            textViewSubtitle.setText(expirationDate);
        }
        else{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String query = "SELECT " + DBTables.Fridge.COLUMN_NAME
                    + " FROM " + DBTables.Fridge.TABLE_NAME
                    + " WHERE " + DBTables.Fridge._ID + " = "
                    + cursor.getString(cursor.getColumnIndex(DBTables.Product.COLUMN_FRIDGE_ID)) ;
            Cursor newCursor =  db.rawQuery(query,null);
            newCursor.moveToFirst();
            String expirationDate = context.getResources().getString(R.string.cursor_product_subtitle_fridge)
                    + " "
                    + newCursor.getString(newCursor.getColumnIndex(DBTables.Fridge.COLUMN_NAME));
            textViewLabel.setText(label);
            textViewSubtitle.setText(expirationDate);
            newCursor.close();
        }


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
