package helpers;

public class UserMobileHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final UserMobileHolder holder = new UserMobileHolder();

	public static UserMobileHolder getInstance() {
		return holder;
	}

}
