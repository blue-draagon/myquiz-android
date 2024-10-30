package com.kajs.android.learn.myquizz.ocntroller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kajs.android.learn.myquizz.R;
import com.kajs.android.learn.myquizz.model.User;

public class MainActivity extends AppCompatActivity {
  private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
  public static final String USER_FIRST_NAME_KEY = "first_name";
  public static final String USER_SCORE_KEY = "score";

  private static Intent gameActivity;
  private static User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final TextView mGreetingTextView = findViewById(R.id.activity_main_greeting_txt);
    final EditText mNameInput = findViewById(R.id.activity_main_name_input);
    final Button mPlayButton = findViewById(R.id.activity_main_play_btn);

    String first_name = getPreferences(MODE_PRIVATE).getString(USER_FIRST_NAME_KEY, null);
    int score = getPreferences(MODE_PRIVATE).getInt(USER_SCORE_KEY, 0);

    if (first_name != null) {
      String welcomeBack = "Welcome back "+ first_name + "!\n";
      String lastScore = "Your last score is " + score + ", You will do better this time.";
      String greetingText = welcomeBack + lastScore;
      mGreetingTextView.setText(greetingText);
      mNameInput.setText(first_name);
    }

    mPlayButton.setEnabled(false);

    mNameInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPlayButton.setEnabled(s.toString().length() != 0);
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

    mPlayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        user  = new User();
        user.setFirstName(mNameInput.getText().toString());

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        preferences.edit().putString(USER_FIRST_NAME_KEY, user.getFirstName()).apply();

        gameActivity = new Intent(MainActivity.this, GameActivity.class);
        startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode && data != null) {
      // Fetch the score from the Intent
      int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

      SharedPreferences preferences = getPreferences(MODE_PRIVATE);
      preferences.edit().putInt(USER_SCORE_KEY, score).apply();
    }
  }
}
