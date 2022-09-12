package helpers;

public class ProductHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final ProductHolder holder = new ProductHolder();

	public static ProductHolder getInstance() {
		return holder;
	}

}
