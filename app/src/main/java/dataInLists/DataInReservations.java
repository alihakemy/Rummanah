package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInReservations implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInReservations() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public ArrayList<Data> data = new ArrayList<>();

    public class Data implements Serializable {
        public int id;
        public int doctor_lawyer_id;
        public String date;
        public String time;
        public byte status;
        public byte user_confirm;
        public String phone;
        public double latitude;
        public double longitude;
        public String image;
        public String name_en;
        public String name_ar;
        public String professional_title_en;
        public String professional_title_ar;
    }
}
