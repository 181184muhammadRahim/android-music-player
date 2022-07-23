package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
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

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login, signup;
    ConstraintLayout layout;
    FirebaseFirestore mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        layout=(ConstraintLayout) findViewById(R.id.loginLayout);
        mDatabase=FirebaseFirestore.getInstance();
        login = (Button) findViewById(R.id.loginBtnLogin);
        signup = (Button) findViewById(R.id.sigupBtnLogin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mi = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(mi);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals("")||username.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Please Enter all the required fields", Toast.LENGTH_SHORT).show();
                }else{
                    CollectionReference cr=mDatabase.collection("Users");
                    Query query=cr.whereEqualTo("name",username.getText().toString());
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().size()==0){
                                    Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();

                                }else{
                                    User user=null;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        user=document.toObject(User.class);
                                    }
                                    if(user.getPassword().equals(password.getText().toString())){
                                        Intent intent=new Intent(LoginActivity.this,AudioList.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Username and Password is not correct", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        }
                    });

                }
            }
        });

    }
}