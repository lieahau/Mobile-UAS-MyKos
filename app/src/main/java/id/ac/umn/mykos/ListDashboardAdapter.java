package id.ac.umn.mykos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListDashboardAdapter extends RecyclerView.Adapter<ListDashboardAdapter.ListDashboardView> {
    // Perlu data struktur terlebih dahulu

    // Placeholder
    ArrayList<String> placeholders = new ArrayList<String>();

    // Reference to NavController
    NavController navController;

    public ListDashboardAdapter(){}

    public ListDashboardAdapter(ArrayList<String> newList, NavController navController){
        SetData(newList);
        this.navController = navController;
    }

    // let ListDashboardDiffUtil make change to data
    public void SetData(ArrayList<String> newList){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ListOverviewDiffUtil(placeholders, newList));
        placeholders = newList;
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
        holder.bind();
    }

    @Override
    public void onBindViewHolder(@NonNull ListDashboardView holder, int position, @NonNull List<Object> payloads) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return placeholders.size();
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

        public void bind(){
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(DashboardFragmentDirections.actionDashboardFragmentToRoomDetailFragment(), extras);
                }
            });

            RoomIDText.setText("");
            NameText.setText("");
            DeadlineText.setText("");
        }
    }

}
