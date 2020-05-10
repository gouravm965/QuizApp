package com.demo.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.demo.quizapp.R;

public class UserInfo extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "myuserdatabase";
    EditText fullName, userAge;
    private RadioGroup genderRadioGroup;
    Button register;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        fullName = (EditText) findViewById(R.id.username);
        userAge = (EditText) findViewById(R.id.age);
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radiogroup);
        findViewById(R.id.btnRegister).setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        createUserTable();
    }

    //this method will create the table
    //as we are going to call this method everytime we will launch the application
    //I have added IF NOT EXISTS to the SQL
    //so it will only create the table when the table is not already created
    private void createUserTable() {
        mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS user (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT user_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    age varchar(200) NOT NULL,\n" +
                        "    gender varchar(200) NOT NULL\n"+");"
        );
    }

    private boolean inputsAreCorrect(String name, String age) {
        if (name.isEmpty()) {
            fullName.setError("Please enter a name");
            fullName.requestFocus();
            return false;
        }

        if (age.isEmpty() || Integer.parseInt(age) <= 0) {
            userAge.setError("Please enter age");
            userAge.requestFocus();
            return false;
        }
        return true;
    }

    //In this method we will do the create operation
    private void addUser() {

            String name = fullName.getText().toString().trim();
            String age = userAge.getText().toString().trim();
            RadioButton selectedRadioButton = (RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId());
            String gender = selectedRadioButton == null ? "" : selectedRadioButton.getText().toString();


            //validating the inptus
            if (inputsAreCorrect(name, age)) {

                String insertSQL = "INSERT INTO user \n" +
                        "(name, age, gender)\n" +
                        "VALUES \n" +
                        "(?, ?, ?);";

                //using the same method execsql for inserting values
                //this time it has two parameters
                //first is the sql string and second is the parameters that is to be binded with the query
                mDatabase.execSQL(insertSQL, new String[]{name, age, gender});

                Toast.makeText(this, "User Added Successfully", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(UserInfo.this, StartingQuiz.class);
                startActivity(i);
            }
        }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                addUser();
                break;
        }
    }
}
