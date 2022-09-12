package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInProductsFav implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInProductsFav() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public ArrayList<ProductDetails> data = new ArrayList<>();


    public class ProductDetails {
        public int id;
        public String title;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public float offer_percentage;
        public int category_id;
        public String category_name;
        public String image;
        public boolean favorite;
    }
}
