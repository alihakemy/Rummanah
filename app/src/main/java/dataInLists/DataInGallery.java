package dataInLists;

public class DataInGallery {

	public DataInGallery() {
		super();
	}
	public DataInGallery(String x , byte y, String z) {
		super();
		link = x ;
		t = y;
		thumb = z;
	}

	public DataInGallery(String x , byte y) {
		super();
		link = x ;
		t = y;
	}


	public String link;
	public String thumb;
	public byte t;
}
