package assignment.db.com.dbassignment.home;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
    private final HomeView view;

    public HomeModule(HomeView view)
    {
        this.view = view;
    }

    @Provides
    HomeView provideHomeView() {
        return view;
    }

    @Provides
    HomePresenter provideHomePresenter(HomeView view){
        return new HomePresenterImpl(view);
    }
}
