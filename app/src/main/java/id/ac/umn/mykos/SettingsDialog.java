package id.ac.umn.mykos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SettingsDialog extends DialogFragment {

    public interface OnClickPositiveButton{
        void sendNumberOfRoom(int input);
        void sendRoomIDValue(String input);
        void sendMaximalDueDate(int input);
    }
    private OnClickPositiveButton onClickPositiveButton;

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
        int id_layout = getArguments().getInt("layoutID");
        final View view = inflater.inflate(id_layout, container, false);

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView btnPositive = view.findViewById(R.id.btn_positive);
        TextView btnNegative = view.findViewById(R.id.btn_negative);

        btnNegative.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getDialog().dismiss();
            }
        });

        TextView title = view.findViewById(R.id.dialog_title);
        String target = getArguments().getString("target");
        if(target.equalsIgnoreCase("NumberOfRoom")){
            title.setText(R.string.numberofrooms);

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.howmanyroom);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = editText.getText().toString();
                    if(!value.equalsIgnoreCase("")) {
                        int input = Integer.parseInt(value);
                        onClickPositiveButton.sendNumberOfRoom(input);
                    }
                    getDialog().dismiss();
                }
            });
        }
        else if(target.equalsIgnoreCase("RoomIDValue")){
            title.setText(R.string.roomidvalue);
            final Spinner spinner = view.findViewById(R.id.spinnerInput);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPositiveButton.sendRoomIDValue(String.valueOf(spinner.getSelectedItem()));
                    getDialog().dismiss();
                }
            });
        }
        else if(target.equalsIgnoreCase("MaximalDueDate")){
            title.setText(R.string.maximalduedate);

            final EditText editText = view.findViewById(R.id.editTextInput);
            editText.setHint(R.string.howmanyday);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = editText.getText().toString();
                    if(!value.equalsIgnoreCase("")) {
                        int input = Integer.parseInt(value);
                        onClickPositiveButton.sendMaximalDueDate(input);
                    }
                    getDialog().dismiss();
                }
            });
        }

        return view;
    }
}
