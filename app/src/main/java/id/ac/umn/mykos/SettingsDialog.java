package id.ac.umn.mykos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SettingsDialog extends DialogFragment {

    /* START CREATE INTERFACE FOR POSITIVE BUTTON FUNCTION */
    public interface OnClickPositiveButton{
        void sendNumberOfRoom(int input);
        void sendRoomIDValue(String input);
        void sendMaximalDueDate(int input);
    }
    private OnClickPositiveButton onClickPositiveButton;
    /* END CREATE INTERFACE FOR POSITIVE BUTTON FUNCTION */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            onClickPositiveButton = (OnClickPositiveButton) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("SETTING DIALOG", "onAttach: " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* START SETUP DIALOG VIEW */
        int id_layout = getArguments().getInt("layoutID");
        final View view = inflater.inflate(id_layout, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        /* END SETUP DIALOG VIEW */

        /* START SETUP FUNCTION FOR NEGATIVE BUTTON */
        TextView btnNegative = view.findViewById(R.id.btn_negative);
        btnNegative.setOnClickListener(v -> getDialog().dismiss());
        /* END SETUP FUNCTION FOR NEGATIVE BUTTON */

        /* START SETUP FUNCTION FOR POSITIVE BUTTON EACH TARGET */
        String target = getArguments().getString("target");
        TextView btnPositive = view.findViewById(R.id.btn_positive);
        TextView title = view.findViewById(R.id.dialog_title);
        if(target.equalsIgnoreCase("NumberOfRoom")){ // if click Number of Room option
            title.setText(R.string.numberofrooms); // set dialog title

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.howmanyroom); // set hint for edit text

            String initial = getArguments().getString("initial");
            if(!initial.equalsIgnoreCase("")) {
                editText.setText(initial); // set initial text for edit text
            }

            // button onclick, call function sendNumberOfRoom with parameter int from input in edit text
            btnPositive.setOnClickListener(v -> {
                String value = editText.getText().toString();
                if(!value.equalsIgnoreCase("")) { // if input not empty, call sendNumberOfRoom(input)
                    int input = Integer.parseInt(value);
                    onClickPositiveButton.sendNumberOfRoom(input);
                }
                getDialog().dismiss(); // close dialog
            });
        }
        else if(target.equalsIgnoreCase("RoomIDValue")){ // if click Room ID Value option
            title.setText(R.string.roomidvalue); // set dialog title

            final Spinner spinner = view.findViewById(R.id.spinnerInput);
            int initial = getArguments().getInt("initial");
            spinner.setSelection(initial); // set initial selected spinner

            // button onclick, call function sendRoomIDValue with parameter string from spinner (dropdown) menu item
            btnPositive.setOnClickListener(v -> {
                onClickPositiveButton.sendRoomIDValue(String.valueOf(spinner.getSelectedItem()));
                getDialog().dismiss(); // close dialog
            });
        }
        else if(target.equalsIgnoreCase("MaximalDueDate")){ // if click Maximal Due Date option
            title.setText(R.string.maximalduedate); // set dialog title

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.howmanyday); // set hint for edit text

            String initial = getArguments().getString("initial");
            if(!initial.equalsIgnoreCase("")) {
                editText.setText(initial); // set initial text for edit text
            }

            // button onclick, call function sendNumberOfRoom with parameter int from input in edit text
            btnPositive.setOnClickListener(v -> {
                String value = editText.getText().toString();
                if(!value.equalsIgnoreCase("")) {
                    int input = Integer.parseInt(value);
                    onClickPositiveButton.sendMaximalDueDate(input);
                }
                getDialog().dismiss(); // close dialog
            });
        }
        /* END SETUP FUNCTION FOR POSITIVE BUTTON EACH TARGET */

        return view;
    }
}
