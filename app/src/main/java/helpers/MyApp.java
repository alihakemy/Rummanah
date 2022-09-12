package helpers;


import com.google.firebase.FirebaseApp;

public class MyApp extends android.app.Application {

    public MyApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            //FirebaseApp.initializeApp(this);
        } catch (Exception e) {

        }

    }
}