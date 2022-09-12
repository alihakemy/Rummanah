package helpers;

public class UserEmailHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final UserEmailHolder holder = new UserEmailHolder();

	public static UserEmailHolder getInstance() {
		return holder;
	}

}
