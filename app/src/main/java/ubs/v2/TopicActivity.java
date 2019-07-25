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

public class TopicActivity extends AppCompatActivity {

    private Button submit;
    private Button cancel;
    private EditText topic;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent intent = getIntent();
        system = intent.getStringExtra("system");

        if(system.equals("Information_Topic")){
            getSupportActionBar().setTitle("Create a Topic");
        }
        else if(system.equals("Organization_Topic")){
            getSupportActionBar().setTitle("Create a Club");
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        topic = findViewById(R.id.Topic);
        submit = findViewById(R.id.Submit);
        cancel = findViewById(R.id.Cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic_txt = topic.getText().toString().trim();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if(topic_txt.isEmpty()){
                    Toast.makeText(TopicActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    String key = mDatabase.child(system).push().getKey();
                    mDatabase.child(system).child(key).child("key").setValue(key);
                    mDatabase.child(system).child(key).child("topic").setValue(topic_txt.toLowerCase());
                    mDatabase.child(system).child(key).child("uid").setValue(currentUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            if(system.equals("Information_Topic")){
                                Intent intent = new Intent(TopicActivity.this, InformationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if(system.equals("Organization_Topic")){
                                Intent intent = new Intent(TopicActivity.this, OrganizationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(TopicActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(system.equals("Information_Topic")){
                    Intent intent = new Intent(TopicActivity.this, InformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(system.equals("Organization_Topic")){
                    Intent intent = new Intent(TopicActivity.this, OrganizationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(system.equals("Information_Topic")){
            Intent intent = new Intent(TopicActivity.this, InformationActivity.class);
            startActivity(intent);
            finish();
        }
        else if(system.equals("Organization_Topic")){
            Intent intent = new Intent(TopicActivity.this, OrganizationActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
