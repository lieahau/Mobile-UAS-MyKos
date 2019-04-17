package id.ac.umn.mykos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class RoomDetailFragment extends Fragment {
    private RoomViewModel roomViewModel;
    TextView roomID, nameData, contactData, arriveData, deadlineData;
    ImageButton nameEditBtn, contactEditBtn, arriveEditBtn, deadlineEditBtn;

    public RoomDetailFragment() {
        // Required empty public constructor
    }

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

        // get args first and if args not -1
        // get Room data and set up content
        if(getArguments() != null){
            int RoomID = RoomDetailFragmentArgs.fromBundle(getArguments()).getRoomID();
            if(RoomID != -1){
                roomViewModel.GetRoom(RoomID).observe(this, new Observer<Room>() {
                    @Override
                    public void onChanged(Room newData) {
                        // Content will change automatically
                        // if you SetRoom() when changing data
                        // roomViewModel.SetRoom();
                        roomID.setText(Integer.toString(newData.getID()));
                        nameData.setText(newData.getName());
                        contactData.setText(newData.getContact());
                        arriveData.setText(newData.getArrivalDateString());
                        deadlineData.setText(newData.getPaymentDeadlineString());
                    }
                });
            }
        }

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
