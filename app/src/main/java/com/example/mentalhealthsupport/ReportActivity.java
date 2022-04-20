package com.example.mentalhealthsupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportActivity extends AppCompatActivity {

    EditText etPatientName,etPatientSymptoms;
    CheckBox cbColds,cbCough,cbFever,cbDiarrhea;
    CheckBox cbSoreThroat,cbLossOfSmell,cbLossOfTaste;
    Button btnSaveReport;
    TextView txtBack;
    private String userId;

    DatabaseReference databaseReports;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        databaseReports = FirebaseDatabase.getInstance().getReference("reports");
        initializeViewComponents();
        txtBack = findViewById(R.id.txtBack);
        userId = getIntent().getStringExtra(Login.ID_KEY);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, CalendarActivity.class);
                intent.putExtra(Login.ID_KEY, userId);
                startActivity(intent);
                finish();
            }
        });
    }

    public void saveReportToDatabase(View view) {
        String patientName = etPatientName.getText().toString();
        String patientSymptoms1 = "";
        String patientSymptoms2 = etPatientSymptoms.getText().toString();

        if(cbColds.isChecked())
            patientSymptoms1 += cbColds.getText().toString() +";";
        if(cbCough.isChecked())
            patientSymptoms1 += cbCough.getText().toString() +";";
        if(cbFever.isChecked())
            patientSymptoms1 += cbFever.getText().toString() +";";
        if(cbSoreThroat.isChecked())
            patientSymptoms1 += cbSoreThroat.getText().toString() +";";
        if(cbDiarrhea.isChecked())
            patientSymptoms1 += cbDiarrhea.getText().toString() +";";
        if(cbLossOfSmell.isChecked())
            patientSymptoms1 += cbLossOfSmell.getText().toString() +";";
        if(cbLossOfTaste.isChecked())
            patientSymptoms1 += cbLossOfTaste.getText().toString() +";";


        if (!TextUtils.isEmpty(patientName) || !TextUtils.isEmpty(patientSymptoms2) ) {
            String reportId = databaseReports.push().getKey();
            Report report = new Report(reportId, patientName, patientSymptoms1, patientSymptoms2);
            databaseReports.child(reportId).setValue(report);
            etPatientName.setText("");
            String successMessage = "Your reported symptoms have been submitted";
            Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    public void returnToHome(View view){
        //Intent intent
    }

    void initializeViewComponents(){
        etPatientName = findViewById(R.id.etPatientName);
        etPatientName.setText(getIntent().getStringExtra("name"));
        etPatientSymptoms = findViewById(R.id.etOtherSymptoms);
        cbFever = findViewById(R.id.cbFever);
        cbCough = findViewById(R.id.cbCough);
        cbColds = findViewById(R.id.cbColds);
        cbSoreThroat = findViewById(R.id.cbSoreThroat);
        cbDiarrhea = findViewById(R.id.cbDiarrhea);
        cbLossOfSmell = findViewById(R.id.cbLossOfSmell);
        cbLossOfTaste = findViewById(R.id.cbLossOfTaste);
        btnSaveReport = findViewById(R.id.btnSaveReport);
    }

}