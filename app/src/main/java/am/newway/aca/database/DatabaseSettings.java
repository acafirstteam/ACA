package am.newway.aca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import am.newway.aca.template.Settings;

public class DatabaseSettings extends SQLiteOpenHelper {

    private final String TAG = "DatabaseSettings";

    private static final int DATABASE_VERSION = 1;

    // Create DB
    private static final String DATABASE_NAME = "Settings.db";

    //Settings table name
    private static final String TABLE_SETTINGS = "settings";

    //Table column names
    private static final String COLUMN_STUDENT_ID = "settings_id";
    private static final String COLUMN_SETTINGS_LOGIN = "login";
    private static final String COLUMN_SETTINGS_NOTIFICATIONS = "notifications";
    private static final String COLUMN_SETTINGS_LANGUAGE = "language";

    //Create table SQL query

    private String CREATE_USER_TABLE = " CREATE TABLE " + TABLE_SETTINGS + "("
            + COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SETTINGS_LOGIN + " TEXT, "
            + COLUMN_SETTINGS_NOTIFICATIONS + " TEXT, "
            + COLUMN_SETTINGS_LANGUAGE + " TEXT"
            + ")";

    //Drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_SETTINGS;

    public DatabaseSettings(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    //Add settings
    public void setSettings(Settings settings) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTINGS_LOGIN, settings.isLoginCheck());
        values.put(COLUMN_SETTINGS_NOTIFICATIONS, settings.isCheckNotification());
        values.put(COLUMN_SETTINGS_LANGUAGE, settings.getLanguageCheck());


        //Insert Row
        db.insert(TABLE_SETTINGS, null, values);
        db.close();
        Log.d(TAG, "------------------------------------------------------Data inserted");
    }

    public Settings getSettings() {

        boolean login, notification;
        String language;
        Settings settings;
        int checkLogin, checkNotify;
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor;
        String queryName = "SELECT * FROM  "
                + TABLE_SETTINGS
                + " LIMIT " + "1";

        cursor = myDB.rawQuery(queryName, null);
        cursor.moveToFirst();
        checkLogin = Integer.valueOf(cursor.getString(1));
        if (checkLogin == 1){
            login = true;
        }else{
            login = false;
        }
        checkNotify = Integer.valueOf(cursor.getString(2));
        if (checkNotify == 1){
            notification = true;
        }else{
            notification = false;
        }
        language = cursor.getString(3);



        settings = new Settings(
                login,
                notification,
                language
        );
        Log.d(TAG, "-----------------------Get Student Handled");

        return settings;
    }

    public void updateSettings(Settings settings) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SETTINGS_LOGIN, settings.isLoginCheck());
        contentValues.put(COLUMN_SETTINGS_NOTIFICATIONS, settings.isCheckNotification());
        contentValues.put(COLUMN_SETTINGS_LANGUAGE, settings.getLanguageCheck());

        myDB.update(TABLE_SETTINGS,
                contentValues,
                COLUMN_STUDENT_ID + " = 1 ",
                null);

        Log.d(TAG, "--------------------------------------Database Updated");
    }

    public boolean checkDB(){
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_SETTINGS, null);
        boolean rowExists;
        if (cursor.moveToFirst()){
            rowExists = true;
        }else {
            rowExists = false;
        }
        Log.d(TAG, "------------------DB checked");
        return rowExists;

    }


}
