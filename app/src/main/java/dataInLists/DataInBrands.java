package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataInBrands implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInBrands() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public ArrayList<BrandContent> data;


    public class BrandContent {
        public int id;
        public String image;
        public String title;
    }

}
