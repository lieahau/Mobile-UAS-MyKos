package id.ac.umn.mykos;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class ListDashboardDiffUtil extends DiffUtil.Callback {
    // Perlu list yang lama dan yang baru

    //placeholders
    ArrayList<Room> oldPlaceholders = new ArrayList<Room>();
    ArrayList<Room> newPlaceholders = new ArrayList<Room>();

     public ListDashboardDiffUtil(){}

    public ListDashboardDiffUtil(ArrayList<Room> oldPlaceholders, ArrayList<Room> newPlaceholders){
        this.oldPlaceholders = oldPlaceholders;
        this.newPlaceholders = newPlaceholders;
    }

    @Override
    public int getOldListSize() {
        return oldPlaceholders.size();
    }

    @Override
    public int getNewListSize() {
        return newPlaceholders.size();
    }

    // akan membandingkan idnya
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // untuk sementara
        return true;
    }

    // akan membandingkan kontennya
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // untuk sementara
//        if(oldPlaceholders.get(oldItemPosition).compareTo(newPlaceholders.get(newItemPosition)) != 0){
        if(oldPlaceholders.get(oldItemPosition).equals(newPlaceholders.get(newItemPosition)) != false){

                return false;
        }

        return true;
    }

    // akan memberikan object berisi data apa saja yg berbeda
    // yang akan mentrigger onBindViewHolder dengan payload
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
