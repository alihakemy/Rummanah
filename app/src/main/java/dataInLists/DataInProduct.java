package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInProduct implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInProduct() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;

    public Data data = new Data();

    public class Data {
        public Product product;
        public ArrayList<Related> related;
        public double rate;
        public int rate_count;
        public Rates rates;

    }

    public class Product {
        public int id;
        public int count;
        public String title;
        public String description;
        public byte offer;
        public ArrayList<String> images;
        public double final_price;
        public double price_before_offer;
        public double offer_percentage;
        public int category_id;
        public double rate;
        public int order_id;
        public int rate_count;
        public String image;
        public String category_name;
        public boolean favorite;
        public ArrayList<Options> options;
        public ArrayList<Rates> rates;
    }

    public class Rates {
        public int id;
        public int user_id;
        public String text;
        public double rate;
        public int admin_approval;
        public int current_page;
        public int order_id;
        public String created_at;
        public String updated_at;
        public User user;
        public ArrayList<Rates> data;

    }

    public class User {
        public int id;
        public String name;
        public String phone;
        public String email;
        public int main_address_id;
    }

    public class Options implements Serializable {
        public int id;
        public int option_id;
        public String value;
        public String key;
    }

    public class Related {
        public int id;
        public String title;
        public String image;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public float offer_percentage;
        public int category_id;
        public double rate;
        public boolean favorite;
        public String category_name;
    }

}
