package com.example.akash.androidonlinequizapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.akash.androidonlinequizapp.BroadcastReceiver.AlarmReceiver;
import com.example.akash.androidonlinequizapp.Common.Common;
import com.example.akash.androidonlinequizapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNewUser,edtNewPassword,edtNewEmail,edtUser,edtPassword;
    Button btnSignUp,btnSignIn;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // registerAlarm();

        //Firebase
        database = FirebaseDatabase.getInstance();
        users=database.getReference("Users");

        edtUser=(MaterialEditText) findViewById(R.id.editUserName);
        edtPassword=(MaterialEditText) findViewById(R.id.editPassword);

        btnSignIn=(Button) findViewById(R.id.btn_sign_in);
        btnSignUp=(Button) findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,14);
        calendar.set(Calendar.MINUTE,46);
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am =(AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void signIn(final String user, final String pwd){
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists()){
                    if(!user.isEmpty()){
                        User login=dataSnapshot.child(user).getValue(User.class);
                        if(login.getPassword().equals(pwd))
                        {
                            Intent homeActivity=new Intent(MainActivity.this,Home.class);
                            Common.currentUser=login;
                            startActivity(homeActivity);
                            finish();
                        }
                        else
                            Toast.makeText(MainActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this,"Enter Your Username",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this,"User does not exist",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog(){
        final AlertDialog.Builder alertDialog= new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill full information");


        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout=inflater.inflate(R.layout.signup_layout,null);


        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);
        edtNewEmail=(MaterialEditText) sign_up_layout.findViewById(R.id.editNewEmail);
        edtNewUser=(MaterialEditText) sign_up_layout.findViewById(R.id.editNewUserName);
        edtNewPassword=(MaterialEditText) sign_up_layout.findViewById(R.id.editNewPassword);

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("hello","+ve");
               final User user = new User(edtNewUser.getText().toString(),
                       edtNewPassword.getText().toString(),
                                            edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUserName()).exists()){
                            Toast.makeText(MainActivity.this,"User Already Exists!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this,"User Registration Success!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
