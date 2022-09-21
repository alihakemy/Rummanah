package helpers;

public class LoginHolder {
    private static final LoginHolder holder = new LoginHolder();

    private String FaceID;

    public LoginHolder() {
        FaceID = "";
    }

    public String getData() {
        return FaceID;
    }

    public void setData(String data) {
        this.FaceID = data;
    }


    public static LoginHolder getInstance() {

        return holder;
    }

}
