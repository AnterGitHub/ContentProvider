package com.example.anter.androidcontentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * @author Anter
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "[MainActivity]";
    private Uri uri = Uri.parse("content://com.example.anter.student");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.insert).setOnClickListener(this);
        findViewById(R.id.query).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insert:
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.STUDENT_NAME, "Anter");
                values.put(DatabaseHelper.STUDENT_SURNAME, "Lu");
                values.put(DatabaseHelper.STUDENT_MARKS, "Hero");
                getContentResolver().insert(uri, values);
                break;
            case R.id.query:
                Cursor cursor = managedQuery(Uri.parse("content://com.example.anter.student/student"), null, null, null, null);
                while (cursor.moveToNext()) {
                    Log.i(TAG, "onClick: " + cursor.getString(0) + ", "
                            + cursor.getString(1) + ", "
                            + cursor.getString(2) + ", "
                            + cursor.getString(3));
                }
                break;
            default:
                break;
        }
    }
}
