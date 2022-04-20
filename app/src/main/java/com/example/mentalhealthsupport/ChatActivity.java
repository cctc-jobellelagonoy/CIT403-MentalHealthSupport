package com.example.mentalhealthsupport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    ListView roomList;
    ArrayList<RoomModel> roomModelArrayList = new ArrayList<>();
    RoomAdapter adapter;
    TextView prompt;
    User currentUser;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = getIntent().getStringExtra(Login.ID_KEY);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        prompt = findViewById(R.id.empty_prompt);
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("rooms");



        roomList = findViewById(R.id.listview);
        adapter = new RoomAdapter(getBaseContext(),roomModelArrayList);
        roomList.setAdapter(adapter);
        roomList.requestLayout();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRoom();
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomModelArrayList.clear();
                if(dataSnapshot.getChildrenCount() == 0)
                {
                    prompt.setVisibility(View.VISIBLE);
                    roomList.setVisibility(View.GONE);
                }
                else
                {
                    prompt.setVisibility(View.GONE);
                    roomList.setVisibility(View.VISIBLE);
                }
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext())
                {
                    RoomModel model = new RoomModel();
                    DataSnapshot snap = ((DataSnapshot)iterator.next());
                    model.setName(snap.getKey());

                    model.setCreateDate((String)snap.child("date").getValue());

                    Log.e("akx",snap.toString()+ " ");

                    roomModelArrayList.add(model);
                    adapter.notifyDataSetChanged();
                    roomList.invalidate();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                joinChatRoom(position);

            }
        });
    }

    private void joinChatRoom(final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChatActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.join_room, null);
        CheckBox cbAnonymous = dialogView.findViewById(R.id.cbAnonymous);
        TextView txtUsername = dialogView.findViewById(R.id.txtUsername);

        txtUsername.setText(currentUser.getFullName());

        cbAnonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbAnonymous.isChecked()){
                    txtUsername.setText("Anonymous");
                }
                else{
                    txtUsername.setText(currentUser.getFullName());
                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Join Room");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(getBaseContext(),ChatRoom.class)
                        .putExtra("room",roomModelArrayList.get(position).getName())
                        .putExtra("date",roomModelArrayList.get(position).getCreateDate())
                        .putExtra("user",txtUsername.getText().toString())
                );

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    private void createNewRoom() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChatActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_new_room, null);

        EditText room = dialogView.findViewById(R.id.room_name);
        TextView user = dialogView.findViewById(R.id.user_name);
        CheckBox cbAnonymous =  dialogView.findViewById(R.id.cb_anonymous);

        user.setText(currentUser.getFullName());
        cbAnonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbAnonymous.isChecked()){
                    user.setText("Anonymous");
                }
                else{
                    user.setText(currentUser.getFullName());
                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Create Room");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Map<String,Object> map = new HashMap<>();
                map.put(room.getText().toString(),"");

                dbRef.updateChildren(map);

                DatabaseReference roomRef = dbRef.child(room.getText().toString());
                map = new HashMap<>();
                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                map.put("date",format.format(new Date()));
                map.put("chats","");
                roomRef.updateChildren(map);

                startActivity(new Intent(getBaseContext(),ChatRoom.class)
                        .putExtra("room",room.getText().toString())
                        .putExtra("date",format.format(new Date()))
                        .putExtra("user",user.getText().toString())
                );
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Toast.makeText(getBaseContext(),"Developed by: CIT306 students", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(ChatActivity.this, CalendarActivity.class);
        setIntent.putExtra(Login.ID_KEY, userId);
        startActivity(setIntent);
        finish();
    }
}