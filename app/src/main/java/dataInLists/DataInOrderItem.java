package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInOrderItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInOrderItem() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public Order data = new Order();

    public class Order {
        public int id;
        public String order_number;
        public byte status;
        public byte payment_method;
        public String date;
        public float subtotal_price;
        public float delivery_cost;
        public float total_price;
        public int products_count;
        public DataInAddress.Address address ;
        public ArrayList<DataInProduct.Product> products = new ArrayList<>() ;
    }


}
