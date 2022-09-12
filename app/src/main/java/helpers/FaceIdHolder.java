package helpers;

public class FaceIdHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final FaceIdHolder holder = new FaceIdHolder();

	public static FaceIdHolder getInstance() {
		return holder;
	}

}
