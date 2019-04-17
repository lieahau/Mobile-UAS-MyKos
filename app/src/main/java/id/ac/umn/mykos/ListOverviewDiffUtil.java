package id.ac.umn.mykos;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class ListOverviewDiffUtil extends DiffUtil.Callback {
    public static final String STATUS = "STATUS";

    // Perlu list yang lama dan yang baru
    ArrayList<Room> oldDatas = new ArrayList<Room>();
    ArrayList<Room> newDatas = new ArrayList<Room>();

    public ListOverviewDiffUtil(){}

    public ListOverviewDiffUtil(ArrayList<Room> oldDatas, ArrayList<Room> newDatas){
        this.oldDatas = oldDatas;
        this.newDatas = newDatas;
    }

    @Override
    public int getOldListSize() {
        return oldDatas.size();
    }

    @Override
    public int getNewListSize() {
        return newDatas.size();
    }

    // akan membandingkan idnya
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if(oldDatas.get(oldItemPosition).getID() == newDatas.get(oldItemPosition).getID()){
            return true;
        }
        return false;
    }

    // akan membandingkan kontennya
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldDatas.get(oldItemPosition).getStatus().compareTo(newDatas.get(newItemPosition).getStatus()) != 0){
            return false;
        }

        return true;
    }

    // akan memberikan object berisi data apa saja yg berbeda
    // yang akan mentrigger onBindViewHolder dengan payload
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Bundle payload = new Bundle();

        if (oldDatas.get(oldItemPosition).getStatus().compareTo(newDatas.get(newItemPosition).getStatus()) != 0){
            payload.putString(STATUS, newDatas.get(newItemPosition).getStatus());
        }

        return payload;
    }
}
