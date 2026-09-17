package com.example.holadamvi;

import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button btnVeure = findViewById(R.id.btnMostrar);
        btnVeure.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View w){
               TextView txtTitol = (TextView) findViewById(R.id.txtTitol);
           }
        });

        Button btnMes = findViewById(R.id.btnSumar);
        btnMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtTitol = (TextView) findViewById(R.id.txtTitol);
                txtTitol.setTextSize(txtTitol.getTextSize()+1);
            }
        });
        Button btnMenys = findViewById(R.id.btnRestar);
        btnMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtTitol = (TextView) findViewById(R.id.txtTitol);
                int sizet = Math.round(txtTitol.getTextSize());
                txtTitol.setTextSize(sizet-1);
            }
        });
        /*LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tvNiko = new TextView(getApplicationContext());
        tvNiko.setText("Hola Damvi 26");
        tvNiko.setTextColor(Color.BLUE);

        tvNiko.setVisibility(TextView.INVISIBLE);

        Button btnNiko = new Button(getApplicationContext());
        btnNiko.setText("Mostrar");
        btnNiko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNiko.setVisibility(TextView.VISIBLE);
            }
        });
        ll.addView(tvNiko);
        ll.addView(btnNiko);
        setContentView(ll);*/

    }
}