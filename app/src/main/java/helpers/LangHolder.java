package helpers;

public class LangHolder {
	private String FaceID;

	public String getData() {
		return FaceID;
	}

	public void setData(String data) {
		this.FaceID = data;
	}

	private static final LangHolder holder = new LangHolder();

	public static LangHolder getInstance() {
		return holder;
	}

}
