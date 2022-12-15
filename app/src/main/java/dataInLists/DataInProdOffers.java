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
    public ArrayList<DataInProducts.ProductDetails> data;




}
