package dataInLists;

import java.io.Serializable;

public class DataInUserProfile implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataInUserProfile() {
		super();
	}


	public boolean success;
	public int code;
	public String message_en;
	public String message_ar;
	public Data data = new Data();

	public class Data {
		public int id;
		public String user_name;
		public String phone;
		public String dob;
		public String email;
		public float wallet;
		public byte gender;
	}

}
