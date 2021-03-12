package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText txtStudentid;
    TextView txtAnswer;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtStudentid = findViewById(R.id.txtStudentid);
        txtAnswer = findViewById(R.id.txtAnswer);
        btnSend = findViewById(R.id.btnSend);
    }

}