package fabian.de.palaver;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddContactDialog extends DialogFragment {

    AddContactInterface callingActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            callingActivity = (AddContactInterface) activity;
        }

        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement the AddContactInterface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_contact_dialog, null);

        builder.setView(view)
                .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callingActivity.addContact();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddContactDialog.this.getDialog().dismiss();
                    }
                });

        builder.setMessage("Kontakt hinzufügen");

        return builder.create();
    }
}
