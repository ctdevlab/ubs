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

public class OrganizationActivity extends AppCompatActivity {

    private FloatingActionButton create_topic;
    private DatabaseReference mDatabase;
    private ListView list_v;
    private ArrayList<Topic> listV = new ArrayList<>();
    private ArrayAdapter<Topic> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        getSupportActionBar().setTitle("Organization");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Organization_Topic");
        create_topic = findViewById(R.id.Floating_Button);
        list_v = findViewById(R.id.List_V);
        adapter = new ArrayAdapter<Topic>(this, R.layout.custom_view, listV);
        list_v.setAdapter(adapter);
        registerForContextMenu(list_v);

        create_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizationActivity.this, TopicActivity.class);
                intent.putExtra("system", "Organization_Topic");
                startActivity(intent);
                finish();
            }
        });

        list_v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Topic value = listV.get(i);

                Intent intent = new Intent(OrganizationActivity.this, ViewActivity.class);
                intent.putExtra("name", value.getTopic());
                intent.putExtra("system", "Organization_Post");
                startActivity(intent);
                finish();
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Topic data = dataSnapshot.getValue(Topic.class);
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
                Topic value = listV.get(info.position);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if(value.getUid().equals(currentUser.getUid())){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Organization_Topic");
                    mDatabase.child(value.getKey()).removeValue();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Organization_Post");
                    mDatabase.child(value.getTopic()).removeValue();
                    adapter.remove(adapter.getItem(info.position));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(OrganizationActivity.this, "You do not have permission to delete this item", Toast.LENGTH_SHORT).show();
                }

                break;
            default: break;
        }
        return super.onContextItemSelected(item);
    }
}
