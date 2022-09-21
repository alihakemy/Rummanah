package helpers;

import dataInLists.DataInLogin;

public class UserTokenHolder {
    private DataInLogin.Tokens FaceID = new DataInLogin.Tokens();

    public DataInLogin.Tokens getData() {
        return FaceID;
    }

    public void setData(DataInLogin.Tokens data) {
        this.FaceID = data;
    }



    private static final UserTokenHolder holder = new UserTokenHolder();

    public static UserTokenHolder getInstance() {
        return new UserTokenHolder();
    }

}
