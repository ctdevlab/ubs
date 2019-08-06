package ubs.v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TopicActivity extends AppCompatActivity implements View.OnClickListener {

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

        if (system.equals(Constants.CREATE_TOPIC)) {
            getSupportActionBar().setTitle("Create a Topic");
        } else if (system.equals(Constants.CREATE_ORGANIZATION)) {
            getSupportActionBar().setTitle("Create a Club");
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        topic = findViewById(R.id.Topic);

        findViewById(R.id.Submit).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (system.equals(Constants.CREATE_TOPIC)) {
            goBackToInformation();
        } else if (system.equals(Constants.CREATE_ORGANIZATION)) {
            goBackToOrganization();
        }
    }

    private void goBackToInformation() {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
        finish();
    }

    private void goBackToOrganization() {
        Intent intent = new Intent(TopicActivity.this, OrganizationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Submit) {
            createTopic();
        } else if (view.getId() == R.id.Cancel) {
            onBackPressed();
        }
    }

    private void createTopic() {
        String topic_txt = topic.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (topic_txt.isEmpty()) {
            Toast.makeText(TopicActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            String table = system;
            if (system.equals(Constants.CREATE_TOPIC)) {
                table = DatabaseManager.TOPICS_DB_KEY;
            } else if (system.equals(Constants.CREATE_ORGANIZATION)) {
                table = DatabaseManager.ORGANIZATIONS_DB_KEY;
            }

            DatabaseReference newTopicRef = mDatabase.child(table).push();
            Topic topic = new Topic(topic_txt.toLowerCase(), currentUser.getUid(), newTopicRef.getKey());
            newTopicRef.setValue(topic, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        String error = databaseError.getMessage();
                        Toast.makeText(TopicActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else {
                        onBackPressed();
                    }
                }
            });
        }
    }
}
