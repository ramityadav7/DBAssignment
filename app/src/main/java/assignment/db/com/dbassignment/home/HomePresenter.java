package assignment.db.com.dbassignment.home;

import android.content.Context;

public interface HomePresenter {
    void getData(String contactId);
    void getContactIdList();
    void setContext(Context context);
}
