package id.ac.umn.mykos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RoomDetailDialog extends DialogFragment {
    /* START CREATE INTERFACE FOR POSITIVE BUTTON FUNCTION */
    public interface OnClickPositiveButton{
        void sendName(String input);
        void sendContact(String input);
        void sendArrive(String input);
        void sendDeadline(String input);
    }
    private OnClickPositiveButton onClickPositiveButton;
    /* END CREATE INTERFACE FOR POSITIVE BUTTON FUNCTION */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            onClickPositiveButton = (OnClickPositiveButton) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("ROOM DETAIL DIALOG", "onAttach: " + e.getMessage());
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
        if(target.equalsIgnoreCase("Name")){ // if click Input Name button
            title.setText(R.string.name0); // set dialog title

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.inputname); // set hint for edit text
            editText.setText(getArguments().getString("initial", ""));

            // button onclick, call function sendName with parameter from String input in edit text
            btnPositive.setOnClickListener(v -> {
                String value = editText.getText().toString();
                onClickPositiveButton.sendName(value);

                getDialog().dismiss(); // close dialog
            });
        }
        else if(target.equalsIgnoreCase("Contact")){ // if click Input Contact button
            title.setText(R.string.contact0); // set dialog title

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.inputcontact); // set hint for edit text
            editText.setText(getArguments().getString("initial", ""));

            // button onclick, call function sendContact with parameter String from input in edit text
            btnPositive.setOnClickListener(v -> {
                String value = editText.getText().toString();
                onClickPositiveButton.sendContact(value);

                getDialog().dismiss(); // close dialog
            });
        }
        else if(target.equalsIgnoreCase("ArriveDate")){ // if click Input Arrive Date button

        }
        else if(target.equalsIgnoreCase("PayDeadline")){ // if click Input Pay Deadline button

        }
        /* END SETUP FUNCTION FOR POSITIVE BUTTON EACH TARGET */

        return view;
    }
}
