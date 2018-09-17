package com.example.mana1.sampletest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class location extends AppCompatActivity {

    public static String newresult01;
    public static String newresult02;
    private Button mbutton;
    private EditText metxtLocation, metxtDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        metxtLocation=(EditText)findViewById(R.id.etxtLocation);
        metxtDestination=(EditText)findViewById(R.id.etxtDestination);

        mbutton=(Button)findViewById(R.id.button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newresult01=metxtLocation.getText().toString();
                newresult02=metxtDestination.getText().toString();
                startActivity(new Intent(location.this, MapsActivity.class));
            }
        });
    }
}
