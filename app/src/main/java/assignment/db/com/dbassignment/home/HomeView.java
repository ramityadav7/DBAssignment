package assignment.db.com.dbassignment.home;

import java.util.List;

import assignment.db.com.dbassignment.data.HomeData;

public interface HomeView {
    void updateView(HomeData homeData);
    void showProgress();
    void hideProgress();
    void updateContactId(List<String> contactID);
}
