package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignupActivity extends AppCompatActivity {

    EditText username, email, password;
    Button login, signup;
    FirebaseFirestore mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.emailField);
        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);

        login = (Button) findViewById(R.id.loginBtnSignup);
        signup = (Button) findViewById(R.id.signupBtnSignup);
        mDatabase=FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mi = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(mi);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("")||password.getText().toString().equals("")||username.getText().toString().equals(""))
                {
                    Toast.makeText(SignupActivity.this, "Please Enter all the required fields", Toast.LENGTH_SHORT).show();

                }else{
                    User user=new User(username.getText().toString(),password.getText().toString(),email.getText().toString());
                    CollectionReference cr=mDatabase.collection("Users");
                    Query query=cr.whereEqualTo("name",user.getName());
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().size()>0){
                                    Toast.makeText(SignupActivity.this, "Username already exist", Toast.LENGTH_SHORT).show();

                                }else{
                                    Query query=cr.whereEqualTo("email",user.getEmail());
                                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                if(task.getResult().size()>0){
                                                    Toast.makeText(SignupActivity.this, "Email already exist", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Task<DocumentReference> task2 = cr.add(user);
                                                }
                                            }
                                        }
                                    });

                                }
                            }
                        }
                    });

                    }
            }

        });



    }

}