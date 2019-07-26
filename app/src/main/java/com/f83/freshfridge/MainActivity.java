package com.f83.freshfridge;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.f83.freshfridge.data.DBFridgeListHelper;
import com.f83.freshfridge.data.DBTables;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity{

    private DBFridgeListHelper dbHelper;
    private Toolbar toolbar;
    private MenuItem addFridge;
    private MenuItem deleteFridge;
    private MenuItem addProduct;
    private MenuItem deleteProduct;
    private MenuItem confirm;
    private MenuItem cancel;
    private static final int ALL_FOOD = 0;
    private static final int FRIDGES_ADD_OR_DELETE_FRIDGE = 1;
    private static final int FRIDGES_OK_CANCEL_FRIDGE = 2;
    private static final int FRIDGES_ADD_OR_DELETE_FOOD = 3;
    private static final int FRIDGES_OK_CANCEL_FOOD = 4;
    private static final int WASTE_DELETE_FOOD = 5;
    private static final int WASTE_OK_CANCEL_FOOD = 6;
    private static final int TO_BUY_LIST_ADD_OR_DELETE_FOOD = 7;
    private static final int TO_BUY_LIST_OK_CANCEL = 8;
    private int toolbarState;
    private String fragmentTag;
    public int selectedFridgeID = 0;
    private Fragment selectedFragment;
    Calendar date=Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBFridgeListHelper(this);
        toolbarState = FRIDGES_ADD_OR_DELETE_FRIDGE;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_fridges);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
        if (savedInstanceState == null) {
            createFridgesFragment(0);
        }
    }

    private void addFridge(View view){
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_fridge,null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK",null)
                .setNegativeButton("Cancel",null)
                .setView(dialogView)
                .setTitle(getString(R.string.dialog_add_fridge_title))
                .show();
        dialog.setTitle(getString(R.string.dialog_add_fridge_msg));
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        final EditText etFridgeName = (EditText)dialogView.findViewById(R.id.fridge_name);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFridgeName.getText().toString().equals("")){
                    String fridgeName = etFridgeName.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBTables.Fridge.COLUMN_NAME, fridgeName);
                    db.insert(DBTables.Fridge.TABLE_NAME, null, values);
                    db.close();
                    createFridgesFragment(0);
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(MainActivity.this, getString(R.string.dialog_add_fridge_incorrect_name), Toast.LENGTH_SHORT).show();
                }

            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addProduct(View view){
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product,null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK",null)
                .setNegativeButton("Cancel",null)
                .setView(dialogView)
                .setTitle(getString(R.string.dialog_add_product_title))
                .show();
        dialog.setTitle(getString(R.string.dialog_add_product_msg));
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        final EditText etProductName = (EditText)dialogView.findViewById(R.id.product_name);
        final EditText etExpirationDate = (EditText)dialogView.findViewById(R.id.expiration_date);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String sDay = String.format("%02d", day);
                String sMonth = String.format("%02d", month+1);
                String sYear = String.format("%02d", year);
                etExpirationDate.setText(String.valueOf(sYear)+"-"+String.valueOf(sMonth)+"-"+String.valueOf(sDay));
            }
        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        etExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etProductName.getText().toString().equals("") && !etExpirationDate.getText().toString().equals("")){
                    String productName = etProductName.getText().toString();
                    String expirationDate = etExpirationDate.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBTables.Product.COLUMN_NAME, productName);
                    values.put(DBTables.Product.COLUMN_EXPIRATION_DATE, expirationDate);
                    values.put(DBTables.Product.COLUMN_FRIDGE_ID, selectedFridgeID);
                    db.insert(DBTables.Product.TABLE_NAME, null, values);
                    db.close();
                    createFridgesFragment(0);
                    dialog.dismiss();
                }
                else {
                    if(!etProductName.getText().toString().equals("")){
                        Toast.makeText(MainActivity.this, getString(R.string.dialog_add_product_incorrect_name), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, getString(R.string.dialog_add_product_incorrect_expiration_date), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void addProductToBuyList(View view){
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product,null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK",null)
                .setNegativeButton("Cancel",null)
                .setView(dialogView)
                .setTitle(getString(R.string.dialog_add_product_title))
                .show();
        dialog.setTitle(getString(R.string.dialog_add_product_msg));
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        final EditText etProductName = (EditText)dialogView.findViewById(R.id.product_name);
        final EditText etExpirationDate = (EditText)dialogView.findViewById(R.id.expiration_date);
        etExpirationDate.setVisibility(View.GONE);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etProductName.getText().toString().equals("")){
                    String fridgeName = etProductName.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBTables.ToBuyList.COLUMN_NAME, fridgeName);
                    db.insert(DBTables.ToBuyList.TABLE_NAME, null, values);
                    db.close();
                    createToBuyListFragment(0);
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(MainActivity.this, getString(R.string.dialog_add_product_incorrect_name), Toast.LENGTH_SHORT).show();
                }

            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void deleteSelectedFridges(final FridgeListFragment fragment){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK",null)
                .setNegativeButton("Cancel",null)
                .setTitle(getString(R.string.dialog_delete_fridge_title))
                .setMessage(getString(R.string.dialog_delete_fridge_msg))
                .show();
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment.adapter.getIDsOFCheckedFridges().size() != 0){
                    StringBuilder builder = new StringBuilder("(");
                    for(int id:fragment.adapter.getIDsOFCheckedFridges()){
                        builder.append(id).append(",");
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append(")");

                    db.delete(DBTables.Fridge.TABLE_NAME,
                            DBTables.Fridge._ID + " in " + builder.toString(),
                            null);
                    db.delete(DBTables.Product.TABLE_NAME,
                            DBTables.Product.COLUMN_FRIDGE_ID + " in " + builder.toString(),
                            null);
                }
                createFridgesFragment(0);
                dialog.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void deleteSelectedProducts(final Fragment fragment){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK",null)
                .setNegativeButton("Cancel",null)
                .setTitle(getString(R.string.dialog_delete_product_title))
                .setMessage(getString(R.string.dialog_delete_product_msg))
                .show();
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment instanceof ProductListFragment){
                    if (((ProductListFragment)fragment).adapter.getIDsOFCheckedProducts().size() != 0){
                        StringBuilder builder = new StringBuilder("(");
                        for(int id:((ProductListFragment)fragment).adapter.getIDsOFCheckedProducts()){
                            builder.append(id).append(",");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        builder.append(")");

                        db.delete(DBTables.Product.TABLE_NAME,
                                DBTables.Product._ID + " in " + builder.toString(),
                                null);
                    }
                    createFridgesFragment(0);
                    dialog.dismiss();
                }
                if (fragment instanceof  WasteListFragment){
                    if (((WasteListFragment)fragment).adapter.getIDsOFCheckedProducts().size() != 0){
                        StringBuilder builder = new StringBuilder("(");
                        for(int id:((WasteListFragment)fragment).adapter.getIDsOFCheckedProducts()){
                            builder.append(id).append(",");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        builder.append(")");

                        db.delete(DBTables.Product.TABLE_NAME,
                                DBTables.Product._ID + " in " + builder.toString(),
                                null);
                    }
                    createWasteFragment(0);
                    dialog.dismiss();
                }
                if (fragment instanceof  ToBuyListFragment){
                    if (((ToBuyListFragment)fragment).adapter.getIDsOFCheckedProducts().size() != 0){
                        StringBuilder builder = new StringBuilder("(");
                        for(int id:((ToBuyListFragment)fragment).adapter.getIDsOFCheckedProducts()){
                            builder.append(id).append(",");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        builder.append(")");

                        db.delete(DBTables.ToBuyList.TABLE_NAME,
                                DBTables.ToBuyList._ID + " in " + builder.toString(),
                                null);
                    }
                    createToBuyListFragment(0);
                    dialog.dismiss();
                }

            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){

                        case R.id.action_all_food:
                            createAllFoodFragment();
                            toolbarState = ALL_FOOD;
                            invalidateOptionsMenu();
                            break;
                        case R.id.action_fridges:
                            createFridgesFragment(0);
                            if(selectedFridgeID == 0){
                                toolbarState = FRIDGES_ADD_OR_DELETE_FRIDGE;
                            }
                            else {
                                toolbarState = FRIDGES_ADD_OR_DELETE_FOOD;
                            }
                            invalidateOptionsMenu();

                            break;
                        case R.id.action_waste:
                            createWasteFragment(0);
                            toolbarState = WASTE_DELETE_FOOD;
                            invalidateOptionsMenu();

                            break;
                        case R.id.action_to_buy_list:
                            createToBuyListFragment(0);
                            toolbarState = TO_BUY_LIST_ADD_OR_DELETE_FOOD;
                            invalidateOptionsMenu();
                            break;
                    }
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem addFridge = menu.getItem(0);
        MenuItem deleteFridge =  menu.getItem(1);
        MenuItem addProduct =  menu.getItem(2);
        MenuItem deleteProduct =  menu.getItem(3);
        MenuItem confirm =  menu.getItem(4);
        MenuItem cancel =  menu.getItem(5);

        switch (this.toolbarState){
            case 0:
                addFridge.setVisible(false);
                deleteFridge.setVisible(false);
                addProduct.setVisible(false);
                deleteProduct.setVisible(false);
                confirm.setVisible(false);
                cancel.setVisible(false);
                break;
            case 1:
                addFridge.setVisible(true);
                deleteFridge.setVisible(true);
                addProduct.setVisible(false);
                deleteProduct.setVisible(false);
                confirm.setVisible(false);
                cancel.setVisible(false);
                break;
            case 3:
            case 7:
                addFridge.setVisible(false);
                deleteFridge.setVisible(false);
                addProduct.setVisible(true);
                deleteProduct.setVisible(true);
                confirm.setVisible(false);
                cancel.setVisible(false);
                break;
            case 5:
                addFridge.setVisible(false);
                deleteFridge.setVisible(false);
                addProduct.setVisible(false);
                deleteProduct.setVisible(true);
                confirm.setVisible(false);
                cancel.setVisible(false);
                break;
            case 2:
            case 4:
            case 6:
            case 8:
                addFridge.setVisible(false);
                deleteFridge.setVisible(false);
                addProduct.setVisible(false);
                deleteProduct.setVisible(false);
                confirm.setVisible(true);
                cancel.setVisible(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        switch (item.getItemId()){
            case R.id.action_add_fridge:
                addFridge(getCurrentFocus());
                break;
            case R.id.action_delete_fridge:
                toolbarState++;
                invalidateOptionsMenu();
                createFridgesFragment(1);
                break;
            case R.id.action_add_product:
                if (fragment instanceof FridgesFragment){
                    addProduct(getCurrentFocus());
                }
                if (fragment instanceof ToBuyListFragment){
                    addProductToBuyList(getCurrentFocus());
                }
                break;
            case R.id.action_delete_product:
                toolbarState++;
                invalidateOptionsMenu();
                if(fragment instanceof FridgesFragment){
                    createFridgesFragment(1);
                }
                if (fragment instanceof WasteListFragment){
                    createWasteFragment(1);
                }
                if (fragment instanceof ToBuyListFragment){
                    createToBuyListFragment(1);
                }
                break;
            case R.id.action_cancel:
                toolbarState--;
                invalidateOptionsMenu();
                if (fragment instanceof FridgesFragment){
                    createFridgesFragment(0);
                }
                break;
            case R.id.action_confirm:
                toolbarState--;
                invalidateOptionsMenu();
                if (fragment instanceof FridgesFragment){
                    Fragment childFragment = fragment.getChildFragmentManager().findFragmentById(R.id.fragment_container);
                    if (childFragment instanceof FridgeListFragment){
                        deleteSelectedFridges((FridgeListFragment) childFragment);
                    }
                    if (childFragment instanceof ProductListFragment){
                        deleteSelectedProducts((ProductListFragment) childFragment);
                    }
                }
                if (fragment instanceof WasteListFragment){
                    deleteSelectedProducts((WasteListFragment) fragment);
                }
                if (fragment instanceof ToBuyListFragment){
                    deleteSelectedProducts((ToBuyListFragment) fragment);
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (toolbarState % 2 == 0){
            toolbarState--;
            invalidateOptionsMenu();
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment!= null && fragment.getChildFragmentManager().getBackStackEntryCount() > 1) {
            if (fragment instanceof FridgesFragment){
                ((FridgesFragment) fragment).setSelectedFridgeID();
                fragment.getChildFragmentManager().popBackStack();
                selectedFridgeID = 0;
                toolbarSetFridgesState();
            }
        }
        else{
            super.onBackPressed();
        }
    }

    private void createAllFoodFragment(){
        selectedFragment = AllFoodFragment.newInstance();
        fragmentTag = getString(R.string.all_food);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,selectedFragment,fragmentTag);
        transaction.commit();
    }

    private void createFridgesFragment(int visibility){
        selectedFragment = FridgesFragment.newInstance(visibility,selectedFridgeID);
        fragmentTag = getString(R.string.fridges);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,selectedFragment,fragmentTag);
        transaction.commit();
    }

    private void createWasteFragment(int visibility){
        selectedFragment = WasteListFragment.newInstance(visibility);
        fragmentTag = getString(R.string.waste);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,selectedFragment,fragmentTag);
        transaction.commit();
    }
    private void createToBuyListFragment(int visibility){
        selectedFragment = ToBuyListFragment.newInstance(visibility);
        fragmentTag = getString(R.string.to_buy_list);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,selectedFragment,fragmentTag);
        transaction.commit();
    }

    public void toolbarSetFridgesState(){
        if(selectedFridgeID == 0){
            toolbarState = FRIDGES_ADD_OR_DELETE_FRIDGE;
        }
        else {
            toolbarState = FRIDGES_ADD_OR_DELETE_FOOD;
        }
        invalidateOptionsMenu();
    }

}
