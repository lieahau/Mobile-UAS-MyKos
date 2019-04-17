package id.ac.umn.mykos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListDashboardAdapter extends RecyclerView.Adapter<ListDashboardAdapter.ListDashboardView> {
    ArrayList<Room> datas = new ArrayList<Room>();

    // Reference to NavController
    NavController navController;

    public ListDashboardAdapter(){}

    public ListDashboardAdapter(ArrayList<Room> newList, NavController navController){
        SetData(newList);
        this.navController = navController;
    }

    // let ListDashboardDiffUtil make change to data
    public void SetData(ArrayList<Room> newList){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ListOverviewDiffUtil(datas, newList));
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
        Room room = datas.get(position);
        holder.bind(room);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDashboardView holder, int position, @NonNull List<Object> payloads) {
        Room room = datas.get(position);

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

            RoomIDText.setText(Integer.toString(room.getID()));
            NameText.setText(room.getName());
            DeadlineText.setText(room.getPaymentDeadlineString());
        }
    }

}
