package assignment.db.com.dbassignment.home;

import assignment.db.com.dbassignment.application.ActivityScope;
import assignment.db.com.dbassignment.application.DBApplicationComponent;
import dagger.Component;

@ActivityScope
@Component(
        dependencies = DBApplicationComponent.class,
        modules = {
                HomeModule.class
        }
)
public interface HomeComponent {

    void inject(HomeActivity activity);
}
