package com.f83.freshfridge.data;

import android.provider.BaseColumns;

public class DBTables {
    private DBTables(){}
    public static final class Fridge implements BaseColumns{
        public final static String TABLE_NAME = "fridges";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
    }
    public static final class Product implements BaseColumns{
        public final static String TABLE_NAME = "products";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_FRIDGE_ID = "fridge_id";
        public final static String COLUMN_EXPIRATION_DATE= "expiration_date";
    }
    public static final class ToBuyList implements BaseColumns{
        public final static String TABLE_NAME = "to_buy_list";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_IS_BOUGHT = "is_bought";

    }
}
