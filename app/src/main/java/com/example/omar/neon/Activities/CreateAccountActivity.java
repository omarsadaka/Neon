package com.example.omar.neon.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omar.neon.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText email , password , firstName , lastName;
    private Button createAccount;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance ( );
        mFirebaseDatabase = FirebaseDatabase.getInstance ( );
        mDatabaseReference = mFirebaseDatabase.getReference ( ).child ( "MUsers" );
        createView();
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }
    public void createView(){
        email = findViewById(R.id.create_acc_email);
        password = findViewById(R.id.create_acc_pwd);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        createAccount = findViewById(R.id.create_account);
        progressDialog = new ProgressDialog(CreateAccountActivity.this);
    }

    private void createNewAccount() {
        final String name = firstName.getText ( ).toString ( ).trim ( );
        final String Lname = lastName.getText ( ).toString ( ).trim ( );
        String Email = email.getText ( ).toString ( ).trim ( );
        String pwd = password.getText ( ).toString ( ).trim ( );
        if (!TextUtils.isEmpty ( Email ) && !TextUtils.isEmpty ( pwd ) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(Lname)) {
            progressDialog.setMessage ( "Creating Account..." );
            progressDialog.show ( );
            mAuth.createUserWithEmailAndPassword ( Email, pwd ).addOnSuccessListener ( new OnSuccessListener<AuthResult>( ) {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult != null) {
                        Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_LONG).show();

                                String userId = mAuth.getCurrentUser ( ).getUid ( );
                                DatabaseReference currentUserDB = mDatabaseReference.child ( userId );
                                currentUserDB.child ( "first Name" ).setValue ( name );
                                currentUserDB.child ( "Last Name" ).setValue ( Lname );
                                progressDialog.dismiss ( );
                                Intent intent = new Intent ( CreateAccountActivity.this, HomeActivity.class );
                                startActivity ( intent );
                                CreateAccountActivity.this.finish();
                            }

                }
            } );
        }

    }
}
