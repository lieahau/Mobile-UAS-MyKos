package id.ac.umn.mykos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListOverviewAdapter extends RecyclerView.Adapter<ListOverviewAdapter.ListOverviewView> {
    // Perlu data struktur terlebih dahulu

    // Placeholder
    ArrayList<String> placeholders = new ArrayList<String>();

    public ListOverviewAdapter(){}

    public ListOverviewAdapter(ArrayList<String> newList){
        SetData(newList);
    }

    // let ListDashboardDiffUtil make change to data
    public void SetData(ArrayList<String> newList){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ListDashboardDiffUtil(placeholders, newList));
        placeholders = newList;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ListOverviewView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate list_overview layout to RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_overview, parent, false);
        return new ListOverviewView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOverviewView holder, int position) {
        // Setup data data into ListDashboardView
        holder.bind();
    }

    @Override
    public void onBindViewHolder(@NonNull ListOverviewView holder, int position, @NonNull List<Object> payloads) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return placeholders.size();
    }

    class ListOverviewView extends RecyclerView.ViewHolder{
        TextView RoomIDText;
        TextView RoomStatusText;

        public ListOverviewView(@NonNull View itemView) {
            super(itemView);
            RoomIDText = itemView.findViewById(R.id.RoomID);
            RoomStatusText = itemView.findViewById(R.id.RoomStatusData);
        }

        public void bind(){
            RoomIDText.setText("");
            RoomStatusText.setText("");
        }
    }
}
