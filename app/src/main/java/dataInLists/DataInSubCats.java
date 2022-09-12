package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInSubCats implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInSubCats() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public Brands data ;

    public class Brands {
        public String category_name;
        public ArrayList<SubCats> sub_categories = new ArrayList<>();
    }

    public class SubCats {
        public SubCats(int ID , String Img , String Title){
            this.id = ID ;
            this.image = Img ;
            this.title = Title ;
        }
        public int id;
        public String image;
        public String title;
    }
}
