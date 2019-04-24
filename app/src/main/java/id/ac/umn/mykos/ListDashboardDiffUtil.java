package id.ac.umn.mykos;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class ListDashboardDiffUtil extends DiffUtil.Callback {
    public static final String NAME = "NAME";
    public static final String DEADLINE = "DEADLINE";

    // Perlu list yang lama dan yang baru
    ArrayList<Room> oldDatas = new ArrayList<Room>();
    ArrayList<Room> newDatas = new ArrayList<Room>();

     public ListDashboardDiffUtil(){}

    public ListDashboardDiffUtil(ArrayList<Room> oldDatas, ArrayList<Room> newDatas){
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
        if(oldDatas.get(oldItemPosition).getID().intValue() == newDatas.get(newItemPosition).getID().intValue()){
            return true;
        }
        return false;
    }

    // akan membandingkan kontennya
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldDatas.get(oldItemPosition).getName().compareTo(newDatas.get(newItemPosition).getName()) != 0){
                return false;
        }

        if (oldDatas.get(oldItemPosition).getPaymentDeadlineString().compareTo(newDatas.get(newItemPosition).getPaymentDeadlineString()) != 0){
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

        if (oldDatas.get(oldItemPosition).getName().compareTo(newDatas.get(newItemPosition).getName()) != 0){
            payload.putString(NAME, newDatas.get(newItemPosition).getName());
        }

        if (oldDatas.get(oldItemPosition).getPaymentDeadlineString().compareTo(newDatas.get(newItemPosition).getPaymentDeadlineString()) != 0){
            payload.putString(DEADLINE, newDatas.get(newItemPosition).getPaymentDeadlineString());
        }

         return payload;
    }
}
