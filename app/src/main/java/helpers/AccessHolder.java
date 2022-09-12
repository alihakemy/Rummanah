package helpers;

public class AccessHolder {
	private int FaceID;

	public int getData() {
		return FaceID;
	}

	public void setData(int data) {
		this.FaceID = data;
	}

	private static final AccessHolder holder = new AccessHolder();

	public static AccessHolder getInstance() {
		return holder;
	}

}
