package com.dypiet.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dypiet.app.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Quiz extends AppCompatActivity{
    Button ans1,ans2,ans3,ans4;
    TextView question_1,tv;
    DatabaseReference reference;
    int total=0;
    int correct=0;
    int que=0;
    int wrong=0;
    ArrayList<Question> quizList;
    ProgressDialog progressDialog;
    CountDownTimer countDownTimer;
    TextView textViewQNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_quiz);

        ans1=(Button)findViewById (R.id.ans1);
        ans2=(Button)findViewById (R.id.ans2);
        ans3=(Button)findViewById (R.id.ans3);
        ans4=(Button)findViewById (R.id.ans4);
        tv=(TextView)findViewById (R.id.tv) ;
        textViewQNo = (TextView) findViewById (R.id.tv_qno);
        question_1 =(TextView)findViewById (R.id.question);

        progressDialog = new ProgressDialog (this);
        progressDialog.setMessage ("Loading Questions...");
        progressDialog.setCancelable (false);
        loadQuestion();
    }

    private void loadQuestion() {
        findViewById (R.id.quiz_layout).setVisibility (View.GONE);
        progressDialog.show ();
        quizList = new ArrayList<> ();

        String branch = getIntent ().getStringExtra ("Branch");
        String subject = getIntent ().getStringExtra ("Subject");

        reference = FirebaseDatabase.getInstance ( ).getReference ( ).child ("Branch").child (branch).child (subject);

        reference.addListenerForSingleValueEvent (new ValueEventListener ( ) {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren ()) {
                    Question question = child.getValue(Question.class);
                    quizList.add(question);
                }
                init();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void init() {
        if(!quizList.isEmpty ()) {
            correct = 0;
            total = 0;
            progressDialog.hide ();
            findViewById (R.id.quiz_layout).setVisibility (View.VISIBLE);
            reverseTimer (120);
            update ();
        }
    }

    private void update() {
        if (total == 25) {
            countDownTimer.cancel ();
            Intent i = new Intent (Quiz.this, ResultActivity.class);
            i.putExtra ("total", String.valueOf (total));
            i.putExtra ("correct", String.valueOf (correct));
            i.putExtra ("wrong", String.valueOf (wrong));
            startActivity (i);
            finish ();
            return;
        }
        textViewQNo.setText (""+(total+1));
        updateViews ();
        Random random = new Random ( );
        final Question question = quizList.get (random.nextInt (quizList.size ( )));
        question_1.setText (question.getQuestion ( ));
        ans1.setText (question.getAns1 ( ));
        ans2.setText (question.getAns2 ( ));
        ans3.setText (question.getAns3 ( ));
        ans4.setText (question.getAns4 ( ));

        if (question.getAns3 ( ).isEmpty ( ))
            ans3.setVisibility (View.GONE);
        else
            ans3.setVisibility (View.VISIBLE);
        if (question.getAns4 ( ).isEmpty ( ))
            ans4.setVisibility (View.GONE);
        else
            ans4.setVisibility (View.VISIBLE);

        ans1.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                total++;
                updateViews();

                if (ans1.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                    ans1.setBackgroundResource (R.drawable.button_success);
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            correct++;
                            ans1.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);
                } else {
                    wrong++;
                    ans1.setBackgroundResource (R.drawable.button_failed);

                    if (ans2.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans2.setBackgroundResource (R.drawable.button_success);
                    } else if (ans3.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans3.setBackgroundResource (R.drawable.button_success);
                    } else if (ans4.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans4.setBackgroundResource (R.drawable.button_success);
                    }
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            ans1.setBackgroundResource (R.drawable.button_options);
                            ans2.setBackgroundResource (R.drawable.button_options);
                            ans3.setBackgroundResource (R.drawable.button_options);
                            ans4.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);

                }
            }
        });
        ans2.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                updateViews();
                total++;
                if (ans2.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                    ans2.setBackgroundResource (R.drawable.button_success);
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            correct++;
                            ans2.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);
                } else {
                    wrong++;
                    ans2.setBackgroundResource (R.drawable.button_failed);

                    if (ans1.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans1.setBackgroundResource (R.drawable.button_success);
                    } else if (ans3.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans3.setBackgroundResource (R.drawable.button_success);
                    } else if (ans4.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans4.setBackgroundResource (R.drawable.button_success);
                    }
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            ans1.setBackgroundResource (R.drawable.button_options);
                            ans2.setBackgroundResource (R.drawable.button_options);
                            ans3.setBackgroundResource (R.drawable.button_options);
                            ans4.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);

                }
            }
        });
        ans3.setOnClickListener (new View.OnClickListener ( ) {

            @Override
            public void onClick(View view) {
                total++;
                updateViews();
                if (ans3.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                    ans3.setBackgroundResource (R.drawable.button_success);
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            correct++;
                            ans3.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);
                } else {
                    wrong++;
                    ans3.setBackgroundResource (R.drawable.button_failed);

                    if (ans1.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans1.setBackgroundResource (R.drawable.button_success);
                    } else if (ans2.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans2.setBackgroundResource (R.drawable.button_success);
                    } else if (ans4.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans4.setBackgroundResource (R.drawable.button_success);
                    }
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            ans1.setBackgroundResource (R.drawable.button_options);
                            ans2.setBackgroundResource (R.drawable.button_options);
                            ans3.setBackgroundResource (R.drawable.button_options);
                            ans4.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);

                }
            }
        });
        ans4.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                total++;
                updateViews();
                if (ans4.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                    ans4.setBackgroundResource (R.drawable.button_success);
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            correct++;
                            ans4.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);
                } else {
                    wrong++;
                    ans4.setBackgroundResource (R.drawable.button_failed);

                    if (ans1.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans1.setBackgroundResource (R.drawable.button_success);
                    } else if (ans2.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans2.setBackgroundResource (R.drawable.button_success);
                    } else if (ans3.getText ( ).toString ( ).equals (question.getAnswer ( ))) {
                        ans3.setBackgroundResource (R.drawable.button_success);
                    }
                    Handler handler = new Handler ( );
                    handler.postDelayed (new Runnable ( ) {
                        @Override
                        public void run() {
                            ans1.setBackgroundResource (R.drawable.button_options);
                            ans2.setBackgroundResource (R.drawable.button_options);
                            ans3.setBackgroundResource (R.drawable.button_options);
                            ans4.setBackgroundResource (R.drawable.button_options);
                            update ( );
                        }
                    }, 1000);

                }

            }
        });


    }

    private void updateViews(){
        if ((ans1.isClickable ( ))) {
            ans1.setClickable (false);
        } else {
            ans1.setClickable (true);
        }
        if ((ans2.isClickable ( ))) {
            ans2.setClickable (false);
        } else {
            ans2.setClickable (true);
        }
        if ((ans3.isClickable ( ))) {
            ans3.setClickable (false);
        } else {
            ans3.setClickable (true);
        }
        if ((ans4.isClickable ( ))) {
            ans4.setClickable (false);
        } else {
            ans4.setClickable (true);
        }
    }

    public  void  reverseTimer(int seconds)
    {
        countDownTimer = new CountDownTimer (seconds *1000+1000,1000){
            public void onTick(long millisUntilFinished){

                int seconds=(int)(millisUntilFinished/1000);
                int minutes=seconds/60;

                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                tv.setText (text);
            }
            public void onFinish(){
                Intent i= new Intent (Quiz.this,ResultActivity.class);
                i.putExtra ("total",String.valueOf (total));
                i.putExtra ("correct",String.valueOf (correct));
                i.putExtra ("wrong",String.valueOf (wrong));
                startActivity (i);
                finish ();
            }
        }.start();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder (this).setIcon (R.drawable.ic_warning).setTitle ("Exit?").setMessage ("Are you sure you want to close exam ?").setPositiveButton ("Yes", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                countDownTimer.cancel ();
                finish ();
            }

        }).setNegativeButton ("No", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss ();
            }
        }).show ( );
    }

}
