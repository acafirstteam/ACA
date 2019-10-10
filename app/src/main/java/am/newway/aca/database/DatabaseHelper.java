package am.newway.aca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import am.newway.aca.template.Student;
import androidx.annotation.NonNull;

public
class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "Database";

    private static final int DATABASE_VERSION = 1;

    // Create DB
    private static final String DATABASE_NAME = "StudentInfo.db";

    //User table name
    private static final String TABLE_STUDENT = "student";

    //User table column names
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_STUDENT_EMAIL = "student_email";
    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_STUDENT_SURENAME = "student_surename";
    private static final String COLUMN_STUDENT_AGE = "student_age";
    private static final String COLUMN_STUDENT_PHONE = "student_phone";
    private static final String COLUMN_STUDENT_PICTURE = "student_picture";

    //Create table SQL query

    private String CREATE_USER_TABLE = " CREATE TABLE " + TABLE_STUDENT + "(" + COLUMN_STUDENT_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_STUDENT_EMAIL + " TEXT, " +
            COLUMN_STUDENT_NAME + " TEXT, " + COLUMN_STUDENT_SURENAME + " TEXT, " +
            COLUMN_STUDENT_AGE + " TEXT, " + COLUMN_STUDENT_PHONE + " TEXT, " +
            COLUMN_STUDENT_PICTURE + " TEXT" + ")";

    //Drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_STUDENT;

    public
    DatabaseHelper ( Context context ) {
        super( context , DATABASE_NAME , null , DATABASE_VERSION );
    }


    @Override
    public
    void onCreate ( SQLiteDatabase db ) {
        db.execSQL( CREATE_USER_TABLE );
    }

    @Override
    public
    void onUpgrade ( SQLiteDatabase db , int oldVersion , int newVersion ) {

        db.execSQL( DROP_USER_TABLE );
        onCreate( db );

    }

    //Add student
    public
    void setStudent ( @NonNull Student student ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( COLUMN_STUDENT_EMAIL , student.getEmail() );
        values.put( COLUMN_STUDENT_NAME , student.getName() );
        values.put( COLUMN_STUDENT_SURENAME , student.getSurname() );
        values.put( COLUMN_STUDENT_AGE , student.getAge() );
        values.put( COLUMN_STUDENT_PHONE , student.getPhone() );
        values.put( COLUMN_STUDENT_PICTURE , student.getPicture() );

        //Insert Row
        db.insert( TABLE_STUDENT , null , values );
        db.close();
        Log.d( TAG , "------------------------------------------------------Data inserted" );
    }

    public
    Student getStudent () {

        String mail, name, surename, phone, picture;
        int age;
        Student student;
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor;
        String[] args = { COLUMN_STUDENT_ID };
        String queryName = "SELECT * FROM  " + TABLE_STUDENT + " LIMIT " + "1";

        cursor = myDB.rawQuery( queryName , null );
        cursor.moveToFirst();
        mail = cursor.getString( 1 );
        name = cursor.getString( 2 );
        surename = cursor.getString( 3 );
        age = Integer.valueOf( cursor.getString( 4 ) );
        phone = cursor.getString( 5 );
        picture = cursor.getString( 6 );

        student = new Student( age , mail , name , phone , picture , surename , "" );
        Log.d( TAG , "-----------------------Get Student Handled" );

        return student;

    }

    public
    void updateSudent ( @NonNull Student student ) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put( COLUMN_STUDENT_EMAIL , student.getEmail() );
        contentValues.put( COLUMN_STUDENT_NAME , student.getName() );
        contentValues.put( COLUMN_STUDENT_SURENAME , student.getSurname() );
        contentValues.put( COLUMN_STUDENT_AGE , student.getAge() );
        contentValues.put( COLUMN_STUDENT_PHONE , student.getPhone() );
        contentValues.put( COLUMN_STUDENT_PICTURE , student.getPicture() );

        myDB.update( TABLE_STUDENT , contentValues , COLUMN_STUDENT_ID + " = 1 " , null );

        Log.d( TAG , "--------------------------------------Database Updated" );
    }
}
