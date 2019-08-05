package ubs.v2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText memberId;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        setUpBackground();
        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        memberId = findViewById(R.id.memberId);
        password = findViewById(R.id.password);
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.signUpButton).setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                Context context = EntryActivity.this.getApplicationContext();
                if (user != null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    EntryActivity.this.startActivity(intent);
                    EntryActivity.this.finish();
                }
            }
        };
    }

    private void setUpBackground() {
        ConstraintLayout constraintLayout = findViewById(R.id.animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signInButton) {
            signIn();
        } else if (view.getId() == R.id.signUpButton) {
            signUp();
        }
    }

    private void signIn() {
        String member = memberId.getText().toString();
        String pass = password.getText().toString();
        if (member.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Member ID or Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        member += "@gmail.com";
        mAuth.signInWithEmailAndPassword(member, pass)
                .addOnCompleteListener(EntryActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Context context = getApplicationContext();
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Incorrect ID or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        String member = memberId.getText().toString();
        String pass = password.getText().toString();
        final Context context = getApplicationContext();
        if (member.isEmpty() || pass.isEmpty()) {
            Toast.makeText(context, "Id and password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (member.length() < 10) {
            Toast.makeText(context, "Id must be at least 10 digits", Toast.LENGTH_LONG).show();
            return;
        }

        member += "@gmail.com";

        boolean uppercase = !pass.equals(pass.toLowerCase());
        boolean lowercase = !pass.equals(pass.toUpperCase());
        boolean length = pass.length() >= 8;
        boolean specialChar = !pass.matches("[A-Za-z0-9]*");

        if (!uppercase || !lowercase || !length || !specialChar) {
            Toast.makeText(context, "Password must have at least 8 characters, 1 uppercase, 1 lowercase, and 1 special characters", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(member, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Member ID is already registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

