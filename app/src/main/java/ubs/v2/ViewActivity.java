package ubs.v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<Post> listV = new ArrayList<>();
    private ArrayAdapter<Post> adapter;
    private String name;
    private String system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        system = intent.getStringExtra("system");

        getSupportActionBar().setTitle(name);

        String table = system;
        if (system.equals(Constants.CREATE_TOPIC_POST)) {
            table = DatabaseManager.TOPIC_POSTS_DB_KEY;
        } else if (system.equals(Constants.CREATE_ORGANIZATION_POST)) {
            table = DatabaseManager.ORGANIZATION_POSTS_DB_KEY;
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child(table).child(name);
        FloatingActionButton createPostButton = findViewById(R.id.Floating_Button);
        ListView listView = findViewById(R.id.List_V);
        adapter = new ArrayAdapter<>(this, R.layout.custom_view_post, listV);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivity.this, PostActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("system", system);
                startActivity(intent);
                finish();
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //String titleV = dataSnapshot.child("title").getValue(String.class);
                //String descV = dataSnapshot.child("description").getValue(String.class);
                Post data = dataSnapshot.getValue(Post.class);
                listV.add(0, data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(system.equals(Constants.CREATE_TOPIC_POST)){
            Intent intent = new Intent(ViewActivity.this, InformationActivity.class);
            startActivity(intent);
            finish();
        }
        else if(system.equals(Constants.CREATE_ORGANIZATION_POST)){
            Intent intent = new Intent(ViewActivity.this, OrganizationActivity.class);
            startActivity(intent);
            finish();
        }
        else if(system.equals("Shop_Post")){
            Intent intent = new Intent(ViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.List_V){
            AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
            MenuItem del=menu.add(0,0,0,"Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case 0:
                Post value = listV.get(info.position);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if(value.getUid().equals(currentUser.getUid())){
                    mDatabase.child(value.getKey()).removeValue();
                    adapter.remove(adapter.getItem(info.position));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ViewActivity.this, "You do not have permission to delete this item", Toast.LENGTH_SHORT).show();
                }

                break;
            default: break;
        }
        return super.onContextItemSelected(item);
    }
}
