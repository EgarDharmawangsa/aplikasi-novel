    package com.example.projekmobileprog;

    import androidx.appcompat.app.AppCompatActivity;

    import android.database.Cursor;
    import android.os.Bundle;
    import android.widget.ListView;
    import android.widget.SimpleCursorAdapter;

    public class UserListActivity extends AppCompatActivity {


        private DatabaseManager databaseManager;

        private ListView listView;

        private SimpleCursorAdapter adapter;

        final String[] from = new String[]{DatabaseHelper.COLUMN_ID_USER,
                DatabaseHelper.COLUMN_NAME_USER, DatabaseHelper.COLUMN_USERNAME_USER};

        final int[] to = new int[]{R.id.id_user_ui, R.id.name_user_ui, R.id.username_user_ui};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_user_list);

            databaseManager = new DatabaseManager(this);
            databaseManager.open();
            Cursor cursor = databaseManager.getAllUsers();


            listView = (ListView) findViewById(R.id.list_user_ui);
            listView.setEmptyView(findViewById(R.id.user_empty_ui));

            adapter = new SimpleCursorAdapter(this, R.layout.view_user_record, cursor, from, to, 0);
            adapter.notifyDataSetChanged();

            listView.setAdapter(adapter);
        }
    }