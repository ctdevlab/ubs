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

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText name;
    private EditText memberid;
    private EditText password;
    private Button cancelbutton;
    private Button signupbutton;
    private String member;
    private String pass;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setTitle("Registration");

        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.Name);
        memberid = findViewById(R.id.Member_ID);
        password = findViewById(R.id.Password);
        cancelbutton = findViewById(R.id.Cancel_Button);
        signupbutton = findViewById(R.id.Signup_Button);

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, SigninSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });


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

                if(!member.isEmpty() && !pass.isEmpty() && check && lowercase && uppercase && specialchac && length){
                    mAuth.createUserWithEmailAndPassword(member, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Member ID is already registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if(!check){
                    Toast.makeText(RegistrationActivity.this, "Member ID must be the University ID", Toast.LENGTH_SHORT).show();
                }
                else if(member.isEmpty() || pass.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Member ID or Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Password must have at least 8 characters, 1 uppercase, 1 lowercase, and 1 special characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity.this, SigninSignupActivity.class);
        startActivity(intent);
        finish();
    }
}
