package com.example.anter.androidcontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * AndroidContentProvider
 * <p>
 *
 * @author Anter
 * @date 2018/5/23
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "[DatabaseHelper]";
    private static final String DATABASE_NAME = "Student.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "student_table";
    // Columns
    public static final String STUDENT_ID = "ID";
    public static final String STUDENT_NAME = "NAME";
    public static final String STUDENT_SURNAME = "SURNAME";
    public static final String STUDENT_MARKS = "MARKS";

    private final int COL_TYPE_ID = 1;
    private final int COL_TYPE_NAME = 2;

    // Encryption show be the data encryption but the connection access

    public DatabaseHelper(Context context) {
        // execute: create database if not exist
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreateDatabase = "create table " + TABLE_NAME
                + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STUDENT_NAME + " TEXT,"
                + STUDENT_SURNAME + " TEXT,"
                + STUDENT_MARKS + " INTEGER)";
        sqLiteDatabase.execSQL(sqlCreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertStudent(Student student) {
        if (!isStudentAvailable(student)) {
            return false;
        }
        if (student.getId() != null && student.getId().length() > 1) {
            if (queryStudentBy(COL_TYPE_ID, student.getId()).size() > 0) {
                Log.e(TAG, "Student existed!");
                return false;
            }
            Log.e(TAG, "Student's id will be ignore!");
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(STUDENT_NAME, student.getName());
        c.put(STUDENT_SURNAME, student.getSurname());
        c.put(STUDENT_MARKS, student.getMarks());
        return db.insert(TABLE_NAME, null, c) != -1;
    }

    public List<Student> getAllStudent() {
        return queryStudentBy(0, null);
    }

    public List<Student> getStudentById(String id) {
        return queryStudentBy(COL_TYPE_ID, id);
    }

    public List<Student> getStudentName(String stuName) {
        return queryStudentBy(COL_TYPE_NAME, stuName);
    }

    private List<Student> queryStudentBy(int queryTye, String param) {
        List<Student> students = new ArrayList<>();
        if (queryTye != 0 && (param == null || param.length() < 1)) {
            return students;
        }
        String sql = "select * from " + TABLE_NAME;
        if (queryTye == COL_TYPE_ID) {
            sql += " where ID = " + param;
        } else if (queryTye == COL_TYPE_NAME) {
            sql += " where NAME = " + param;
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery(sql, null);
        res.moveToFirst();
        while (res.moveToNext()) {
            Student student = new Student();
            student.setId(res.getString(0));
            student.setName(res.getString(1));
            student.setSurname(res.getString(2));
            student.setMarks(res.getString(3));
            students.add(student);
        }
        res.close();
        return students;
    }

    public boolean updateStudent(Student student) {
        if (!isStudentAvailable(student)) {
            return false;
        }
        if (student.getId() == null || student.getId().length() < 1) {
            logErrMsg("Student's id is null");
            return false;
        }
        if (queryStudentBy(COL_TYPE_ID, student.getId()).size() < 1) {
            logErrMsg("Student not existed!");
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_ID, student.getId());
        contentValues.put(STUDENT_NAME, student.getName());
        contentValues.put(STUDENT_SURNAME, student.getSurname());
        contentValues.put(STUDENT_MARKS, student.getMarks());
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{student.getId()});
        return false;
    }

    public boolean deleteStudentById(String id) {
        if (id == null || id.length() < 1) {
            logErrMsg("Student'id is null.");
            return false;
        }

        return queryStudentBy(COL_TYPE_ID, id).size() >= 1 && deleteStudent(COL_TYPE_ID, id);
    }

    public boolean deleteStudentByName(String name) {
        if (name == null || name.length() < 1) {
            logErrMsg("Student'name is null.");
            return false;
        }

        return queryStudentBy(COL_TYPE_NAME, name).size() >= 1 && deleteStudent(COL_TYPE_NAME, name);
    }

    private boolean deleteStudent(int deleteType, String param) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (deleteType == COL_TYPE_ID) {
            return db.delete(TABLE_NAME, "ID = ?", new String[]{param}) > 0;
        }
        return deleteType == COL_TYPE_NAME && db.delete(TABLE_NAME, "NAME = ?", new String[]{param}) > 0;
    }

    private void logErrMsg(String errMsg) {
        Log.e(TAG, errMsg);
    }

    /**
     * Notice: checkout without student's id
     */
    private boolean isStudentAvailable(Student student) {
        if (student == null) {
            logErrMsg("Student is null");
            return false;
        }
        if (student.getName() == null || student.getName().length() < 1) {
            logErrMsg("Student'name is null");
            return false;
        }
        if (student.getSurname() == null || student.getSurname().length() < 1) {
            logErrMsg("Student's surname is null");
            return false;
        }
        if (student.getMarks() == null || student.getMarks().length() < 1) {
            logErrMsg("Student' marks is null");
            return false;
        }
        return true;
    }

}
