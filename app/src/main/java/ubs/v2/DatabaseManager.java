package ubs.v2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {

    public static final String TOPICS_DB_KEY = "topics";
    public static final String POSTS_DB_KEY = "posts";

    private static DatabaseManager instance;
    private FirebaseDatabase database;

    private DatabaseManager() {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return database.getReference();
    }
}
