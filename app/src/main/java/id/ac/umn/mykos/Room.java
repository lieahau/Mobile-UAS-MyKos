package id.ac.umn.mykos;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@IgnoreExtraProperties
public class Room {
    private Integer id;
    private String name;
    private Date arrivalDate;
    private Date paymnetDeadline;
    private String contact;
    private Boolean occupied = new Boolean(false);

    public Room(){}

    public Room(Integer id, String name, Date arrivalDate, Date paymnetDeadline, String contact) {
        this.id = id;
        if(name == null) name = "";
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.paymnetDeadline = paymnetDeadline;
        if(contact == null) contact = "";
        this.contact = contact;
        this.occupied = false;
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        String str = dateFormat.format(date);
        return str;
    }

    public static Date stringToDate(String str){
        Date date = null;
        try {
            SimpleDateFormat df =  new SimpleDateFormat("dd/MM/yy");
            date = df.parse(str);
        } catch (ParseException e) {
            //handle exception
        }
        return date;
    }

    public Integer getID(){return this.id;}
    public String getName(){return name;}
    public Date getArrivalDate(){return this.arrivalDate;}
    public String getArrivalDateString(){return dateToString(this.arrivalDate);}
    public Date getPaymentDeadline(){return this.paymnetDeadline;}
    public String getPaymentDeadlineString(){return dateToString(this.paymnetDeadline);}
    public String getContact(){return this.contact;}
    public Boolean isOcupied(){return this.occupied;}

    public void setOccupied(Boolean bool){this.occupied = bool;}
    public void setArrivalDate(Date date){this.arrivalDate = date;}
    public void setPaymentDeadline(Date date){this.paymnetDeadline = date;}

}