package com.example.gebruiker.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Game game;
    int mode;

    /** Create method with own toolbar and setting the mode to either AI or 2-player. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mode = getIntent().getIntExtra("mode", 1);
        game = new Game(mode);
    }

    /** Inflate the menu options. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /** Bind actions to home and reset buttons from the menu. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                game = new Game(mode);
                updateUI();
                return true;
            case R.id.home:
                finish();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Put game object in outState. */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
    }

    /** Set game object to game object from inState and update the UI. */
    @Override
    public void onRestoreInstanceState( Bundle inState) {
        super.onRestoreInstanceState(inState);
        game = (Game) inState.getSerializable("game");
        updateUI();
    }

    /** Updates the UI by:
     *  1) Updating the text in the buttons by looping through the game object.
     *  2) Updating the winner textView. */
    public void updateUI() {
        GridLayout grid = findViewById(R.id.board);
        for (int i = 0; i <9; i++) {
            Tile tile = game.getTile(i);
            Button button = (Button) grid.getChildAt(i);
            switch(tile) {
                case CROSS:
                    button.setText("X");
                    break;
                case CIRCLE:
                    button.setText("O");
                    break;
                case BLANK:
                    button.setText("");
                    break;
                case INVALID:
                    break;
            }
        }
        getWinner();
    }

    /** Handles a click on a tile by:
     *  1) Check which tile is clicked by parsing the id name.
     *  2) Send to game object that button is clicked.
     *  3) Update UI. */
    public void tileClicked(View view) {
        int id = view.getId();
        Button button = findViewById(id);
        String idname = getResources().getResourceEntryName(id);
        idname = idname.replaceAll("\\D+","");
        int idnumber = Integer.parseInt(idname);
        int row=(idnumber-1)/3;
        int column=(idnumber-1)%3;
        Tile tile = game.draw(row, column);
        if (mode == 1) {
            updateUI();
        }
        switch(tile) {
            case CROSS:
                button.setText("X");
                break;
            case CIRCLE:
                button.setText("O");
                break;
            case INVALID:
                break;
        }
        getWinner();
    }

    /** Check the game object if and who has won and update the winner textView. */
    public void getWinner() {
        TextView winner = findViewById(R.id.winner);
        if (game.getGameStatus()){
            if(game.getFirstPlayerStatus()) {
                if (mode == 1) {
                    winner.setText("You win!");
                }
                else {
                    winner.setText("Player 1 Wins!");
                }
            }
            else if (game.getSecondPlayerStatus()) {
                if (mode == 1) {
                    winner.setText("You lose :(");
                }
                else {
                    winner.setText("Player 2 Wins!");
                }
            }
            else {
                winner.setText("Draw!");
            }
        }
        else {
            winner.setText("");
        }
    }

}
