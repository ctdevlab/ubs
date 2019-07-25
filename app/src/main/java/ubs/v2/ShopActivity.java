package ubs.v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView list_v;
    private ArrayList<Topic> listV = new ArrayList<>();
    private ArrayAdapter<Topic> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        getSupportActionBar().setTitle("Shop");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop_Topic");
        list_v = findViewById(R.id.List_V);
        adapter = new ArrayAdapter<Topic>(this, R.layout.custom_view, listV);
        list_v.setAdapter(adapter);

        list_v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Topic value = listV.get(i);

                Intent intent = new Intent(ShopActivity.this, ViewActivity.class);
                intent.putExtra("name", value.getTopic());
                intent.putExtra("system", "Shop_Post");
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
}
