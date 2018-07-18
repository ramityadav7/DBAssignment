package assignment.db.com.dbassignment.application;

import dagger.Module;
import dagger.Provides;

@Module
public class DBApplicationModule {
    private final DBApplication dbApplicaton;

    public DBApplicationModule(DBApplication dbApplication)
    {
        this.dbApplicaton = dbApplication;
    }

    @Provides
    public DBApplication provideApplication(){
        return dbApplicaton;
    }
}
