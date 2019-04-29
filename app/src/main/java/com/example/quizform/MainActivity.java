package com.example.quizform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  ArrayList<RadioQuestion> radioQuestions;

  int questionsCount = 6;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.showQuestions(null);
  }

  @Override
  public void onBackPressed() {
    System.out.println("asd");
    this.showQuestions(null);
  }

  protected int calculateRightAnswers() {
    int rightAnswersCount = 0;
    for (int i = 0; i < this.radioQuestions.size(); i++) {
      RadioQuestion rq = this.radioQuestions.get(i);
      Question question = rq.question;
      RadioGroup radioGroup = rq.radioGroup;

      int radioButtonID = radioGroup.getCheckedRadioButtonId();
      if (radioButtonID == -1) continue;

      View radioButton = radioGroup.findViewById(radioButtonID);
      String answer = (String) ((RadioButton) radioButton).getText();
      if (question.checkAnswer(answer)) rightAnswersCount++;
    }
    return rightAnswersCount;
  }

  protected void updateButtonState() {
    Button btn = this.findViewById(R.id.resultsButton);
    for (int i = 0; i < this.radioQuestions.size(); i++) {
      RadioQuestion rq = this.radioQuestions.get(i);
      RadioGroup radioGroup = rq.radioGroup;

      int radioButtonID = radioGroup.getCheckedRadioButtonId();
      if (radioButtonID == -1) {
        btn.setEnabled(false);
        return;
      }
    }
    btn.setEnabled(true);
    return;
  }

  private RadioGroup addQuestion(Question question) {
    RadioGroup rg = new RadioGroup(this); //create the RadioGroup

    TextView text = new TextView(this);
    text.setText(question.getTitle());
    rg.addView(text);

    String[] answers = question.getAnswers();
    for (int i = 0; i < answers.length; i++) {
      RadioButton rb = new RadioButton(this);
      rb.setText(answers[i]);
      rg.addView(rb);
      rb.setId(i);
    }

    LinearLayout item = this.findViewById(R.id.mainLayout);
    item.addView(rg, 0);

    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        updateButtonState(); // обновляем состояние кнопки (не даём посмотреть результаты)
      }
    });

    rg.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    rg.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;

    return rg;
  }

  protected void showResults(View view) {
    setContentView(R.layout.activity_result);
    TextView text = this.findViewById(R.id.resultText);
    int rightAnswersCount = this.calculateRightAnswers();
    text.setText("Ваш результат: " + rightAnswersCount + "/" + this.questionsCount);
  }

  protected void showQuestions(View view) {
    setContentView(R.layout.activity_main);

    this.radioQuestions = new ArrayList<RadioQuestion>();

    Random rgen = new Random();
    for (int i = 0; i < this.questionsCount; i++) {
      int a = rgen.nextInt(100);
      int b = rgen.nextInt(100);
      int sum = a + b;

      Question question = new Question(a + " + " + b + " = ?", "" + sum, new String[] {"" + sum, "" + rgen.nextInt(200), "" + rgen.nextInt(200), "" + rgen.nextInt(200)});
      RadioGroup rg = this.addQuestion(question);

      RadioQuestion rq = new RadioQuestion();
      rq.radioGroup = rg;
      rq.question = question;

      this.radioQuestions.add(rq);
    }
  }
}

class RadioQuestion {
  public Question question;
  public RadioGroup radioGroup;
}

class Question {
  private String title;
  private String rightAnswer;
  private String answers[];

  public Question(String title, String rightAnswer, String[] answers) {
    this.title = title;
    this.rightAnswer = rightAnswer;
    this.answers = Utils.RandomizeArray(answers);
  }

  public String getTitle() {
    return this.title;
  }

  public String[] getAnswers() {
    return this.answers;
  }

  public Boolean checkAnswer(String answer) {
    return this.rightAnswer.equals(answer);
  }
}

class Utils {
  public static String[] RandomizeArray(String[] array) {
    Random rgen = new Random();  // Random number generator
    for (int i = 0; i < array.length; i++) {
      int randomPosition = rgen.nextInt(array.length);
      String temp = array[i];
      array[i] = array[randomPosition];
      array[randomPosition] = temp;
    }
    return array;
  }
}
