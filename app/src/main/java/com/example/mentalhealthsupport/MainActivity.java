package com.example.mentalhealthsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView show_date_text;
    private Button show_calendar;
    Spinner spinnerStatus;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    long isolationStart, isolationEnd;

    EditText et_fullname, et_email, et_password, et_Cnumber;
    TextView mlogin, txt_isolationDate;
    CheckBox cb_termsandcondition;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    Button signupbtn;

    DatabaseReference databaseUsers;


    public void goback(View view){
        Intent n = new Intent(MainActivity.this, Login.class);
        startActivity(n);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        show_date_text = findViewById(R.id.show_calendar);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerStatus.setOnItemSelectedListener(OnCatSpinnerCL);
        show_calendar = findViewById(R.id.show_calendar);

        et_fullname = findViewById(R.id.et_fullname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_Cnumber = findViewById(R.id.et_Cnumber);
        mlogin = findViewById(R.id.mlogin);
        cb_termsandcondition = findViewById(R.id.cb_termsandcondition);
        progressBar = findViewById(R.id.progressBar);
        signupbtn = findViewById(R.id.signupbtn);
        txt_isolationDate = findViewById(R.id.txt_isolationDate);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
        date = dateFormat.format(calendar.getTime());

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            fAuth.signOut();
        }

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String fullname = et_fullname.getText().toString().trim();
                String isolationDate = txt_isolationDate.getText().toString().trim();
                String status = spinnerStatus.getSelectedItem().toString().trim();
                String contact = et_Cnumber.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    et_email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    et_password.setError("Password is Required");
                    return;
                }
                if(TextUtils.isEmpty(fullname)){
                    et_fullname.setError("Fullname is Required");
                    return;
                }
                if(TextUtils.isEmpty(isolationDate)){
                    show_calendar.setError("Isolation date is Required");
                    return;
                }
                if(TextUtils.isEmpty(status)){
                    Toast.makeText(MainActivity.this, "Status is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(contact)){
                    et_Cnumber.setError("Contact number is Required");
                    return;
                }
                if (password.length() < 8){
                    et_password.setError("Password must be 8 characters long");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //add user to users db
                            String id = fAuth.getCurrentUser().getUid();
                            User newUser = new User(id, email, fullname, status, isolationDate, isolationStart, isolationEnd, contact);
                            databaseUsers.child(id).setValue(newUser);
                            registerCometChatAccount(id, fullname);

                            Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                            intent.putExtra(Login.ID_KEY, id);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(MainActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        String status = spinnerStatus.getSelectedItem().toString();
        findViewById(R.id.show_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        show_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String pickerDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView tvDatePicker = findViewById(R.id.textViewContent);
//        CountdownView myCountdownView = findViewById(R.id.mycountdown);

        try {
            show_calendar.setText(pickerDateString);
            txt_isolationDate.setText(pickerDateString);
//            Date now = new Date();

//            long currentDate = now.getTime();
//            long pickerDate =
            isolationStart = calendar.getTimeInMillis();
            calendar.add(Calendar.DATE, 14);
            isolationEnd = calendar.getTimeInMillis();
          //  countDownToPickerDate = pickerDate - currentDate;
          //  myCountdownView.start(countDownToPickerDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemSelectedListener OnCatSpinnerCL = new AdapterView.OnItemSelectedListener() {
        @SuppressLint("ResourceAsColor")
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(R.color.blue);
            ((TextView) parent.getChildAt(0)).setTextSize(16);
        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void registerCometChatAccount(String ID, String username) {
        com.cometchat.pro.models.User user = new com.cometchat.pro.models.User();
        user.setUid(ID);
        user.setName(username);
        user.setAvatar("https://www.seekpng.com/png/detail/115-1150053_avatar-png-transparent-png-royalty-free-default-user.png");

        CometChat.createUser(user, Constants.COMETCHAT_AUTH_KEY, new CometChat.CallbackListener<com.cometchat.pro.models.User>() {
            @Override
            public void onSuccess(com.cometchat.pro.models.User user) {
                Log.d("CometChat", "onSuccess: User created");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("CometChat", "onError: "+e.getMessage());
            }
        });
    }

}