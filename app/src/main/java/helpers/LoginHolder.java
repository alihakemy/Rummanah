package helpers;

public class LoginHolder {
    private String FaceID;

    public String getData() {
        return FaceID;
    }

    public void setData(String data) {
        this.FaceID = data;
    }

    private static final LoginHolder holder = new LoginHolder();

    public static LoginHolder getInstance() {
        return holder;
    }

}
