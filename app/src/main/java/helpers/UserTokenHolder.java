package helpers;

import dataInLists.DataInLogin;

public class UserTokenHolder {
    private DataInLogin.Tokens FaceID;

    public DataInLogin.Tokens getData() {
        return FaceID;
    }

    public void setData(DataInLogin.Tokens data) {
        this.FaceID = data;
    }

    public void setData() {
        this.FaceID = null;
    }

    private static final UserTokenHolder holder = new UserTokenHolder();

    public static UserTokenHolder getInstance() {
        return new UserTokenHolder();
    }

}
