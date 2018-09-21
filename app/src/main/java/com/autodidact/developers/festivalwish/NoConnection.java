package com.autodidact.developers.festivalwish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NoConnection extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate( savedInstanceState );
        setContentView( R.layout.no_connection );

        button = (Button) findViewById(R.id.button2);

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh;
                refresh = new Intent(NoConnection.this, MainActivity.class);
                startActivity(refresh);
                finish();
            }
        } );
    }
}