package com.example.mentalhealthsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    public static String ID_KEY = "userID";
    EditText et_email, et_password;
    Button btnlogin;
    TextView createtv;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        progressBar = findViewById(R.id.progressBar);
        btnlogin = findViewById(R.id.btnlogin);
        createtv = findViewById(R.id.createtv);

        fAuth = FirebaseAuth.getInstance();
        initCometChat();
        if (fAuth.getCurrentUser() != null){
            loginCometChat();
            Intent intent = new Intent(Login.this, CalendarActivity.class);
            intent.putExtra(Login.ID_KEY, fAuth.getCurrentUser().getUid());
            startActivity(intent);
            finish();
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    et_email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    et_password.setError("Password is Required");
                    return;
                }
                if (password.length() < 8){
                    et_password.setError("Password must be 8 characters long");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userID = fAuth.getCurrentUser().getUid();
                            Intent intent = new Intent(Login.this, CalendarActivity.class);
                            intent.putExtra(ID_KEY, userID);
                            loginCometChat();
                            Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        createtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    private void initCometChat() {
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.COMETCHAT_REGION).build();

        UIKitSettings.userAudioCall(false);
        UIKitSettings.userVideoCall(false);
        CometChat.init(this, Constants.COMETCHAT_APP_ID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                UIKitSettings.setAuthKey(Constants.COMETCHAT_AUTH_KEY);
                CometChat.setSource("uikit","android","java");
                Log.d("CometChat", "Initialized CometChat");
            }
            @Override
            public void onError(CometChatException e) {
                Log.d("CometChat", "Failure to initialize CometChat: "+e.getMessage());
            }
        });
    }

    private void loginCometChat() {
       // if (this.loggedInUser != null && this.loggedInUser.getUid() != null) {
            CometChat.login(fAuth.getCurrentUser().getUid(), Constants.COMETCHAT_AUTH_KEY, new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    Log.d("CometChat", "onSuccess: CometChat Login Successful");
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("CometChat", "onError: " + e.getMessage());
                }
            });
      //  }
    }
}