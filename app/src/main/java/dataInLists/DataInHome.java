package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInHome implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInHome() {
        super();
    }


    public boolean success;
    public boolean next_page;
    public int code;
    public String message;
    public ArrayList<HomeData> data;

    public class HomeData {
        public int id;
        public byte type;
        public String title;
        public ArrayList<HomeContent> data;

    }

    public class HomeContent implements Serializable {
        public int id;
        public byte type;
        public String title;
        public String image;
        public String content;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public float offer_percentage;
        public int category_id;
        public double rate;
        public boolean favorite;
        public String  category_name;
    }

}
