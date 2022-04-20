package com.example.mentalhealthsupport;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Quotes extends Activity {
    TextView txtDate, txtBack;
    GridView grid;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private String userId;
    public static String[] authors = { "Alcott", "Atwood", "Bombeck", "Buddha", "Coron", "Estefan", "Grellet", "Keller", "King", "Lennon", "Luhrmann", "Noland", "Obama", "Riley", "Rowling", "Steinbeck", "Wells", "Wilcox", "Winfrey"} ;
    public static int[] imageId = { R.drawable.alcott, R.drawable.atwood, R.drawable.bombeck, R.drawable.buddha, R.drawable.coron, R.drawable.estefan, R.drawable.grellet, R.drawable.keller, R.drawable.king, R.drawable.lennon, R.drawable.luhrmann, R.drawable.noland, R.drawable.obama, R.drawable.riley, R.drawable.rowling, R.drawable.steinbeck, R.drawable.wells, R.drawable.wilcox, R.drawable.winfrey};
    public static  String[] quotes = {
            "I am not afraid of storms, for I am learning how to sail my ship.",
            "Optimism means better than reality; pessimism means worse than reality.",
            "Worry is like a rocking chair: It gives you something to do but never gets you anywhere",
            "Praise and blame, gain and loss, pleasure and sorrow come and go like the wind.",
            "Everybody goes through difficult times, but it is those who push through those difficult times who will eventually become successful in life. Don't give up, because this too shall pass.",
            "There are so many difficult things we're living through in the world today, so many horrible events, but we cannot let them stop us. No matter what happens, I feel you must move forward with optimism and not get totally sideswiped.",
            "I shall pass this way but once; any good that I can do or any kindness I can show to any human being; let me do it now.",
            "No pessimist ever discovered the secrets of the stars, or sailed to an uncharted land, or opened a new heaven to the human spirit.",
            "The ultimate measure of a man is not where he stands in moments of comfort and convenience, but where he stands at times of challenge and controversy.",
            "Everything will be okay in the end. If it's not okay, it's not the end.",
            "A life lived in fear is a life half lived,\" declares a character in the 1992 Australian film \"Strictly Ballroom.",
            "I know what I have to do now, I've got to keep breathing because tomorrow the sun will rise. Who knows what the tide could bring?",
            "You may not always have a comfortable life. And you will not always be able to solve all the world’s problems all at once. But don’t ever underestimate the impact you can have, because history has shown us that courage can be contagious, and hope can take on a life of its own.",
            "Hard times don’t create heroes. It is during the hard times when the ‘hero’ within us is revealed.",
            "We do not need magic to change the world, we carry all the power we need inside ourselves already: we have the power to imagine better.",
            "This here bearing went out. We didn't know it was goin', so we didn' worry none. Now she's out an' we'll fix her. An' by Christ that goes for the rest of it.",
            "Life is short, but it is wide. This too shall pass.",
            "And let its meaning permeate each day. Whatever comes, This too shall pass away.",
            "When I look at the future, it's so bright it burns my eyes."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        txtDate = findViewById(R.id.txtDate);
        txtBack = findViewById(R.id.txtBack);
        txtDate.setText(getMonth());

        userId = getIntent().getStringExtra(Login.ID_KEY);

        CustomGrid adapter = new CustomGrid(Quotes.this, authors, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Quotes.this, "You Clicked at " + authors[+ position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Quotes.this, PerQuoteView.class);
                intent.putExtra("author_pos", position);
                intent.putExtra(Login.ID_KEY, userId);
                startActivity(intent);
            }
        });



        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quotes.this, CalendarActivity.class);
                intent.putExtra(Login.ID_KEY, userId);
                startActivity(intent);
                finish();
            }
        });


    }

    private String getMonth(){
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
        date = dateFormat.format(calendar.getTime());
        return date;
    }

}