package com.f83.freshfridge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.f83.freshfridge.data.DBTables.Fridge;
import com.f83.freshfridge.data.DBTables.Product;
import com.f83.freshfridge.data.DBTables.ToBuyList;


public class DBFridgeListHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DBFridgeListHelper.class.getSimpleName();

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "freshfridge.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;

    /**
     *
     * Конструктор {@link DBFridgeListHelper}.
     *
     * @param context Контекст приложения
     */
     public DBFridgeListHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *Создание таблицы
     */
    @Override
    public void onCreate(SQLiteDatabase db){

         //строка создания таблицы с холодильниками
         String SQL_CREATE_FRIDGE_TABLE = String.format("CREATE TABLE %s " +
                         "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "%s TEXT NOT NULL);",
                 Fridge.TABLE_NAME,
                 Fridge._ID,
                 Fridge.COLUMN_NAME);

        //строка создания таблицы с продуктами
        String SQL_CREATE_PRODUCT_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "%s INTEGER);",
                Product.TABLE_NAME,
                Product._ID,
                Product.COLUMN_NAME,
                Product.COLUMN_FRIDGE_ID,
                Product.COLUMN_EXPIRATION_DATE);

        String SQL_CREATE_TO_BUY_LIST_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +
                        "%s BOOLEAN);",
                ToBuyList.TABLE_NAME,
                ToBuyList._ID,
                ToBuyList.COLUMN_NAME,
                ToBuyList.COLUMN_IS_BOUGHT);

        //Выполняем создание таблиц
        db.execSQL(SQL_CREATE_FRIDGE_TABLE);
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
        db.execSQL(SQL_CREATE_TO_BUY_LIST_TABLE);
    }
    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + Fridge.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToBuyList.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(db);
    }
}
