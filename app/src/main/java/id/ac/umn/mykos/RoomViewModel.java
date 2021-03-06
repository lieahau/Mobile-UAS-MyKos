package id.ac.umn.mykos;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// For containing List of Room available
// Useful for sharing data between fragment
// will be use with live data

public class RoomViewModel extends ViewModel {

    private DatabaseReference mDatabase = MainActivity.GetFirebaseInstance().getReference();
    private String idUser;

    // hold all value
    private MutableLiveData<ArrayList<Room>> datas = new MutableLiveData<ArrayList<Room>>();
    private MutableLiveData<ArrayList<Room>> dashboardDatas = new MutableLiveData<ArrayList<Room>>();
    private MutableLiveData<ArrayList<Room>> dashboardBackup = new MutableLiveData<ArrayList<Room>>();
    private MutableLiveData<Room> roomData = new MutableLiveData<Room>();
    private String sortSave;
    private Integer maxDueDate;

    // Get Firebase
    public void getFirebase(String idUser){
        this.idUser = idUser;
        Query userDB = mDatabase.child(idUser);
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Room> listRoom = new ArrayList<Room>();

                for (DataSnapshot postSnapshot: dataSnapshot.child("rooms").getChildren()) {
                    Room room = postSnapshot.getValue(Room.class);
                    listRoom.add(room);
                }

                // check for null reference error
                if(dataSnapshot.child("maxDueDate").getValue(Integer.class) != null)
                    setMaxDueDate(dataSnapshot.child("maxDueDate").getValue(Integer.class));

                Log.i("KOK", ""+maxDueDate);
                SetData(listRoom);
                SetDashboardData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // kalo gagal
            }
        });
    }

    public void setPlaceholder(Integer idx){
        ArrayList<Room> temp = datas.getValue();
        mDatabase.child(idUser).child("rooms").removeValue();

        for(int i=0; i<idx; i++){
            Room newRoom;
            if(temp == null || temp.size() <= i || temp.get(i) == null) newRoom = new Room(i+1, "---", null, null, "---");
            else newRoom = temp.get(i);

            mDatabase.child(idUser).child("rooms").child(Integer.toString(i+1)).setValue(newRoom);
        }

    }

    public void changeRoom(int id, Room newRoom){
        // set data in firebase
        mDatabase.child(idUser).child("rooms").child(Integer.toString(id)).setValue(newRoom);
        // set data in mutablelivedata so you don't need to change UI manually
        SetRoom(newRoom);
    }

    public void setMaxDueDate(int i){ this.maxDueDate = i; }
    public Integer getMaxDueDate(){ return this.maxDueDate; }
    public void changeMaxDueDate(int i){
        mDatabase.child(idUser).child("maxDueDate").setValue(i);
    }
    // Set data from Main Thread
    private void SetData(ArrayList<Room> data){
        datas.setValue(data);
    }
    private void SetDashboardData(){
        ArrayList<Room> newDatas = new ArrayList<Room>();
        for (Room data : datas.getValue()) {
            if(data.getStatus().compareTo(Room.STATUS_EMPTY) != 0){
                newDatas.add(data);
            }
        }

        dashboardDatas.setValue(newDatas);
        dashboardBackup.setValue(newDatas);
    }

    // Set data from different thread
    // useful for Room framework or async method
    private void SetDataAsync(ArrayList<Room> data){
        datas.postValue(data);
    }

    private void SetDashboardDataAsync(ArrayList<Room> data){
        dashboardDatas.postValue(data);
    }

    // Get Data and return LiveData to be observed
    public LiveData<ArrayList<Room>> GetData(){
        return datas;
    }

    // Get data for dashboard only
    public LiveData<ArrayList<Room>> GetDashboardData(){
        return dashboardDatas;
    }
    public LiveData<ArrayList<Room>> GetDashboardBackup(){
        return dashboardBackup;
    }


    public void sortRoom(Context c, String search, String sort){
        ArrayList<Room> listData = new ArrayList<Room>();

        if(search == null){ // GET ALREADY SEARCHED LIST
            listData = GetDashboardData().getValue();
        }
        else if(search.compareToIgnoreCase("")==0){ // GET FULL LIST
            listData = GetDashboardBackup().getValue();
        }
        else{ // GET SEARCH LIST
            for (Room room : GetDashboardBackup().getValue()) {
                if (room.getName().toLowerCase().contains(search.toLowerCase())) {
                    listData.add(room);
                }
            }
        }

        if(sort == null) {} // Past SORTED
        else sortSave = sort; // NEW SORT

        if(sortSave != null) { //SORT
            Collections.sort(listData, new Comparator<Room>() {
                @Override
                public int compare(Room room1, Room room2) {
                    int result = 0;
                    if (sortSave.equalsIgnoreCase(c.getResources().getString(R.string.sortbyname))) { // SORT BY NAME
                        result = room1.getName().compareToIgnoreCase(room2.getName());
                    } else if (sortSave.equalsIgnoreCase(c.getResources().getString(R.string.sortbyid))) { // SORT BY ID
                        result = room1.getID() < room2.getID() ? -1 : (room1.getID() > room2.getID()) ? 1 : 0;
                    } else if (sortSave.equalsIgnoreCase(c.getResources().getString(R.string.sortbydeadline))) { // SORT BY DEADLINE
                        if (room1.getPaymentDeadline() == null && room2.getPaymentDeadline() == null) result = 0;
                        else if (room1.getPaymentDeadline() == null) result = 1;
                        else if (room2.getPaymentDeadline() == null) result = -1;
                        else result = room1.getPaymentDeadline().compareTo(room2.getPaymentDeadline());
                    }
                    return result;
                }
            });
        }
        dashboardDatas.setValue(listData);
    }

    // Set Room
    // this is only container for UI(not connected to database)
    public void SetRoom(Room room){
        roomData.setValue(room);
    }

    public void SetRoomAsync(Room room){
        roomData.postValue(room);
    }

    // Find invidual Room
    public LiveData<Room> GetRoom(int RoomID){
        // Get invidual room data
        if(roomData.getValue() == null){
            roomData.setValue(SearchRoom(RoomID));
        }else if(roomData.getValue().getID() != RoomID){
            roomData.setValue(SearchRoom(RoomID));
        }

        return roomData;
    }

    private Room SearchRoom(int RoomID){
        Room room = new Room();
        if(datas.getValue() != null){
            for (Room res : datas.getValue()){
                if(res.getID() == RoomID){
                    room = res;
                    break;
                }
            }
        }

        return room;
    }
}
