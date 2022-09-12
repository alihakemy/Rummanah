package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInOrders implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInOrders() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public ArrayList<Orders> data = new ArrayList<>();

    public class Orders {
        public int id;
        public String order_number;
        public byte status;
        public String date;
        public float total_price;
        public int count;

    }
}
