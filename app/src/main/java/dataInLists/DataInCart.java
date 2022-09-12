package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInCart implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInCart() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public MainData data = new MainData();

    public class MainData {
        public float subtotal_price;
        public int count;
        public String min_order;
        public ArrayList<Carts> cart = new ArrayList<>();
    }

    public class Carts {
        public int id;
        public int count;
        public String title;
        public String image;
        public float final_price;
        public float price_before_offer;
        public boolean favorite;
    }
}
