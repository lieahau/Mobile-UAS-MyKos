package id.ac.umn.mykos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DashboardDialog extends DialogFragment {

    /* START CREATE INTERFACE FOR POSITIVE BUTTON FUNCTION */
    public interface OnClickPositiveButton{
        void sendSort(String input);
        void sendSearch(String input);
    }
    private OnClickPositiveButton onClickPositiveButton;
    /* END CREATE INTERFACE FOR POSITIVE BUTTON FUNCTION */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            onClickPositiveButton = (OnClickPositiveButton) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("DASHBOARD DIALOG", "onAttach: " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* START SETUP DIALOG VIEW */
        // get layout id based on arguments, then inflate it
        int id_layout = getArguments().getInt("layoutID");
        final View view = inflater.inflate(id_layout, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        /* END SETUP DIALOG VIEW */

        /* START SETUP FUNCTION FOR NEGATIVE BUTTON */
        TextView btnNegative = view.findViewById(R.id.btn_negative);
        btnNegative.setOnClickListener(v -> getDialog().dismiss()); // close dialog
        /* END SETUP FUNCTION FOR NEGATIVE BUTTON */

        /* START SETUP FUNCTION FOR POSITIVE BUTTON EACH TARGET */
        String target = getArguments().getString("target");
        TextView btnPositive = view.findViewById(R.id.btn_positive);
        TextView title = view.findViewById(R.id.dialog_title);
        if(target.equalsIgnoreCase("Search")){ // if click search icon in toolbar
            title.setText(R.string.search); // set dialog title

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.inputname); // set hint for edit text
            editText.setText(getArguments().getString("initial", ""));

            // button onclick, call function sendSearch with parameter string from input in edit text
            btnPositive.setOnClickListener(v -> {
                String value = editText.getText().toString();
                onClickPositiveButton.sendSearch(value);
                getDialog().dismiss(); // close dialog
            });
        }
        else if(target.equalsIgnoreCase("Sort")){ // if click sort icon in toolbar
            title.setText(R.string.sort); // set dialog title

            int initial = getArguments().getInt("initial", R.id.radio_sort_id);
            final RadioGroup radioGroup = view.findViewById(R.id.radio_sort_group);
            radioGroup.check(initial);

            // button onclick, call function sendSort with parameter String based on selected radio
            btnPositive.setOnClickListener(v -> {
                String value;

                int selectedID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(selectedID);
                value = radioButton.getText().toString();
                onClickPositiveButton.sendSort(value);
                getDialog().dismiss(); // close dialog
            });
        }
        /* END SETUP FUNCTION FOR POSITIVE BUTTON EACH TARGET */

        return view;
    }
}
