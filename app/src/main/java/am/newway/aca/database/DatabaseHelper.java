package am.newway.aca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import am.newway.aca.template.Student;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "Database";

    private static final int DATABASE_VERSION = 1;

    // Create DB
    private static final String DATABASE_NAME = "StudentInfo.db";

    //Student table name
    private static final String TABLE_STUDENT = "student";

    //Table column names
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_STUDENT_EMAIL = "student_email";
    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_STUDENT_SURENAME = "student_surename";
    private static final String COLUMN_STUDENT_AGE = "student_age";
    private static final String COLUMN_STUDENT_PHONE = "student_phone";
    private static final String COLUMN_STUDENT_PICTURE = "student_picture";
    private static final String COLUMN_STUDENT_TOKEN = "student_id";
    private static final String COLUMN_STUDENT_VERIFICATION = "student_verification";
    private static final String COLUMN_STUDENT_COURSE = "student_course";
    private static final String COLUMN_STUDENT_TYPE = "student_type";

    //Create table SQL query

    private String CREATE_USER_TABLE = " CREATE TABLE " + TABLE_STUDENT + "("
            + COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_STUDENT_EMAIL + " TEXT, "
            + COLUMN_STUDENT_NAME + " TEXT, "
            + COLUMN_STUDENT_SURENAME + " TEXT, "
            + COLUMN_STUDENT_AGE + " TEXT, "
            + COLUMN_STUDENT_PHONE + " TEXT, "
            + COLUMN_STUDENT_PICTURE + " TEXT, "
            + COLUMN_STUDENT_TOKEN + " TEXT, "
            + COLUMN_STUDENT_VERIFICATION + " TEXT, "
            + COLUMN_STUDENT_COURSE + " TEXT, "
            + COLUMN_STUDENT_TYPE + " INTEGER "
            + ")";

    //Drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_STUDENT;

    public DatabaseHelper(Context context) {
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

    //Add student
    public void setStudent(Student student) {

        if (checkDB()){
            updateStudent(student);
        }else{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_STUDENT_EMAIL, student.getEmail());
            values.put(COLUMN_STUDENT_NAME, student.getName());
            values.put(COLUMN_STUDENT_SURENAME, student.getSurname());
            values.put(COLUMN_STUDENT_AGE, student.getAge());
            values.put(COLUMN_STUDENT_PHONE, student.getPhone());
            values.put(COLUMN_STUDENT_PICTURE, student.getPicture());
            values.put(COLUMN_STUDENT_TOKEN, student.getToken());
            values.put(COLUMN_STUDENT_VERIFICATION, student.isVerified());
            values.put(COLUMN_STUDENT_COURSE, student.getCourse());
            values.put(COLUMN_STUDENT_TYPE, student.getType());

            //Insert Row
            db.insert(TABLE_STUDENT, null, values);
            db.close();
            Log.d(TAG, "------------------------------------------------------Data inserted");
        }


    }

    public Student getStudent() {

        String mail,
                name,
                surename,
                phone,
                picture,
                token,
                course;
        int age;
        boolean verified;
        Student student;
        int checkVerify, type;
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor;
        String[] args = {COLUMN_STUDENT_ID};
        String queryName = "SELECT * FROM  "
                + TABLE_STUDENT
                + " LIMIT " + "1";

        cursor = myDB.rawQuery(queryName, null);
        cursor.moveToFirst();
        mail = cursor.getString(1);
        name = cursor.getString(2);
        surename = cursor.getString(3);
        age = Integer.valueOf(cursor.getString(4));
        phone = cursor.getString(5);
        picture = cursor.getString(6);
        token = cursor.getString(7);
        checkVerify = Integer.valueOf(cursor.getString(8));
        if (checkVerify == 1){
            verified = true;
        }else{
            verified = false;
        }
        course = cursor.getString(9);
        type = cursor.getInt(10);


        student = new Student(
                mail,
                name,
                surename,
                age,
                phone,
                picture,
                token,
                verified,
                course,
                type);
        Log.d(TAG, "-----------------------Get Student Handled");

        return student;

    }

    public void updateStudent(Student student) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STUDENT_EMAIL, student.getEmail());
        contentValues.put(COLUMN_STUDENT_NAME, student.getName());
        contentValues.put(COLUMN_STUDENT_SURENAME, student.getSurname());
        contentValues.put(COLUMN_STUDENT_AGE, student.getAge());
        contentValues.put(COLUMN_STUDENT_PHONE, student.getPhone());
        contentValues.put(COLUMN_STUDENT_PICTURE, student.getPicture());
        contentValues.put(COLUMN_STUDENT_TOKEN, student.getToken());
        contentValues.put(COLUMN_STUDENT_VERIFICATION, student.isVerified());
        contentValues.put(COLUMN_STUDENT_COURSE, student.getCourse());
        contentValues.put(COLUMN_STUDENT_TYPE, student.getType());


        myDB.update(TABLE_STUDENT,
                contentValues,
                COLUMN_STUDENT_ID + " = 1 ",
                null);

        Log.d(TAG, "--------------------------------------Database Updated");
    }

    public boolean checkDB(){
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_STUDENT, null);
        boolean rowExists;
        if (cursor.moveToFirst()){
            rowExists = true;
        }else {
            rowExists = false;
        }
        Log.d(TAG, "------------------DB Students checked");
        return rowExists;

    }
}
