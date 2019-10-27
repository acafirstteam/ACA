package am.newway.aca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import am.newway.aca.template.Settings;
import am.newway.aca.template.Student;
import am.newway.aca.template.Visit;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public
class DatabaseHelper extends SQLiteOpenHelper implements Student.OnStudentChangeListener,
        Settings.OnSettingsChangeListener {

    private static final String TAG = "Database";
    private final static String ENGLISH = "en";
    private Student student;
    private Settings settings;
    private Visit visit;
    private static volatile DatabaseHelper database = null;
    public Context context;

    private static final int DATABASE_VERSION = 1;

    // Create DB
    private static final String DATABASE_NAME = "aca.db";

    //Student table name
    private static final String TABLE_STUDENT = "student";
    private static final String TABLE_SETTINGS = "settings";

    //Student table column names
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_STUDENT_EMAIL = "student_email";
    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_STUDENT_SURNAME = "student_surename";
    private static final String COLUMN_STUDENT_AGE = "student_age";
    private static final String COLUMN_STUDENT_PHONE = "student_phone";
    private static final String COLUMN_STUDENT_PICTURE = "student_picture";
    private static final String COLUMN_STUDENT_TOKEN = "student_token";
    private static final String COLUMN_STUDENT_VERIFICATION = "student_verification";
    private static final String COLUMN_STUDENT_COURSE = "student_course";
    private static final String COLUMN_STUDENT_TYPE = "student_type";

    //Settings table column names
    private static final String COLUMN_SETTINGS_ID = "id";
    private static final String COLUMN_SETTINGS_LOGIN = "login";
    private static final String COLUMN_SETTINGS_NOTIFICATIONS = "notifications";
    private static final String COLUMN_SETTINGS_LANGUAGE = "language";
    private static final String COLUMN_SETTINGS_ANIMATION= "first_animation";

    public static
    DatabaseHelper getInstance ( Context context ) {
        if ( database == null )
            synchronized ( DatabaseHelper.class ) {
                if ( database == null ) {
                    database = new DatabaseHelper( context );
                    database.getStudent();
                    if ( database.getSettings() == null ) {
                        Log.e( TAG , "getInstance: Settings is null, Created new Settings " );
                        database.setSettings( new Settings( true , true , ENGLISH, false ) );
                        database.getSettings().addOnSettingsChangeListener( database );
                    }
                }
            }
        return database;
    }

    //Create table SQL query
    private String CREATE_STUDENT_TABLE = String.format(
            " CREATE TABLE IF NOT EXISTS %s(%s TEXT,%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                    "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER )" ,
            TABLE_STUDENT , COLUMN_STUDENT_ID , COLUMN_STUDENT_EMAIL , COLUMN_STUDENT_NAME ,
            COLUMN_STUDENT_SURNAME , COLUMN_STUDENT_AGE , COLUMN_STUDENT_PHONE ,
            COLUMN_STUDENT_PICTURE , COLUMN_STUDENT_TOKEN , COLUMN_STUDENT_VERIFICATION ,
            COLUMN_STUDENT_COURSE , COLUMN_STUDENT_TYPE );

    //Create table SQL query
    private String CREATE_SETTINGS_TABLE = String.format(
            " CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT, %s TEXT, %s TEXT)" ,
            TABLE_SETTINGS , COLUMN_SETTINGS_ID , COLUMN_SETTINGS_LOGIN ,
            COLUMN_SETTINGS_NOTIFICATIONS , COLUMN_SETTINGS_LANGUAGE, COLUMN_SETTINGS_ANIMATION );

    private
    DatabaseHelper ( Context context ) {
        super( context , DATABASE_NAME , null , DATABASE_VERSION );
        this.context = context;
        settings = getSettings();
    }

    @Override
    public
    void onCreate ( SQLiteDatabase db ) {
        db.execSQL( CREATE_STUDENT_TABLE );
        db.execSQL( CREATE_SETTINGS_TABLE );
    }

    @Override
    public
    void onUpgrade ( SQLiteDatabase db , int oldVersion , int newVersion ) {

    }

    public
    Visit getVisit () {
        return visit;
    }

    public
    void setVisit ( final Visit visit ) {
        this.visit = visit;
    }

    //Add student
    public
    void setStudent ( @Nullable Student student ) {

        if ( student == null )
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        long nCount = DatabaseUtils.longForQuery( db ,
                String.format( "SELECT COUNT(*) FROM %s" , TABLE_STUDENT ) , null );

        this.student = student;

        if ( nCount > 0 ) {
            updateStudent( student );
            return;
        }

        ContentValues values = new ContentValues();
        values.put( COLUMN_STUDENT_ID , student.getId() );
        values.put( COLUMN_STUDENT_EMAIL , student.getEmail() );
        values.put( COLUMN_STUDENT_NAME , student.getName() );
        values.put( COLUMN_STUDENT_SURNAME , student.getSurname() );
        values.put( COLUMN_STUDENT_AGE , student.getAge() );
        values.put( COLUMN_STUDENT_PHONE , student.getPhone() );
        values.put( COLUMN_STUDENT_PICTURE , student.getPicture() );
        values.put( COLUMN_STUDENT_TOKEN , student.getToken() );
        values.put( COLUMN_STUDENT_VERIFICATION , student.isVerified() );
        values.put( COLUMN_STUDENT_COURSE , student.getCourse() );
        values.put( COLUMN_STUDENT_TYPE , student.getType() );

        //Insert Row
        db.insert( TABLE_STUDENT , null , values );
        db.close();
        Log.d( TAG , "------------------------------------------------------Data inserted" );
    }

    @NonNull
    public
    Student getStudent () {

        Cursor cursor;
        SQLiteDatabase myDB = this.getReadableDatabase();
        String queryName = String.format( "SELECT * FROM  %s LIMIT 1" , TABLE_STUDENT );
        cursor = myDB.rawQuery( queryName , null );

        if ( cursor.getCount() > 0 && student == null ) {
            cursor.moveToFirst();

            student = new Student();

            student.addOnStudentChangeListener( this );

            student.setId( cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_ID ) ) );
            student.setEmail( cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_EMAIL ) ) );
            student.setName( cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_NAME ) ) );
            student.setSurname(
                    cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_SURNAME ) ) );
            student.setAge( Integer.valueOf(
                    cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_AGE ) ) ) );
            student.setPhone( cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_PHONE ) ) );
            student.setPicture(
                    cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_PICTURE ) ) );
            student.setToken( cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_TOKEN ) ) );
            student.setVerified( Integer.valueOf(
                    cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_VERIFICATION ) ) ) ==
                    1 );
            student.setCourse( cursor.getString( cursor.getColumnIndex( COLUMN_STUDENT_COURSE ) ) );
            student.setType( cursor.getInt( cursor.getColumnIndex( COLUMN_STUDENT_TYPE ) ) );

            Log.d( TAG , "-----------------------Get Student Handled" );
        }else if(student == null)
            student = new Student(  );

        cursor.close();

        return student;
    }

    private
    void updateStudent ( @NonNull Student student ) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put( COLUMN_STUDENT_ID , student.getId() );
        contentValues.put( COLUMN_STUDENT_EMAIL , student.getEmail() );
        //contentValues.put( COLUMN_STUDENT_NAME , student.getName() );
        contentValues.put( COLUMN_STUDENT_SURNAME , student.getSurname() );
        contentValues.put( COLUMN_STUDENT_AGE , student.getAge() );
        //contentValues.put( COLUMN_STUDENT_PHONE , student.getPhone() );
        contentValues.put( COLUMN_STUDENT_PICTURE , student.getPicture() );
        contentValues.put( COLUMN_STUDENT_TOKEN , student.getToken() );
        contentValues.put( COLUMN_STUDENT_VERIFICATION , student.isVerified() );
        contentValues.put( COLUMN_STUDENT_COURSE , student.getCourse() );
        contentValues.put( COLUMN_STUDENT_TYPE , student.getType() );

        myDB.update( TABLE_STUDENT , contentValues ,
                String.format( "%s in (SELECT %s FROM %s LIMIT 1) " , COLUMN_STUDENT_ID ,
                        COLUMN_STUDENT_ID , TABLE_SETTINGS ) , null );

        Log.e( TAG , "--------------------------------------Student Updated" );
    }

    //Add settings
    private
    void setSettings ( Settings setting ) {

        SQLiteDatabase db = this.getWritableDatabase();

        long nCount = DatabaseUtils.longForQuery( db ,
                String.format( "SELECT COUNT(*) FROM %s" , TABLE_SETTINGS ) , null );

        if ( nCount > 0 ) {
            updateSettings( setting );
            return;
        }

        ContentValues values = new ContentValues();
        values.put( COLUMN_SETTINGS_LOGIN , setting.isLogin() );
        values.put( COLUMN_SETTINGS_NOTIFICATIONS , setting.isNotification() );
        values.put( COLUMN_SETTINGS_LANGUAGE , setting.getLanguage() );
        values.put( COLUMN_SETTINGS_ANIMATION , setting.isFirstAnimation() );

        //Insert Row
        db.insert( TABLE_SETTINGS , null , values );
        db.close();
        Log.d( TAG , "------------------------------------------------------Data inserted" );
    }

    public
    Settings getSettings () {

        if ( settings != null ) {
            Log.e( TAG , "getSettings: " + settings.getLanguage()  );
            return settings;
        }

        settings = new Settings();
        settings.addOnSettingsChangeListener( this );

        Cursor cursor;
        SQLiteDatabase myDB = this.getReadableDatabase();
        String queryName = String.format( "SELECT * FROM  %s LIMIT 1" , TABLE_SETTINGS );

        cursor = myDB.rawQuery( queryName , null );
        if ( cursor.getCount() > 0 ) {
            cursor.moveToFirst();

            Log.e( TAG , "getSettings: Reading new Settings language " +
                    cursor.getString( cursor.getColumnIndex( COLUMN_SETTINGS_LANGUAGE ) ) );
            Log.e( TAG , "getSettings: Reading new Settings animation " +
                    cursor.getString( cursor.getColumnIndex( COLUMN_SETTINGS_ANIMATION ) ) );

            settings.setLogin( cursor.getString( cursor.getColumnIndex( COLUMN_SETTINGS_LOGIN ) )
                    .equals( "1" ) );
            settings.setLanguage(
                    cursor.getString( cursor.getColumnIndex( COLUMN_SETTINGS_LANGUAGE ) ) ,
                    context );
            settings.setNotification(
                    cursor.getString( cursor.getColumnIndex( COLUMN_SETTINGS_NOTIFICATIONS ) )
                            .equals( "1" ) );
            settings.setFirstAnimation(
                    cursor.getString( cursor.getColumnIndex( COLUMN_SETTINGS_ANIMATION ) )
                            .equals( "1" ) );
        }
        cursor.close();

        return settings;
    }

    private
    void updateSettings ( Settings setting ) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put( COLUMN_SETTINGS_LOGIN , setting.isLogin() );
        contentValues.put( COLUMN_SETTINGS_NOTIFICATIONS , setting.isNotification() );
        contentValues.put( COLUMN_SETTINGS_LANGUAGE , setting.getLanguage() );
        contentValues.put( COLUMN_SETTINGS_ANIMATION , setting.isFirstAnimation() );

        myDB.update( TABLE_SETTINGS , contentValues , COLUMN_SETTINGS_ID + " = 1 " , null );

        Log.d( TAG , "--------------------------------------Settings Updated" );
    }

    @Override
    public
    void OnStudentChanged () {
        Log.e( TAG , "OnStudentChanged: " );
        //setStudent( student );
    }

    @Override
    public
    void OnSettingsChanged () {
        setSettings( settings );
    }

    public
    void deleteStudent () {
        SQLiteDatabase myDB = this.getWritableDatabase();
        myDB.delete( TABLE_SETTINGS , "", null);
    }
}
