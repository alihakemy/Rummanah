package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataInProdOffers implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInProdOffers() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public ArrayList<OfferContent> data;


    public class OfferContent {
        public int id;
        public String title;
        public String image;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public float offer_percentage;
        public int category_id;
        public boolean favorite;
        public String  category_name;
    }

}
