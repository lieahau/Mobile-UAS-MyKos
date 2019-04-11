package id.ac.umn.mykos;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// For containing List of Room available
// Useful for sharing data between fragment
// will be use with live data

public class RoomViewModel extends ViewModel {
    // Perlu data struktur terlebih dahulu

    // Placeholder
    MutableLiveData<ArrayList<String>> placeholders = new MutableLiveData<ArrayList<String>>();

    // Set data from Main Thread
    public void SetData(ArrayList<String> data){
        placeholders.setValue(data);
    }

    // Set data from different thread
    // useful for Room framework or async method
    public void SetDataAsync(ArrayList<String> data){
        placeholders.postValue(data);
    }

    // Get Data and return LiveData to be observed
    public LiveData<ArrayList<String>> GetData(){
        return placeholders;
    }

    // Fetch data from database
    public void FetchData(){

    }
}
