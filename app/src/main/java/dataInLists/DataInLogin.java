package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInLogin implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInLogin() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public Data data = new Data();

    public class Data {
        public int id;
        public Tokens token = new Tokens();
        public String name;
        public String phone;
        public String email;
        public String password;
        public String fcm_token;
        public String verification_code;
        public String gender;
        public String updated_at;
        public String created_at;
    }

    public static class Tokens {
        public Tokens() {
            access_token = "";
            token_type = "";
            expires_in = "";
        }

        public String access_token;
        public String token_type;
        public String expires_in;
    }
}
