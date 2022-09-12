package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInNoti implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DataInNoti() {
		super();
	}


	public boolean success;
	public int code;
	public String message_en;
	public String message_ar;
	public String message;
	public ArrayList<Notification> data = new ArrayList<>();

	public class Notification {
		public int id;
		public String image;
		public String title;
		public String body;
		public String created_at;

	}
}
