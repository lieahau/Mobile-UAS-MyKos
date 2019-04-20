package id.ac.umn.mykos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class RoomDetailFragment extends Fragment implements RoomDetailDialog.OnClickPositiveButton {
    private RoomViewModel roomViewModel;
    TextView roomID, nameData, contactData, arriveData, deadlineData;
    ImageButton nameEditBtn, contactEditBtn, arriveEditBtn, deadlineEditBtn;
    private boolean hasInitial;

    public RoomDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void sendName(String input) {
        Log.e("ROOM DETAIL FRAGMENT", "sendName: found incoming input: " + input);
        /* TODO: WRITE ACTION AFTER INPUT NAME HERE */

    }

    @Override
    public void sendContact(String input) {
        Log.e("ROOM DETAIL FRAGMENT", "sendContact: found incoming input: " + input);
        /* TODO: WRITE ACTION AFTER INPUT CONTACT HERE */

    }

    private DatePickerDialog.OnDateSetListener sendArrive = (view, year, month, day) -> { // year, month, and day is Integer
        Log.e("ROOM DETAIL FRAGMENT", "sendArrive: found incoming input: " + day + "/" + month + "/" + year);
        /* TODO: WRITE ACTION AFTER INPUT ARRIVE DATE HERE */

    };

    private DatePickerDialog.OnDateSetListener sendDeadline = (view, year, month, day) -> { // year, month, and day is Integer
        Log.e("ROOM DETAIL FRAGMENT", "sendDeadline: found incoming input: " + day + "/" + month + "/" + year);
        /* TODO: WRITE ACTION AFTER INPUT ARRIVE DATE HERE */

    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /* START INIT ROOM VIEW MODEL */
        // Use activity context to make it shareable between fragment
        roomViewModel = ViewModelProviders.of(getActivity()).get(RoomViewModel.class);
        /* END INIT ROOM VIEW MODEL */
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* START CREATE TOOLBAR */
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setActionBar(toolbar);
        ((MainActivity)getActivity()).setHomeButton(R.drawable.ic_arrow_back);
        /* END CREATE TOOLBAR */

        /* START SET UP CONTENT */
        roomID = view.findViewById(R.id.RoomID);
        nameData = view.findViewById(R.id.NameData);
        contactData = view.findViewById(R.id.ContactData);
        arriveData = view.findViewById(R.id.ArriveData);
        deadlineData = view.findViewById(R.id.DeadlineData);

        nameEditBtn = view.findViewById(R.id.NameEditBtn);
        contactEditBtn = view.findViewById(R.id.ContactEditBtn);
        arriveEditBtn = view.findViewById(R.id.ArriveEditBtn);
        deadlineEditBtn = view.findViewById(R.id.DeadlineEditBtn);

        hasInitial = false;
        // get args first and if args not -1
        // get Room data and set up content
        if(getArguments() != null){
            int RoomID = RoomDetailFragmentArgs.fromBundle(getArguments()).getRoomID();
            if(RoomID != -1){
                roomViewModel.GetRoom(RoomID).observe(this, newData -> {
                    // Content will change automatically
                    // if you SetRoom() when changing data
                    // roomViewModel.SetRoom();
                    roomID.setText(Integer.toString(newData.getID()));
                    nameData.setText(newData.getName());
                    contactData.setText(newData.getContact());
                    arriveData.setText(newData.getArrivalDateString());
                    deadlineData.setText(newData.getPaymentDeadlineString());
                    hasInitial = true;
                });
            }
        }

        nameEditBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("layoutID", R.layout.dialog_edittext_text);
            bundle.putString("target", "Name");
            if(hasInitial) {
                bundle.putString("initial", nameData.getText().toString());
            }
            RoomDetailDialog roomDetailDialog = new RoomDetailDialog();
            roomDetailDialog.setArguments(bundle);
            roomDetailDialog.setTargetFragment(RoomDetailFragment.this, 200);
            roomDetailDialog.show(getFragmentManager(), "RoomDetailDialog");
        });

        contactEditBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("layoutID", R.layout.dialog_edittext_number);
            bundle.putString("target", "Contact");
            if(hasInitial) {
                bundle.putString("initial", contactData.getText().toString());
            }
            RoomDetailDialog roomDetailDialog = new RoomDetailDialog();
            roomDetailDialog.setArguments(bundle);
            roomDetailDialog.setTargetFragment(RoomDetailFragment.this, 200);
            roomDetailDialog.show(getFragmentManager(), "RoomDetailDialog");
        });

        arriveEditBtn.setOnClickListener(v -> {
            RoomDetailDialog calendarDialog = new RoomDetailDialog().newCalendarInstance();
            calendarDialog.setCallBack(sendArrive);
            calendarDialog.show(getFragmentManager().beginTransaction(), "RoomDetailDialog");
        });

        deadlineEditBtn.setOnClickListener(v -> {
            RoomDetailDialog calendarDialog = new RoomDetailDialog().newCalendarInstance();
            calendarDialog.setCallBack(sendDeadline);
            calendarDialog.show(getFragmentManager().beginTransaction(), "RoomDetailDialog");
        });

        /* END SET UP CONTENT */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();

                return true;
            case R.id.btn_reset:
                /* TODO: RESET HERE */

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.roomdetail_menu, menu);

        changeActionMenuItemsBackground(getResources().getColor(R.color.colorRed));
    }

    private void changeActionMenuItemsBackground(int color){
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);
            if (v instanceof ActionMenuView) {
                v.setBackgroundColor(color);
            }
        }
    }
}
