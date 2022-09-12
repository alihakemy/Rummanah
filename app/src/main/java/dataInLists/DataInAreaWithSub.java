package dataInLists;

import java.util.ArrayList;

public class DataInAreaWithSub {

	public DataInAreaWithSub() {
		super();
	}

	public DataInAreaWithSub(String name , String photo , int id) {
		this.name = name ;
		this.photo = photo ;
		this.id = id ;
	}

	public String name;
	public String photo;
	public String code;
	public int id;
	public ArrayList<Childs> child;

	public class Childs {
		public String name;
		public int id;
	}
}
