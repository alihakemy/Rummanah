package dataInLists;

import java.util.ArrayList;

public class DataInSetting {

	public DataInSetting() {
		super();
	}


	public boolean success;
	public int code;
	public String message_en;
	public String message_ar;
	public String message;
	public DataInSetting.Data data = new DataInSetting.Data();

	public class Data {
		public int id;
		public String app_phone;
		public String message;
		public String termsandconditions_en;
		public String termsandconditions_ar;
		public String aboutapp_en;
		public String aboutapp_ar;
		public String created_at;
		public String updated_at;

	}
}
