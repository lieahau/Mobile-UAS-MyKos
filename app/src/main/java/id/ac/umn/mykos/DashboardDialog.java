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

    public interface OnClickPositiveButton{
        void sendSort(String input);
        void sendSearch(String input);
    }
    private OnClickPositiveButton onClickPositiveButton;

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
        int id_layout = getArguments().getInt("layoutID");
        final View view = inflater.inflate(id_layout, container, false);

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String target = getArguments().getString("target");
        TextView btnPositive = view.findViewById(R.id.btn_positive);
        TextView btnNegative = view.findViewById(R.id.btn_negative);
        btnNegative.setOnClickListener(v -> getDialog().dismiss());

        TextView title = view.findViewById(R.id.dialog_title);
        if(target.equalsIgnoreCase("Search")){
            title.setText(R.string.search);

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.inputname);
            btnPositive.setOnClickListener(v -> {
                String value = editText.getText().toString();
                onClickPositiveButton.sendSearch(value);
                getDialog().dismiss();
            });
        }
        else if(target.equalsIgnoreCase("Sort")){
            title.setText(R.string.sort);

            btnPositive.setOnClickListener(v -> {
                String value;
                RadioGroup radioGroup = view.findViewById(R.id.radio_sort_group);
                int selectedID = radioGroup.getCheckedRadioButtonId();
                if(selectedID == -1) {
                    value = "";
                }
                else{
                    RadioButton radioButton = view.findViewById(selectedID);
                    value = radioButton.getText().toString();
                }
                onClickPositiveButton.sendSort(value);
                getDialog().dismiss();
            });
        }

        return view;
    }
}
