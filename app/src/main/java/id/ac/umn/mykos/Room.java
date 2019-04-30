package id.ac.umn.mykos;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@IgnoreExtraProperties
public class Room {
    public static final String STATUS_EMPTY = "Empty";
    public static final String STATUS_OCCUPIED = "Occupied";
    public static final String STATUS_RESERVED = "Reserved";

    private Integer id;
    private String name;
    private Date arrivalDate;
    private Date paymentDeadline;
    private String contact;

    public Room(){}

    public Room(Integer id, String name, String arrivalDate, String paymentDeadline, String contact) {
        this.id = id;

        if(name == null) name = "";
        this.name = name;

        if(arrivalDate == null) this.arrivalDate = null;
        else this.arrivalDate = stringToDate(arrivalDate);

        if(paymentDeadline == null) this.paymentDeadline = null;
        else this.paymentDeadline = stringToDate(paymentDeadline);

        if(contact == null) contact = "";
        this.contact = contact;
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String str = dateFormat.format(date);
        return str;
    }

    public static Date stringToDate(String str){
        Date date = null;
        try {
            SimpleDateFormat df =  new SimpleDateFormat("dd/MM/yyyy");
            date = df.parse(str);
        } catch (ParseException e) {
            //handle exception
        }
        return date;
    }

    public Integer getID(){return this.id;}

    public static String IDNumericIntoAlphabet(int id){
        String res = "";
        id -= 1;
        while(id >= 0){
            int i = 65 + id%26;
            char c = (char)i;
            res += c;
            id -= 26;
        }

        return res;
    }

    public String getName(){return name;}
    public Date getArrivalDate(){return this.arrivalDate;}
    public String getArrivalDateString(){
        if(this.arrivalDate == null) return "--/--/----";
        return dateToString(this.arrivalDate);
    }
    public Date getPaymentDeadline(){return this.paymentDeadline;}
    public String getPaymentDeadlineString(){
            if(this.paymentDeadline == null) return "--/--/----";
            return dateToString(this.paymentDeadline);
    }
    public String getContact(){return this.contact;}
    public String getStatus(){
        String str;
        Date now = new Date();
        if (this.arrivalDate == null) str = STATUS_EMPTY;
        else if (this.arrivalDate.compareTo(new Date()) <= 0) str = STATUS_OCCUPIED;
        else str = STATUS_RESERVED;
        return str;
    }

    public void setName(String name){this.name = name;}
    public void setContact(String contact){this.contact = contact;}
    public void setArrivalDate(Date date){this.arrivalDate = date;}
    public void setPaymentDeadline(Date date){this.paymentDeadline = date;}

}