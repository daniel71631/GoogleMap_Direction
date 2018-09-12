package com.example.mana1.sampletest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class location extends AppCompatActivity {

    public static String newresult01;
    public static String newresult02;
    private Button mbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        final TextView text_1=(TextView)findViewById(R.id.text1);
        final TextView text_2=(TextView)findViewById(R.id.text2);

        mbutton=(Button)findViewById(R.id.button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newresult01=text_1.getText().toString();
                newresult02=text_2.getText().toString();
                startActivity(new Intent(location.this, MapsActivity.class));
            }
        });
    }
}
