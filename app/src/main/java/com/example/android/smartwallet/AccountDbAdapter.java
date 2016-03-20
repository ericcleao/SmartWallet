package com.example.android.smartwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eric Cerqueira on 19/03/2016.
 */


public class AccountDbAdapter {
    private static final String DATABASE_NAME = "SMARTWALLET_ACCOUNTS";
    private static final int DATABASE_VERSION = 1;


    private static final String ACCOUNT_TABLE = "Accounts";
    private static final String ACCOUNT_ID = "_id";
    private static final String ACCOUNT_NAME = "name";
    private static final String ACCOUNT_BALANCE = "balance";
    private static final String ACCOUNT_TYPE = "type";
    private static final String ACCOUNT_LIMIT = "limit";
    public static final String[] ACCOUNT_FIELDS = new String[]{
            ACCOUNT_ID,
            ACCOUNT_NAME,
            ACCOUNT_BALANCE,
            ACCOUNT_TYPE,
            ACCOUNT_LIMIT,
    };
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ACCOUNT_TABLE + " (" +
            ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ACCOUNT_NAME + " TEXT, " +
            ACCOUNT_BALANCE + " REAL, " +
            ACCOUNT_TYPE + " TEXT NULL, " +
            ACCOUNT_LIMIT + " REAL" +
            ");";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;

    private AccountDbHelper dbHelper;
    private SQLiteDatabase database;
    private final Context context;

    public AccountDbAdapter(Context context) {
        this.context = context;
    }

    public AccountDbAdapter open() throws SQLException {
        dbHelper = new AccountDbHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public void upgrade() throws SQLException {
        dbHelper = new AccountDbHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(database, 1, 2);
    }

    public long createAccount(Account accountToCreate) {
        ContentValues initialValues = new ContentValues();
        if (accountToCreate.getName() != null)
            initialValues.put(ACCOUNT_NAME, accountToCreate.getName());
        if (accountToCreate.getBalance() != 0)
            initialValues.put(ACCOUNT_BALANCE, accountToCreate.getBalance());
        if (accountToCreate instanceof Credit) {
            initialValues.put(ACCOUNT_TYPE, "Credit");
            initialValues.put(ACCOUNT_LIMIT, ((Credit) accountToCreate).getLimit());
        } else if (accountToCreate instanceof Debit)
            initialValues.put(ACCOUNT_TYPE, "Debit");

        return database.insert(ACCOUNT_TABLE, null, initialValues);
    }

    public boolean deleteAccount(long rowId) {
        return database.delete(ACCOUNT_TABLE, ACCOUNT_ID + "=" + rowId, null) > 0;
    }

    public boolean deleteAccount(String name) {
        return database.delete(ACCOUNT_TABLE, ACCOUNT_NAME + "=" + name, null) > 0;
    }

    public Cursor fetchByType(String type) throws SQLException {
        Cursor mCursor = database.query(true, ACCOUNT_TABLE, ACCOUNT_FIELDS, ACCOUNT_TYPE + "=" + type, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchByName(String name) throws SQLException {
        Cursor mCursor = database.query(true, ACCOUNT_TABLE, ACCOUNT_FIELDS, ACCOUNT_NAME + "=" + name, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Account getAccountByName(String name) {
        Account account = null;
        Cursor accountCursor = fetchByName(name);
        if (accountCursor.moveToFirst()) {
            account = getAccountFromCursor(accountCursor);
        }
        if (accountCursor != null) {
            accountCursor.close();
        }
        return account;
    }

    public static Account getAccountFromCursor(Cursor cursor) {
        Account account;
        String name = cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME));
        double balance = cursor.getDouble(cursor.getColumnIndex(ACCOUNT_BALANCE));
        String type = cursor.getString(cursor.getColumnIndex(ACCOUNT_TYPE));
        if (type.equals("Credit")) {
            double limit = cursor.getDouble(cursor.getColumnIndex(ACCOUNT_LIMIT));
            account = new Credit(name, balance, limit);
        } else {
            account = new Debit(name, balance);
        }
        return account;
    }

    public boolean updateAccount(String name, Account accountToUpdate) {
        ContentValues updateValues = new ContentValues();
        if (accountToUpdate.getName() != null)
            updateValues.put(ACCOUNT_NAME, accountToUpdate.getName());
        if (accountToUpdate.getBalance() != 0)
            updateValues.put(ACCOUNT_BALANCE, accountToUpdate.getBalance());
        if (accountToUpdate instanceof Credit)
            if (((Credit) accountToUpdate).getLimit() != 0)
                updateValues.put(ACCOUNT_LIMIT, ((Credit) accountToUpdate).getLimit());

        return database.update(ACCOUNT_TABLE, updateValues, ACCOUNT_NAME + "=" + DatabaseUtils.sqlEscapeString(name), null) > 0;
    }

    public static class AccountDbHelper extends SQLiteOpenHelper {
        AccountDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}
