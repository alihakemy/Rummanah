package helpers;

public class UsernameHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final UsernameHolder holder = new UsernameHolder();

	public static UsernameHolder getInstance() {
		return holder;
	}

}
