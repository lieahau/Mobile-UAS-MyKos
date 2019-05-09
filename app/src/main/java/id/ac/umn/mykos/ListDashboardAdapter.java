package id.ac.umn.mykos;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListDashboardAdapter extends RecyclerView.Adapter<ListDashboardAdapter.ListDashboardView> {
    ArrayList<Room> datas = new ArrayList<Room>();

    // Reference to NavController and activity
    NavController navController;
    Activity activity;

    public ListDashboardAdapter(){}

    public ListDashboardAdapter(ArrayList<Room> newList, NavController navController, Activity activity){
        SetData(newList);
        this.navController = navController;
        this.activity = activity;
    }

    // let ListDashboardDiffUtil make change to data
    public void SetData(ArrayList<Room> newList){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ListDashboardDiffUtil(datas, newList), true);
        datas = newList;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ListDashboardView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate list_dashboard layout to RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dashboard, parent, false);
        return new ListDashboardView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDashboardView holder, int position) {
        // Setup data data into ListDashboardView
        Log.d("Debug", "Normal Bind");
        Room room = datas.get(position);
        holder.bind(room);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDashboardView holder, int position, @NonNull List<Object> payloads) {
        Room room = datas.get(position);
        Log.d("Debug", "Payload Empty: "+payloads.isEmpty());
        if(!payloads.isEmpty()){
            Bundle payload = (Bundle) payloads.get(0);
            if(payload.containsKey(ListDashboardDiffUtil.NAME)){
                room.setName(payload.getString(ListDashboardDiffUtil.NAME));
            }

            if(payload.containsKey(ListDashboardDiffUtil.DEADLINE)){
                room.setName(payload.getString(ListDashboardDiffUtil.DEADLINE));
            }
        }

        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ListDashboardView extends RecyclerView.ViewHolder{
        View container;
        TextView RoomIDLabel;
        TextView NameLabel;
        TextView DeadlineLabel;

        TextView RoomIDText;
        TextView NameText;
        TextView DeadlineText;

        TextView noticeText;

        FragmentNavigator.Extras extras;

        public ListDashboardView(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            RoomIDLabel = itemView.findViewById(R.id.RoomIDLabel);
            NameLabel = itemView.findViewById(R.id.NameLabel);
            DeadlineLabel = itemView.findViewById(R.id.DeadlineLabel);

            RoomIDText = itemView.findViewById(R.id.RoomID);
            NameText = itemView.findViewById(R.id.NameData);
            DeadlineText = itemView.findViewById(R.id.DeadlineData);

            noticeText = itemView.findViewById(R.id.NOTICE);

            extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(RoomIDText, "RoomID")
                    .addSharedElement(RoomIDLabel, "RoomIDLabel")
                    .addSharedElement(NameText, "NameData")
                    .addSharedElement(NameLabel, "NameLabel")
                    .addSharedElement(DeadlineText, "DeadlineData")
                    .addSharedElement(DeadlineText, "DeadlineLabel")
                    .build();
        }

        public void bind(Room room){
            container.setOnClickListener((v) -> {
                // Set args(using navigation framework)
                DashboardFragmentDirections.ActionDashboardFragmentToRoomDetailFragment action =
                        DashboardFragmentDirections.actionDashboardFragmentToRoomDetailFragment();
                action.setRoomID(room.getID());

                navController.navigate(action, extras);
            });

            // Check ID convention setting
            // if ID_NUMERIC then dashboard and overview will show number
            // if ID_ALPHABET then dashboard and overview will show character
            if(SharedPrefHandler.GetPrefInt(activity, SharedPrefHandler.KEY_ID) == SharedPrefHandler.ID_NUMERIC)
                RoomIDText.setText(Integer.toString(room.getID()));
            else
                RoomIDText.setText(Room.IDNumericIntoAlphabet(room.getID().intValue()));

            NameText.setText(room.getName());

            // Set notice text
            // if pass deadline but still inside deadline + maximal due date then set notice with orange text
            // if pass deadline + maximal due date then set notice with red text
            if(room.getPaymentDeadline() != null){
                DeadlineText.setText(room.getPaymentDeadlineString());
                Date todayDate = Calendar.getInstance().getTime();
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    todayDate = dateFormat.parse(dateFormat.format(todayDate));
                }
                catch (ParseException e){
                    Log.e("Error", "Error parsing date");
                }
                long daypass = (todayDate.getTime() - room.getPaymentDeadline().getTime()) / (24*60*60*1000);
                int maxDeadline = SharedPrefHandler.GetPrefInt(activity, SharedPrefHandler.KEY_DUEDATE);
                Log.d("Debug", room.getName()+" daypass: "+daypass+", maxDeadline: "+maxDeadline);
                if((int)daypass >= 0 && (int)daypass <= maxDeadline){
                    noticeText.setText(room.getName()+ " pass payment date( "+ daypass +" days have passed )");
                    noticeText.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                }
                else if((int)daypass > maxDeadline){
                    noticeText.setText("This person pass maximal payment date");
                    noticeText.setTextColor(activity.getResources().getColor(R.color.colorRed));
                }
            }
        }
    }

}
