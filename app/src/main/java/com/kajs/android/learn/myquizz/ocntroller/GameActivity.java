package com.kajs.android.learn.myquizz.ocntroller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kajs.android.learn.myquizz.R;
import com.kajs.android.learn.myquizz.model.Question;
import com.kajs.android.learn.myquizz.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionText;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;

    public static final String BUNDLE_EXTRA_SCORE = "extra_score";
    public static final String BUNDLE_STATE_SCORE = "current_score";
    public static final String BUNDLE_STATE_QUESTION = "current_question";
    public static final String BUNDLE_STATE_QUESTION_INDEX = "current_question_index";

    private static final Integer NUMBER_OF_QUESTIONS = 4;

    private static Boolean enableTouchEvent;

    private static QuestionBank questionBank;
    private static Question currentQuestion;
    Integer currentQuestionIndex;
    private static Integer score;
    private static Integer numberOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("GameActivity::onCreate()");

        setContentView(R.layout.activity_game);

        questionText = findViewById(R.id.activity_game_question_text);
        answer1Button = findViewById(R.id.activity_game_answer1_btn);
        answer2Button = findViewById(R.id.activity_game_answer2_btn);
        answer3Button = findViewById(R.id.activity_game_answer3_btn);
        answer4Button = findViewById(R.id.activity_game_answer4_btn);

        answer1Button.setTag(0);
        answer2Button.setTag(1);
        answer3Button.setTag(2);
        answer4Button.setTag(3);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);

        questionBank = this.generateQuestionBank();

        enableTouchEvent = true;

        if (savedInstanceState != null) {
            numberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
            score = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            currentQuestionIndex = savedInstanceState.getInt(BUNDLE_STATE_QUESTION_INDEX);
        } else {
            currentQuestionIndex = questionBank.getCurrentQuestionIndex();
            numberOfQuestions = NUMBER_OF_QUESTIONS;
            score = 0;
            currentQuestion = questionBank.getQuestion(currentQuestionIndex);
        }

        displayQuestion(currentQuestion);
    }

    private void displayQuestion(Question question) {
        questionText.setText(question.getQuestion());
        answer1Button.setText(question.getChoiceList().get(0));
        answer2Button.setText(question.getChoiceList().get(1));
        answer3Button.setText(question.getChoiceList().get(2));
        answer4Button.setText(question.getChoiceList().get(3));
    }

    @Override
    public void onClick(View v) {

        Integer responseIndex = (int) v.getTag();
        if (responseIndex.equals(currentQuestion.getAnswerIndex())) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
        }

        enableTouchEvent = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enableTouchEvent = true;
                // If this is the last question, ends the game.
                // Else, display the next question.
                if (--numberOfQuestions <= 0) {
                    endGame();// No questions remaining, endf the game
                } else {
                    currentQuestion = questionBank.getQuestion(null);
                    displayQuestion(currentQuestion);
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return enableTouchEvent && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, score);
        outState.putInt(BUNDLE_STATE_QUESTION, numberOfQuestions);
        outState.putInt(BUNDLE_STATE_QUESTION_INDEX, currentQuestionIndex);

        super.onSaveInstanceState(outState);
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!").setMessage("Your score is " + score + "/" + NUMBER_OF_QUESTIONS)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra(BUNDLE_EXTRA_SCORE, score);
                setResult(RESULT_OK, intent);
                finish();
            }
        }).create().show();
    }

    private QuestionBank generateQuestionBank() {
        Question q1 = new Question("In which year did Maradona score a goal with his hand?",
                Arrays.asList("1984", "1985", "1986", "1987"),
                2
        );
        Question q2 = new Question("What is the national sport in Japan?",
                Arrays.asList("Judo", "Sumo Wrestling", "Foot-ball", "Karate"),
                1
        );
        Question q3 = new Question("How many minutes is a rugby match?",
                Arrays.asList("40 Minutes", "60 Minutes", "80 Minutes", "90 Minutes"),
                2
        );
        Question q4 = new Question("In which country were the first Olympic Games held?",
                Arrays.asList("England", "Germany", "United States", "Greece"),
                3
        );
        Question q5 = new Question("How many players are on each side of the net in beach volleyball?",
                Arrays.asList("Two Players", "Four Players", "One Player", "Three Players"),
                0
        );
        Question q6 = new Question("What should you do in swordplay when you break your saber?",
                Arrays.asList("Replace it", "Continue without", "Get out", "Stop"),
                0
        );
        Question q7 = new Question("How matches did Mohammed Ali lose in his career?",
                Arrays.asList("Three", "Two", "One", "Zero"),
                2
        );
        Question q8 = new Question("In which sport can you win the Davis Cup?",
                Arrays.asList("Boxes", "Foot-ball", "Formula One", "Tennis"),
                3
        );
        Question q9 = new Question(
                "What is the nickname of the Belgian national soccer team?",
                Arrays.asList("The red", "Red devils", "Red Black", "The Devils"),
                1
        );
        Question q10 = new Question("What is the name of the Barcelona football stadium ?",
                Arrays.asList("Catalan", "Camp Nou", "Barca Stadium", "Bluegrana"),
                1
        );

        return new QuestionBank(Arrays.asList(q1, q2, q3, q4, q5, q6, q7, q8, q9, q10));
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("GameActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("GameActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("GameActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("GameActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("GameActivity::onDestroy()");
    }
}
