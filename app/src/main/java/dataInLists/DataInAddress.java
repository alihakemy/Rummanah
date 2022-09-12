package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInAddress implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInAddress() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public ArrayList<Address> data = new ArrayList<>();

    public class Address {
        public int id;
        public int user_id;
        public double latitude ;
        public double longitude ;
        public String title;
        public byte address_type;
        public int area_id;
        public String area_name;
        public String gaddah;
        public String building;
        public String floor;
        public String apartment_number;
        public String street;
        public String extra_details;
        public String phone;
        public String piece;
        public boolean is_default;
        public String created_at;
        public String updated_at;

    }
}
