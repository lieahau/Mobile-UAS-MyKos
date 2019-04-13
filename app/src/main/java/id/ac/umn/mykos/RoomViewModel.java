package id.ac.umn.mykos;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// For containing List of Room available
// Useful for sharing data between fragment
// will be use with live data

public class RoomViewModel extends ViewModel {
    // Perlu data struktur terlebih dahulu //udah

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // Placeholder
    MutableLiveData<ArrayList<Room>> placeholders = new MutableLiveData<ArrayList<Room>>();

    // Set data from Main Thread
    public void SetData(ArrayList<Room> data){
        placeholders.setValue(data);
    }

    // Get Feirbase
    public void getFirebase(String idUser){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Room>> genericTypeIndicator =new GenericTypeIndicator<ArrayList<Room>>(){};
//                ArrayList<Room> listRoom = dataSnapshot.child("users").child(idUser).getValue(genericTypeIndicator);
                ArrayList<Room> listRoom = dataSnapshot.child("rooms").getValue(genericTypeIndicator);

                if(listRoom == null){
                    setFirebase("", 10);
                    listRoom = dataSnapshot.child("rooms").getValue(genericTypeIndicator);
                }

//                for (Room room: listRoom) Log.e("Room", Integer.toString(room.getID()) + room.getName() + " " + room.getPaymentDeadlineString());
                SetData(listRoom);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // kalo gagal
            }
        });
    }


    // Set data from different thread
    // useful for Room framework or async method
    public void SetDataAsync(ArrayList<Room> data){
        placeholders.postValue(data);
    }

    public void setFirebase(String idUser, Integer idx){
        for(int i=0; i<idx/3; i++){
            Room newRoom = new Room(i+1, "Orang", "01/01/2021", "31/12/2099", "Kontak");
//            mDatabase.child("users").child(idUser).child(Integer.toString(i)).setValue(newRoom);
            mDatabase.child("rooms").child(Integer.toString(i)).setValue(newRoom);
        }
        for(int i=idx/3; i<idx*2/3; i++){
            Room newRoom = new Room(i+1, "Makhlus halus", "01/01/2001", null, "Empty");
//            mDatabase.child("users").child(idUser).child(Integer.toString(i)).setValue(newRoom);
            mDatabase.child("rooms").child(Integer.toString(i)).setValue(newRoom);
        }
        for(int i=idx*2/3; i<idx; i++){
            Room newRoom = new Room(i+1, "Empty", null, null, "Empty");
//            mDatabase.child("users").child(idUser).child(Integer.toString(i)).setValue(newRoom);
            mDatabase.child("rooms").child(Integer.toString(i)).setValue(newRoom);
        }
    }

    // Get Data and return LiveData to be observed
    public LiveData<ArrayList<Room>> GetData(){
        return placeholders;
    }

    // Fetch data from database
    public void FetchData(){

    }
}
