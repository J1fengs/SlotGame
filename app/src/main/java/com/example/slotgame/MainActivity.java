package com.example.slotgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ImageView left, middle, right;
    Button play;
    boolean isPlayed = false;
    private static int[] slotImgPool = {R.drawable.diamond, R.drawable.cherry, R.drawable.img777};
    //Modified
    ArrayList <String> URL = new ArrayList<>();
    ExecutorService executorServiceMod;

    //Background Task
    SlotTask slotTask1, slotTask2, slotTask3;
    ExecutorService executorService1, executorService2, executorService3, executorServicePool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Modified
        executorServiceMod = Executors.newSingleThreadExecutor();

        //================================================\\
        left = findViewById(R.id.imageViewLeft);
        middle = findViewById(R.id.imageViewMiddle);
        right = findViewById(R.id.imageViewRight);
        play = findViewById(R.id.buttonPlay);

        executorService1 = Executors.newSingleThreadExecutor();
        executorService2 = Executors.newSingleThreadExecutor();
        executorService3 = Executors.newSingleThreadExecutor();

        executorServicePool = Executors.newFixedThreadPool(3);
        slotTask1 = new SlotTask(left);
        slotTask2 = new SlotTask(middle);
        slotTask3 = new SlotTask(right);

        play.setOnClickListener(this::OnClick);
    }

    //Modified OnClick
    /**public void OnClick(View view){
        Handler handler = new Handler(Looper.getMainLooper());
        executorServiceMod.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    final String text
                            = loadStringFromNetwork("https://mocki.io/v1/821f1b13-fa9a-43aa-ba9a-9e328df8270e");
                    try{
                        JSONArray jsonArray = new JSONArray(text);

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            URL.add(jsonObject.getString("url"));
                        }
                    } catch (JSONException e){ e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this).load(URL.get(0)).into(left);
                            Glide.with(MainActivity.this).load(URL.get(1)).into(middle);
                            Glide.with(MainActivity.this).load(URL.get(2)).into(right);
                        }
                    });
                } catch (IOException e ){ e.printStackTrace();
                }
            }
        });
    }**/

    public void OnClick(View view) {
        if (view.getId() == play.getId()) {
            if (!isPlayed) {
                slotTask1.played = true;
                slotTask2.played = true;
                slotTask3.played = true;

                executorServicePool.execute(slotTask1);
                executorServicePool.execute(slotTask2);
                executorServicePool.execute(slotTask3);
                play.setText("stop");

                isPlayed =! isPlayed;

            }else{
                slotTask1.played = false;
                slotTask2.played = false;
                slotTask3.played = false;
                play.setText("play");

                isPlayed =! isPlayed;
            }

        }

    }
    class SlotTask implements Runnable {
        ImageView slotImg;
        Random random = new Random();
        public boolean played = true;
        int counter;

        public SlotTask(ImageView slotImg) {
            this.slotImg = slotImg;
            counter = 0;
            played = true;
        }

        @Override
        public void run() {
            while (played) {
                counter = random.nextInt(3);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        slotImg.setImageResource(MainActivity.slotImgPool[counter]);
                    }
                });

                try {
                    Thread.sleep(random.nextInt(500));}
                catch (InterruptedException e) {
                    e.printStackTrace(); }


                }
            }

        }
    /**private String loadStringFromNetwork(String s) throws
            IOException {
        final URL myURL = new URL(s);
        final InputStream input = myURL.openStream();
        final StringBuilder out = new StringBuilder();
        final byte[] buffer = new byte[1024];
        try {
            for (int i; (i = input.read(buffer)) != -1; ) {
                out.append(new String(buffer, 0, i));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error Receiving Text",
                    e);
        }
        final String yourFileAsAString = out.toString();
        return yourFileAsAString;

    }/**/
    }
