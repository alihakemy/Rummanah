package dataInLists;

import java.io.Serializable;

public class DataInCheckUser implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataInCheckUser() {
		super();
	}


	public boolean success;
	public int code;
	public String message;
	public String message_en;
	public String message_ar;
	public boolean phone;
	public boolean email;
	public Data data = new Data();

	public class Data {
		public int id;
		public DataInLogin.Tokens token = new DataInLogin.Tokens();
		public String name;
		public String phone;
		public String email;
		public String phone_verified_at;
		public String password;
		public String fcm_token;
		public String verification_code;
		public String date_of_birth;
		public byte verified;
		public byte seen;
		public byte active;
		public int main_address_id;
		public String remember_token;
		public String updated_at;
		public String created_at;
	}

}
