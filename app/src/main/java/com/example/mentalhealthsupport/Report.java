package com.example.mentalhealthsupport;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Report {
   private String reportId;
   private String patientName;
   private String patientSymptoms1;
   private String patientSymptoms2;

   public Report(){
      //this constructor is required
   }

   public Report(String reportId, String patientName, String patientSymptoms1, String patientSymptoms2) {
      this.reportId = reportId;
      this.patientName = patientName;
      this.patientSymptoms1 = patientSymptoms1;
      this.patientSymptoms2 = patientSymptoms2;
   }

   public String getPatientName() {
      return patientName;
   }

   public String getPatientSymptoms1() {
      return patientSymptoms1;
   }

   public String getPatientSymptoms2() {
      return patientSymptoms2;
   }
}