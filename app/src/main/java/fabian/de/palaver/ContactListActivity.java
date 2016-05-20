package fabian.de.palaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_bar_logout){
            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addContact(View view) {
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Kontakt hinzufügen");

        builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getFragmentManager().popBackStack();
            }
        });

        builder.setView(getLayoutInflater().inflate(R.layout.add_contact_dialog, null, false));

        builder.show();*/

        AddContactDialog dialog = new AddContactDialog();

        dialog.show(getFragmentManager(), "Add Contact");
    }
}
