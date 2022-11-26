package helpers;


import android.content.Context;

import com.google.firebase.FirebaseApp;

public class MyApp extends android.app.Application {

    public  static Context context ;
    public MyApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context =this;
        try {

            //FirebaseApp.initializeApp(this);
        } catch (Exception e) {

        }

    }
}