package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInGlobal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInGlobal() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;
    public Data data = new Data();

    public class Data {
        public int id;
        public int count;
        public int visitor_id;
        public int product_id;
        public float delivery_cost;
        public String created_at;
        public String updated_at;

    }
}
