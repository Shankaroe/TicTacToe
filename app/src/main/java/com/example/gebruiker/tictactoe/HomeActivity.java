package com.example.gebruiker.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /** Starts AI mode. */
    public void playAI(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 1);
        finish();
        startActivity(intent);
    }

    /** Starts two player mode. */
    public void play2(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 2);
        finish();
        startActivity(intent);
    }
}
