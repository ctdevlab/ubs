package ubs.v2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class SigninSignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText memberid;
    private EditText password;
    private Button signinbutton;
    private Button signupbutton;
    private String member;
    private String pass;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup);

        getSupportActionBar().setTitle("Welcome");

        mAuth = FirebaseAuth.getInstance();
        memberid = findViewById(R.id.Member_ID);
        password = findViewById(R.id.Password);
        signinbutton = findViewById(R.id.Signup_Button);
        signupbutton = findViewById(R.id.Cancel_Button);

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member = memberid.getText().toString() + "@gmail.com";
                pass = password.getText().toString();

                if(!member.isEmpty() && !pass.isEmpty()){
                    mAuth.signInWithEmailAndPassword(member, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SigninSignupActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SigninSignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SigninSignupActivity.this, "Incorrect Member ID or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SigninSignupActivity.this, "Member ID or Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninSignupActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member = memberid.getText().toString();
                pass = password.getText().toString();

                if(member.length() == 10){
                    try {
                        Integer.parseInt(member);
                        member = member + "@gmail.com";
                        check = true;
                    } catch (Exception e) {
                        check = false;
                    }
                }
                else{
                    check = false;
                }

                boolean uppercase = !pass.equals(pass.toLowerCase());
                boolean lowercase = !pass.equals(pass.toUpperCase());
                boolean length = pass.length() >= 8;
                boolean specialchac = !pass.matches("[A-Za-z0-9]*");

                if(!member.isEmpty() && !pass.isEmpty() && check && lowercase && uppercase && specialchac){
                    mAuth.createUserWithEmailAndPassword(member, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SigninSignupActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SigninSignupActivity.this, "Member ID is already registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if(!check){
                    Toast.makeText(SigninSignupActivity.this, "Member ID must be the University ID", Toast.LENGTH_SHORT).show();
                }
                else if(member.isEmpty() || pass.isEmpty()){
                    Toast.makeText(SigninSignupActivity.this, "Member ID or Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SigninSignupActivity.this, "Password must have at least 8 characters, 1 uppercase, 1 lowercase, and 1 special characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SigninSignupActivity.this, EntryActivity.class);
        startActivity(intent);
        finish();
    }
}
