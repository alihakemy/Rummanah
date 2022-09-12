package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataInOffers implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInOffers() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public ArrayList<List<OfferContent>> data;


    public class OfferContent {
        public int id;
        public byte type;
        public byte size;
        public String image;
        public String target_id;
        public int sort;
        public String created_at;
        public String updated_at;
    }

}
