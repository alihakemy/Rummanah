package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInArea implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInArea() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public ArrayList<Area> data = new ArrayList<>();

    public class Area {
        public int id;
        public String title;

    }
}
