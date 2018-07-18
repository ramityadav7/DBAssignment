package assignment.db.com.dbassignment.application;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DBApplicationModule.class})
public interface DBApplicationComponent {
    void inject(DBApplication app);

    DBApplication getDBApplication();
}
