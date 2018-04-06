package com.dal_csci3130.course_registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by AJM-D on 2018-02-22.
 * This class is used to search for and show class information using filters.
 */

public class Main2Activity extends AppCompatActivity {

    //myApplication myapp = (myApplication)getApplication();
    //User user = myapp.getUser();

    public String Filter1, Filter2;
    public User user;
    public int year, seats;
    public ArrayAdapter results_Adapter;
    public ArrayList<Course> results_courses;

    ArrayList<Course> courseList = new ArrayList<Course>();
    ArrayList<Course> database_results = new ArrayList<Course>();

    TextView text2, text3, text4, text5;
    ListView results_List;
    Button Apply_Button;
    DataBase db;
    Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view_results);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        assert extras != null;
        user = (User) extras.getSerializable("user");
        db = (DataBase) extras.getSerializable("database");


        //Creation of the spinners used to display the string array filters.
        Spinner spinner = findViewById(R.id.spinner2);
        Spinner spinner2 = findViewById(R.id.spinner3);
        //Calls the array to be displayed
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.faculty_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.year_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.seat_array, android.R.layout.simple_spinner_item);
        //Sets the adapters view from resource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Sets the spinner
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        //Creation of button
        Apply_Button = this.findViewById(R.id.applyButton);
        //Gets filter conditions
        Filter1 = spinner.getSelectedItem().toString();
        Filter2 = spinner2.getSelectedItem().toString();
        //Creates text

        text3 = this.findViewById(R.id.textView3);
        text5 = this.findViewById(R.id.textView5);


        results_List = this.findViewById(R.id.resultsList);
        results_Adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, database_results);
        results_List.setAdapter(results_Adapter);

        results_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                course = results_courses.get(position);
                Intent intent = new Intent(Main2Activity.this, CourseDetails.class);
                Bundle extras = new Bundle();
                extras.putSerializable("user", user);
                extras.putSerializable("course", course);
                extras.putSerializable("database", db);
                intent.putExtras(extras);
                startActivityForResult(intent, 0);
            }
        });


        //Adapter of faculty
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Filter1 = "ANY";
                        break;
                    case 1:
                        Filter1 = "CSCI";
                        break;
                    case 2:
                        Filter1 = "MATH";
                        break;
                    case 3:
                        Filter1 = "MGMT";
                        break;
                    case 4:
                        Filter1 = "POLI";
                        break;
                    case 5:
                        Filter1 = "BIOL";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Adapter of year
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Filter2 = "ANY";
                        break;
                    case 1:
                        Filter2 = "1";
                        break;
                    case 2:
                        Filter2 = "2";
                        break;
                    case 3:
                        Filter2 = "3";
                        break;
                    case 4:
                        Filter2 = "4";
                        break;
                    case 5:
                        Filter2 = "6";
                        break;
                    case 6:
                        Filter2 = "8";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void filterApply(View v) {
        //results of query
        String tmp_string = "";

        //parses string input to int
        String year = (Filter2);
        String faculty = (Filter1);
        results_Adapter.clear();
        filtered_search search_instance = new filtered_search(db);


        results_courses = search_instance.QUERY_COURSES_DB(faculty, year);

        results_Adapter.addAll(results_courses);

    }
    //This method determines if the user's database profile already has the selected course.

    /**
     * Method is used to compare completed list with inputted parameter.
     *
     * @param filterSelection
     * @return
     */
    public boolean notCompleted(String filterSelection) {

        boolean notCompleted = true;
        //loops through to compare if there's a match.
        for (int i = 0; i < user.getCompleted().size(); i++) {
            //If there's a match with the registered course and selected filter
            if (filterSelection.equalsIgnoreCase(user.getCompleted().get(i).getTitle()))
                notCompleted = false;

        }
        return notCompleted;
    }

    /**
     * Method tests to see the type of access.
     *
     * @param filterSelection
     * @return
     */
    //This method looks at if the student has completed a class already and makes the appropriate action.
    public boolean denyAccess(String filterSelection) {
        boolean noAccess = true;
        noAccess = this.notCompleted(filterSelection);
        return noAccess;
    }

    /**
     * Method tests if error is correctly given.
     *
     * @param filterSelection
     * @return
     */
    //This method determines if the user was given access or not and gives the appropriate error.
    public boolean throwError(String filterSelection) {
        boolean noError = true;
        noError = this.denyAccess(filterSelection);
        return noError;

    }

    /**
     * This method is used to compare all times of the courses with the selected registered time.
     *
     * @param time
     * @return
     */
    public boolean timeError(String time) {

        boolean Error = false;
        //TODO:Iterate through current class time for a match
        for (int i = 0; i < user.getCompleted().size(); i++) {
            if (time.equalsIgnoreCase(user.getCompleted().get(i).getTime()))
                Error = true;
        }
        return Error;
    }

    /**
     * Checks time error.
     *
     * @param time
     * @return
     */
    public boolean deniedTime(String time) {
        boolean noAccess = true;
        noAccess = this.timeError(time);
        return noAccess;
    }

    /**
     * This method checks if there is an error and gives proper error.
     *
     * @param time
     * @return
     */

    public boolean timeErrorThrown(String time) {
        boolean noError = false;
        noError = this.deniedTime(time);
        return noError;
    }

    /**
     * return user and db to previous activity
     */

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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
        user = (User) extras.getSerializable("user");
        db = (DataBase) extras.getSerializable("database");
        results_Adapter.clear();
    }
}
