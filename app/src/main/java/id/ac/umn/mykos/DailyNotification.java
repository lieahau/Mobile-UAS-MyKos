package id.ac.umn.mykos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Date;

public class DailyNotification extends BroadcastReceiver {
    boolean late = false;
    String text;
    @Override
    public void onReceive(Context context, Intent intent) {
        String bigtext = roomDue();

//        Intent i = new Intent(this.get, MainActivity.class);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ch1")
                .setSmallIcon(R.drawable.mykos_icon)
                .setContentTitle("MyKos")
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().
                        bigText(bigtext))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification n = builder.build();
        nm.notify(111 , n);
    }

    public String roomDue(){
        ArrayList<Room> rooms = SharedPrefHandler.GetPrefDummy();
        late = false;
        String deadMessage = "";
        String contentStr;
        for(int i = 0; i < rooms.size(); i++){
            Room r = rooms.get(i);
            if(r.getDue() >=0){
                deadMessage += "Room " + r.getID() + " : " + r.getName() + " overdue " + r.getDue() + " days.\n";
                late = true;
            }
        }
        if(!late){
            contentStr = "No room deadline reached.";
            text = "Room payment is not overdue.";
        }
        else{
            contentStr = "Room that have reached the deadline: \n" + deadMessage;
            text = "Room payment overdue!";
        }
        return contentStr;
    }
}
