package fabian.de.palaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ContactListActivity extends AppCompatActivity {

    private String[] contacts = {"Herbert", "Günther", "Gwendolyn", "Helmut", "Frank", "Annette", "Susanne"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);

        ListView contactList = (ListView) findViewById(R.id.contact_list_view);

        contactList.setAdapter(adapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contactSelected = String.valueOf(parent.getItemAtPosition(position));
                Intent startChatIntent = new Intent(ContactListActivity.this, ChatActivity.class);
                startChatIntent.putExtra("Chat Partner", contactSelected);
                startActivity(startChatIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_bar_logout){
            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
        }

        else if(id == R.id.action_bar_add_contact){
            AddContactDialog dialog = new AddContactDialog();

            dialog.show(getFragmentManager(), "Add Contact");
        }

        return super.onOptionsItemSelected(item);
    }

}
