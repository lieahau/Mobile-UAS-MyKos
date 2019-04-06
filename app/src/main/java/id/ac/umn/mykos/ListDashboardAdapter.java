package id.ac.umn.mykos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListDashboardAdapter extends RecyclerView.Adapter<ListDashboardAdapter.ListDashboardView> {
    // Perlu data struktur terlebih dahulu

    // Placeholder
    ArrayList<String> placeholders = new ArrayList<String>();

    public ListDashboardAdapter(){}

    public ListDashboardAdapter(ArrayList<String> newList){
        SetData(newList);
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
        TextView RoomIDText;
        TextView NameText;
        TextView DeadlineText;

        public ListDashboardView(@NonNull View itemView) {
            super(itemView);
            RoomIDText = itemView.findViewById(R.id.RoomID);
            NameText = itemView.findViewById(R.id.NameData);
            DeadlineText = itemView.findViewById(R.id.DeadlineData);
        }

        public void bind(){
            RoomIDText.setText("");
            NameText.setText("");
            DeadlineText.setText("");
        }
    }

}
