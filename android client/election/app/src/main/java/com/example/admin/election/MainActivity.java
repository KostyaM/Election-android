package com.example.admin.election;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    LinkedList<Candidate>output = new LinkedList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    String subject,question;
    static ConnectionSettings connectionSettings;
    Handler  animator = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==0){
                LinearLayout progressBarContainer=findViewById(R.id.progressbar_layout);
                progressBarContainer.removeAllViews();

                Log.e("Remove","DOne"+progressBarContainer.getChildCount());
            }
            Log.e("Remove", String.valueOf(msg.what));
            fillIndexData("not worjing", output.get(msg.what));
            return true;
        }
    });
    Handler  infoSetter = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            TextView subjecTextViewt=findViewById(R.id.subject);
            subjecTextViewt.setText(subject);
            TextView questionTextView=findViewById(R.id.question);
            questionTextView.setText(question);
            return true;
        }
    });

    Handler notFound = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mSwipeRefreshLayout.setRefreshing(false);
            LinearLayout progressBarContainer=findViewById(R.id.progressbar_layout);
            ImageView image;
            image = new ImageView(getApplicationContext());
            image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.not_found));
            progressBarContainer.removeAllViews();
            progressBarContainer.addView(image);
            Toast toast=Toast.makeText(getApplicationContext(), R.string.fail,Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionSettings=new ConnectionSettings("192.168.0.102");
        LinearLayout mainLayout= findViewById(R.id.main_layout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;


        FrameLayout.LayoutParams  mainParams = new FrameLayout.LayoutParams(
                width,
               FrameLayout.LayoutParams.WRAP_CONTENT
        );


        mainLayout.setLayoutParams(mainParams);
        // code request code here

        new NetworkGetJSON(connectionSettings.getIp()+"/android").start();
        mSwipeRefreshLayout =  findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
             NetworkGetJSON njc=  new NetworkGetJSON(connectionSettings.getIp()+"/android");
             njc.start();

            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.blue);

        ImageButton settings=findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                final DialogFragment dialogFragment = new SettingsDialog();
                dialogFragment.show(ft, "dialog");


            }
        });

    }
    public void saveData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = pref.edit();
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        EditText fioEditTexdt=toolbar.findViewById(R.id.fio);
        editor.putString("fio", fioEditTexdt.getText().toString());
        editor.apply();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }
    public void retrieveData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Settings", 0);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        EditText fioEditTexdt=toolbar.findViewById(R.id.fio);
       fioEditTexdt.setText(pref.getString("fio", null));
    }
    @Override
    protected void onStart() {
        super.onStart();
        retrieveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    public void fillIndexData(String subject, final Candidate candidate){
        TextView subjectTextView= findViewById(R.id.subject);
        subjectTextView.setText(subject);
        LinearLayout progressBarContainer=findViewById(R.id.progressbar_layout);

        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        LinearLayout.LayoutParams progressBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 135);

        RelativeLayout relativelayout = new RelativeLayout(this);
        relativelayout.setLayoutParams(layoutparams);

        ProgressBar progressBar=new ProgressBar(getApplicationContext(),null,android.R.attr.progressBarStyleHorizontal);
        progressBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(20,VibrationEffect.DEFAULT_AMPLITUDE));
                }else{
                    //deprecated in API 26
                    vibrator.vibrate(20);
                }
                android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
                EditText fioEditTexdt=toolbar.findViewById(R.id.fio);
                try {
                    System.out.println(fioEditTexdt);
                    String fio =fioEditTexdt.getText().toString();
                    new SendVote(fio, candidate.getName(), connectionSettings.getIp()+"/android").start();
                }catch (NullPointerException ne){
                    Log.e("SEND","FAIL");
                    ne.printStackTrace();
                }

                return false;
            }
        });
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_drawable));
        progressBar.setLayoutParams(progressBarLayoutParams);
        progressBar.setMax(100);


        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, candidate.getVoteCount());
        progressAnimator.setDuration(500);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();


        relativelayout.addView(progressBar);



        TextView candidateData=new TextView(this);
        candidateData.setText(String.format(getResources().getString(R.string.candidate_item),  candidate.getName(), candidate.getVoteCount()));
        candidateData.setTextColor(Color.WHITE);
        candidateData.setTextSize(20);
        candidateData.setPadding(90,20,50,20);
        relativelayout.addView(candidateData);

        progressBarContainer.addView(relativelayout);

    }
    class ParseJson extends Thread{

        String JSON;
        ParseJson(String JSON){
            this.JSON=JSON;
        }

        @Override
        public void run() {
            super.run();
            JSONParser parser = new JSONParser();
            output.clear();
            try {
                JSONArray candidates = (JSONArray) parser.parse(JSON);
                for (Object option : candidates) {
                    JSONObject optionJSON=(JSONObject) option;
                    output.add(new Candidate(String.valueOf(optionJSON.get("name")),(int)Float.parseFloat(String.valueOf(optionJSON.get("persent")))));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    class GetInfo extends Thread {
        @Override
        public void run() {
            super.run();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(connectionSettings.getIp() + "/android/info")
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONParser parser = new JSONParser();
            try {
                JSONObject info = (JSONObject) parser.parse(response.body().string());
                subject= (String) info.get("subject");
                question= (String) info.get("question");
            } catch (ParseException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            infoSetter.sendEmptyMessage(0);

        }
    }
    class beautifullAnimation extends Thread{
        @Override
        public void run() {
            super.run();
            for (int i=0; i<output.size(); i++){
                animator.sendEmptyMessage(i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            new GetInfo().start();
        }
    }
    class SendVote extends Thread{
        String fio;
        String candidate;
        String url;
        String route;
        SendVote(String fio,String candidate,String url, String route){
            this.url=url;
            this.route=route;
            this.fio=fio;
            this.candidate=candidate;
        }
        SendVote(String fio,String candidate,String url){
            this.url=url;
            this.fio=fio;
            this.candidate=candidate;
        }
        @Override
        public void run() {
            super.run();
            OkHttpClient httpClient = new OkHttpClient();
            final MediaType mediaType
                    = MediaType.parse("application/json");
            String responceData="{ \n" +
                    "\t \"fio\":\"" +fio+"\", \n "+
                    "\t \"candidate\":\"" +candidate+"\" \n "+
                    "}";

            System.out.println(responceData);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, responceData))
                    .build();
            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {

                    response.body().string();
                }

            } catch (IOException e) {

            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new NetworkGetJSON(connectionSettings.getIp()+"/android").start();

        }
    }
    class NetworkGetJSON extends Thread{
        String url;
        String route;
        OkHttpClient client;
        NetworkGetJSON(String url){
            this.url=url;
            client = new OkHttpClient();
        }
        NetworkGetJSON(String url, String route){
            this.url=url;
            this.route=route;
            client = new OkHttpClient();
        }

        @Override
        public void run() {
            super.run();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
               ParseJson pj= new ParseJson(response.body().string());
                pj.start();
                try {
                    pj.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new beautifullAnimation().start();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException ne){

                notFound.sendEmptyMessage(0);
            }
        }
    }
}
