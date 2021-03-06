package com.dal_csci3130.course_registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ClassRemaining extends AppCompatActivity {

    private User user;
    private DataBase db;
    public ArrayAdapter<Course> results_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remaining_classes);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
        if (extras != null) {
            db = (DataBase) extras.getSerializable("database");
        }
        ArrayList<Course> courseList = user.getRemaining();

        //Gets filter conditions

        displayRemaining(courseList);
    }

    /**
     * return user and db to previous activity
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putSerializable("user", user);
        extras.putSerializable("database", db);
        intent.putExtras(extras);
        setResult(0, intent);
        finish();
    }

    /**
     *  Wait for results from initiated activity and update this activities params
     * @param requestCode = request code of activity
     * @param resultCode = result code of activity (expect 0)
     * @param data = bundles
     */
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
     if (extras != null) {
         user = (User) extras.getSerializable("user");
     }
     if (extras != null) {
         db = (DataBase) extras.getSerializable("database");
     }
 }

    public void displayRemaining(ArrayList<Course> courseList)  {
        ListView results_List = this.findViewById(R.id.resultsList);
        results_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseList);
        results_List.setAdapter(results_Adapter);
    }
}