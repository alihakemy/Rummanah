package helpers;

public class OpenHolder {
	private int FaceID;

	public int getData() {
		return FaceID;
	}

	public void setData(int data) {
		this.FaceID = data;
	}

	private static final OpenHolder holder = new OpenHolder();

	public static OpenHolder getInstance() {
		return holder;
	}

}
