package assignment.db.com.dbassignment.application;

import android.app.Application;
import android.content.Context;

public class DBApplication extends Application{

    private DBApplicationComponent dbApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeAppComponentGraph();
    }

    private void initializeAppComponentGraph() {
        dbApplicationComponent = DaggerDBApplicationComponent.builder().dBApplicationModule(new DBApplicationModule(this)).build();
        dbApplicationComponent.inject(this);
    }

    public static DBApplication get(Context context) {
        return (DBApplication) context.getApplicationContext();
    }

    public DBApplicationComponent component() {
        return dbApplicationComponent;
    }
}
