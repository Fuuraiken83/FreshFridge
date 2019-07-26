package com.f83.freshfridge;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Product> products;
    Cursor cursor;
    Boolean visibleCheckboxes;


    CustomListAdapter(Context context, Cursor cursor, ArrayList<Product> products, Boolean visibleCheckboxes) {
        this.context = context;
        this.cursor = cursor;
        this.products = products;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.visibleCheckboxes = visibleCheckboxes;
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return products.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }


    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item, parent, false);
        }

        Product product = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.label)).setText(product.label);
        ((TextView) view.findViewById(R.id.subtitle)).setText(product.expirationDate);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.isChecked);
        if (visibleCheckboxes){
            checkBox.setVisibility(View.VISIBLE);
        }
        else {
            checkBox.setVisibility(View.INVISIBLE);
        }
        // присваиваем чекбоксу обработчик
        checkBox.setOnCheckedChangeListener(myCheckChangeList);
        // пишем позицию
        checkBox.setTag(position);
        // заполняем данными из товаров: в корзине или нет
        checkBox.setChecked(product.isChecked);
        return view;
    }

    // товар по позиции
    Product getProduct(int position) {
        return ((Product) getItem(position));
    }

    // содержимое корзины
    ArrayList<Product> getCheckedProducts() {
        ArrayList<Product> checkedList = new ArrayList<Product>();
        for (Product p : products) {
            // если в корзине
            if (p.isChecked)
                checkedList.add(p);
        }
        return checkedList;
    }

    // обработчик для чекбоксов
    OnCheckedChangeListener myCheckChangeList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // меняем данные товара (в корзине или нет)
            getProduct((Integer) buttonView.getTag()).isChecked = isChecked;
        }
    };



}
