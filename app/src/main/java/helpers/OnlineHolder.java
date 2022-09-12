package helpers;

public class OnlineHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final OnlineHolder holder = new OnlineHolder();

	public static OnlineHolder getInstance() {
		return holder;
	}

}
