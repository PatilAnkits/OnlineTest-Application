package com.dypiet.app;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {
    TextView total,correct_ans,wrong_ans, result,percentageTv;
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_result);
        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
        total=(TextView)findViewById (R.id.textView_total);
        correct_ans=(TextView)findViewById (R.id.textView_correct);
        wrong_ans=(TextView)findViewById (R.id.textView_wrong);
        result = findViewById (R.id.textView_result);
        percentageTv = findViewById (R.id.textView_percentage);
        animationView = findViewById (R.id.animation_view);
        Intent i = getIntent ();
        String que =i.getStringExtra ("total");
        String correct =i.getStringExtra ("correct");
        String wrong = i.getStringExtra ("wrong");
        total.setText (que);
        correct_ans.setText (correct);
        wrong_ans.setText (wrong);
        int marks = Integer.parseInt (correct);

        if(marks>=10){
            result.setText ("Pass");
            result.setBackgroundResource (R.drawable.button_success);
            animationView.setAnimation (R.raw.thumb_up);
            animationView.playAnimation ();
        }
        else{
            result.setText ("Failed");
            result.setBackgroundResource (R.drawable.button_failed);
            animationView.setAnimation (R.raw.sad);
            animationView.playAnimation ();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double percentage = (double)((double)marks/25.00)*100.00;
        String formatted = df.format(percentage);
        percentageTv.setText (formatted+"%");


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("Exit ?")
                .setMessage("Are you sure you want to close ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish ();
                    }

                })
                .setNegativeButton ("No", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss ();
                    }
                })
                .show();

    }
}
