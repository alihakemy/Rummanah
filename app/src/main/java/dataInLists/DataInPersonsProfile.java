package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInPersonsProfile implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInPersonsProfile() {
        super();
    }


    public boolean success;
    public int code;
    public Data data = new Data();

    public class Data {
        public int id;
        public String personal_image;
        public String app_name_en ;
        public String app_name_ar ;
        public byte gender ;
        public String professional_title_en ;
        public String professional_title_ar ;
        public String city_en ;
        public String city_ar ;
        public String address_en ;
        public String address_ar ;
        public float reservation_cost ;
        public String reservation_type ;
        public String about_en ;
        public String about_ar ;
        public int category_id ;
        public boolean favorite ;
        public String vector ;
        public int rate_count ;
        public float rate_average ;
        public boolean available_today ;
        public ArrayList<Images> images = new ArrayList<>() ;
        public ArrayList<Rates> rates = new ArrayList<>() ;
        public ArrayList<DataInPersons.Services> services = new ArrayList<>() ;
        public ArrayList<Times_Of_Work> times_of_work = new ArrayList<>() ;
    }

    public class Images implements Serializable {
        public int id;
        public int doctor_lawyer_id;
        public String image;
        public String created_at;
        public String updated_at;
    }

    public class Rates {
        public int id;
        public int user_id;
        public int doctor_lawyer_id;
        public int reservation_id;
        public int rate;
        public int admin_approval;
        public String user_name;
        public String text;
        public String created_at;
        public String updated_at;
    }
    public class Times_Of_Work {
        public int id;
        public int day;
        public int holiday;
        public String from;
        public String to;
        public String date;
        public int doctor_lawyer_id;
        public int count;
        public boolean avialable;
        public String created_at;
        public String updated_at;
    }
}
