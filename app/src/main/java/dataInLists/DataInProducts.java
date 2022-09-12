package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInProducts implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInProducts() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public MainData data;


    public class MainData {
        public ArrayList<SubCats> sub_categories = new ArrayList<>();
        public Products products;
    }

    public class SubCats {
        public int id;
        public String title;
        public byte selected;
    }

    public class Products {
        public int current_page;
        public ArrayList<ProductDetails> data = new ArrayList<>();
        public String first_page_url;
        public String from;
        public String next_page_url;
        public String path;
        public String prev_page_url;
        public String to;
        public int per_page;

    }

    public class ProductDetails {
        public int id;
        public String title;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public float offer_percentage;
        public int category_id;
        public double rate;
        public String category_name;
        public String image;
        public boolean favorite;
    }
}
