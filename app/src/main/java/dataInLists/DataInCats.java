package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInCats implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInCats() {
        super();
    }


    public boolean success;
    public int code;
    public ArrayList<MainData> data = new ArrayList<>();

    public class MainData {
        public int id;
        public String title;
        public String image;
        public boolean selected = false ;
    }

}
