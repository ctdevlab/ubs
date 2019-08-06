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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

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
        mDatabase = DatabaseManager.getInstance().getDatabaseReference();
        title = findViewById(R.id.Topic);
        description = findViewById(R.id.Description);

        findViewById(R.id.Submit).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Submit) {
            createPost();
        } else if (view.getId() == R.id.Cancel) {
            onBackPressed();
        }
    }

    private void createPost() {
        String postTitle = title.getText().toString().trim();
        String postDescription = description.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (postTitle.isEmpty() || postDescription.isEmpty()) {
            Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            String table = system;
            if (system.equals(Constants.CREATE_TOPIC_POST)) {
                table = DatabaseManager.TOPIC_POSTS_DB_KEY;
            } else if (system.equals(Constants.CREATE_ORGANIZATION_POST)) {
                table = DatabaseManager.ORGANIZATION_POSTS_DB_KEY;
            } else if (system.equals(Constants.CREATE_SHOP_POST)) {
                table = DatabaseManager.SHOP_POSTS_DB_KEY;
            }
            DatabaseReference newPostRef = mDatabase.child(table).child(name).push();
            Post post = new Post(postTitle, postDescription, currentUser.getUid(), newPostRef.getKey());
            newPostRef.setValue(post, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference databaseReference) {
                    if (error != null) {
                        String message = error.getMessage();
                        Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        onBackPressed();
                    }
                }
            });
        }
    }
}

