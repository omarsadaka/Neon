package com.example.omar.neon.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.neon.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView loginAsGuest;
    private EditText email , password;
    private LoginButton loginButton ;
    private Button fb , createAcc , signIn;
    private CallbackManager callbackManager;
    private AccessToken mAccessToken;
    private static final String EMAIL = "email";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        keyHash();
        createView();

       loginAsGuest.setOnClickListener(this);
       createAcc.setOnClickListener(this);
       signIn.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        LoginWithFAcebook();
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        mAuth = FirebaseAuth.getInstance ( );
        mAuthListner = new FirebaseAuth.AuthStateListener ( ) {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser ();
                if (mUser != null){
                    Toast.makeText ( MainActivity.this, "Signed In", Toast.LENGTH_SHORT ).show ( );
                }else {
                    Toast.makeText ( MainActivity.this, "Not Signed In", Toast.LENGTH_SHORT ).show ( );

                }

            }
        };


    }

    protected void keyHash() {
        PackageInfo info;
        try {
            info = this.getPackageManager().getPackageInfo("com.example.omar.neon", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("KeyHash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("KeyHash name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHashnosuchalgorithm", e.toString());
        } catch (Exception e) {
            Log.e("KeyHash exception", e.toString());
        }
    }
    public void createView(){
        loginAsGuest = findViewById(R.id.loginAsGuest);
        loginButton = findViewById(R.id.login_button);
        fb =  findViewById(R.id.fb);
        createAcc = findViewById(R.id.create_acc);
        email  =findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void LoginWithFAcebook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d ( "Sucses!" , loginResult.toString () );
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Login onCancel Failed!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "Login Failed!\n"+error.toString (), Toast.LENGTH_LONG).show();
                Log.e("failed" , error.toString());
            }
        });
    }

    public void onClick(View view) {
        if (view == fb) {
            loginButton.performClick();
        }else if (view == loginAsGuest){
            startActivity(new Intent(MainActivity.this , HomeActivity.class));
            MainActivity.this.finish();
        }else if (view == createAcc){
            startActivity(new Intent(MainActivity.this , CreateAccountActivity.class));
            MainActivity.this.finish();
        }else if (view == signIn){
            if (!TextUtils.isEmpty ( email.getText ().toString () )&& !TextUtils.isEmpty ( password.getText ().toString () )){
                String Email = email.getText ().toString ();
                String Password = password.getText ().toString ();
                login(Email , Password);
            }
        }
    }
    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword ( email , password ).addOnCompleteListener ( MainActivity.this,
                new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ()){
                            Toast.makeText ( MainActivity.this, "Signed In", Toast.LENGTH_SHORT ).show ( );
                            startActivity ( new Intent ( MainActivity.this , HomeActivity.class ) );
                            MainActivity.this.finish();
                        }else {
                            Toast.makeText ( MainActivity.this, "Not Signed In", Toast.LENGTH_LONG ).show ( );

                        }

                    }
                } );

    }
}
