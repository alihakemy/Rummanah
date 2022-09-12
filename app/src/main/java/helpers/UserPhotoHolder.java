package helpers;

public class UserPhotoHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final UserPhotoHolder holder = new UserPhotoHolder();

	public static UserPhotoHolder getInstance() {
		return holder;
	}

}
