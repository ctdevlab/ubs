package ubs.v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {

    private Button submit;
    private Button cancel;
    private EditText title;
    private EditText description;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String name;
    private String system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().setTitle("Create a Post");

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        system = intent.getStringExtra("system");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        title = findViewById(R.id.Topic);
        description = findViewById(R.id.Description);
        submit = findViewById(R.id.Submit);
        cancel = findViewById(R.id.Cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_post = title.getText().toString().trim();
                String des_post = description.getText().toString().trim();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if(title_post.isEmpty() || des_post.isEmpty()){
                    Toast.makeText(PostActivity.this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    String key = mDatabase.child(system).child(name).push().getKey();
                    mDatabase.child(system).child(name).child(key).child("key").setValue(key);
                    mDatabase.child(system).child(name).child(key).child("title").setValue(title_post);
                    mDatabase.child(system).child(name).child(key).child("description").setValue(des_post);
                    mDatabase.child(system).child(name).child(key).child("uid").setValue(currentUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(PostActivity.this, ViewActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("system", system);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(PostActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, ViewActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("system", system);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PostActivity.this, ViewActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("system", system);
        startActivity(intent);
        finish();
    }
}

