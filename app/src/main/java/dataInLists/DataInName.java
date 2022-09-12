package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInName implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInName() {
        super();
    }


    public boolean success;
    public int code;
    public ArrayList<MainData> data = new ArrayList<>();

    public class MainData {
        public MainData(int i, String title, String img) {
            this.id = i;
            this.title = title;
            this.image = img;
        }

        public int id;
        public String title;
        public String image;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public float offer_percentage;
        public int category_id;
        public String category_name;
        public boolean favorite;
    }
}
