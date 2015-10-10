package com.idiotsapps.englishpicturedictionary4vietnam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {

    private int grade = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //
        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final ImageView imageRound = (ImageView) findViewById(R.id.imageView);
        Button ringBtn = (Button) findViewById((R.id.button));
        ringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageRound.startAnimation(rotate);
            }
        });

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startQuestAct();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private int getGrade() {
        int grade = getRandNumber();
        return grade;
    }

    private int getRandNumber() {
        return 6;
    }



    public void startQuestAct() {
        //Start a new activity
        this.grade = getGrade();
        Intent intent = new Intent(this, QuesActivity.class);
        intent.putExtra("grade", this.grade);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
