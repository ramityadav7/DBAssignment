package assignment.db.com.dbassignment.home;

public class HomePresenterImpl implements HomePresenter {
    private final HomeView searchView;

    public HomePresenterImpl(HomeView searchView)
    {
        this.searchView = searchView;
    }

    @Override
    public void getData(String contactId) {

    }

    @Override
    public void getContactIdList() {

    }
}
