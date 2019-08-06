package ubs.v2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {

    public static final String TOPICS_DB_KEY = "topics";
    public static final String TOPIC_POSTS_DB_KEY = "topic_posts";
    public static final String ORGANIZATIONS_DB_KEY = "organizations";
    public static final String ORGANIZATION_POSTS_DB_KEY = "organization_posts";
    public static final String SHOP_POSTS_DB_KEY = "shop_posts";

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
