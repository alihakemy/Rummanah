package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInTimeOfworks implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInTimeOfworks() {
        super();
    }


    public boolean success;
    public int code;
    public Data data = new Data();

    public class Data {
        public int id;
        public String reservation_type;
        public ArrayList<ThirtyDays> thirtydays = new ArrayList<>();

    }

    public class ThirtyDays {
        public int number;
        public String date;
        public Day day = new Day();
        public ArrayList<DataInPersonsProfile.Times_Of_Work> timesofwork = new ArrayList<>();
        public boolean avialable;
        public boolean selected = false;
        public String created_at;
        public String updated_at;
    }

    public class Day {
        public String name_en;
        public String name_ar;
    }

}
