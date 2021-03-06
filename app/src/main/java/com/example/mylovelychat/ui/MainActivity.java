package com.example.mylovelychat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovelychat.FireChatHelper.ChatHelper;
import com.example.mylovelychat.FireChatHelper.ReferenceUrl;
import com.example.mylovelychat.ProfileActivity;
import com.example.mylovelychat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Ref;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        /**возможно этот кусок кода нужно будет изменить на слушателя*/
        if (firebaseAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser()  {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        //if validation are ok
        //we will first show a progressbar

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //User is successfully registered and logged in
                            //we will start the profile activity here

                            // Store user data necessary for chat app
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put(ReferenceUrl.KEY_PROVIDER, firebaseAuth.getCurrentUser().getProviderId());
                            map.put(ReferenceUrl.KEY_FIRST_NAME, name);
                            map.put(ReferenceUrl.KEY_USER_EMAIL, email);
                            map.put(ReferenceUrl.CHILD_CONNECTION, ReferenceUrl.KEY_ONLINE);
                            map.put(ReferenceUrl.KEY_AVATAR_ID, ChatHelper.generateRandomAvatarForUser());

                            // Time user date is stored database
                            long createTime = new Date().getTime();
                            map.put(ReferenceUrl.KEY_TIMESTAMP, String.valueOf(createTime));


                            String user_id = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = databaseReference.child(user_id);
                            current_user_db.setValue(map);


                            finish();
                            /*Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else{
                            Exception s = task.getException();
                            Toast.makeText(MainActivity.this, "Could not register.. please try again + " + s,  Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
            registerUser();
        }

        if (v == textViewSignin){
            //will open login activity here
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
