package id.ac.umn.mykos;

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
    private MutableLiveData<Room> roomData = new MutableLiveData<Room>();

    // Get Firebase
    public void getFirebase(String idUser){
        this.idUser = idUser;
        Query userDB = mDatabase.child(idUser).child("rooms");
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Room> listRoom = new ArrayList<Room>();

                if(!dataSnapshot.exists()) setPlaceholder(10);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Room room = postSnapshot.getValue(Room.class);
                    listRoom.add(room);
                }

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
        for(int i=1; i<=idx/3; i++){
            Room newRoom = new Room(i, "Orang", "01/01/2001", "31/12/2099", "---");
            mDatabase.child(idUser).child("rooms").child(Integer.toString(i)).setValue(newRoom);
        }
        for(int i=idx/3+1; i<=idx*2/3; i++){
            Room newRoom = new Room(i, "Makhlus halus", "01/01/2021", null, "---");
            mDatabase.child(idUser).child("rooms").child(Integer.toString(i)).setValue(newRoom);
        }
        for(int i=idx*2/3+1; i<=idx; i++){
            Room newRoom = new Room(i, "Empty", null, null, "---");
            mDatabase.child(idUser).child("rooms").child(Integer.toString(i)).setValue(newRoom);
        }
    }

    public void changeRoom(int id, Room newRoom){
        mDatabase.child(idUser).child("rooms").child(Integer.toString(id)).setValue(newRoom);
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
