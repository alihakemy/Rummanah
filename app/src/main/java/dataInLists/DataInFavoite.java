package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInFavoite implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInFavoite() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public ArrayList<MainData> data = new ArrayList<>();



    public class MainData {
        public int id;
        public String image;
        public String personal_image;
        public String name_en;
        public String name_ar;
        public byte gender;
        public String professional_title_en;
        public String professional_title_ar;
        public String app_name_en ;
        public String app_name_ar ;
        public String city_en;
        public String city_ar;
        public float reservation_cost;
        public int category_id;
        public String reservation_type;
        public double latitude;
        public double longitude;
        public String category_name_en;
        public String category_name_ar;
        public boolean favorite;
        public int rate_count;
        public float rate_average;
        public boolean available_today;
        public ArrayList<Services> services = new ArrayList<>();
    }

    public class Services {
        public String title_en;
        public String title_ar;
    }
}
