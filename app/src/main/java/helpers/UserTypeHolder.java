package helpers;

public class UserTypeHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final UserTypeHolder holder = new UserTypeHolder();

	public static UserTypeHolder getInstance() {
		return holder;
	}

}
