package assignment.db.com.dbassignment.home;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import assignment.db.com.dbassignment.data.HomeData;
import assignment.db.com.dbassignment.database.DBManager;
import assignment.db.com.dbassignment.utils.AppUtils;

public class HomePresenterImpl implements HomePresenter {
    private final HomeView searchView;
    private Context context;

    public HomePresenterImpl(HomeView searchView)
    {
        this.searchView = searchView;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void getData(final String contactId) {
        final Handler handler = new Handler();

        searchView.showProgress();
        //Starting a worker thread to perform heavy background operations
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                final HomeData homeData = DBManager.getHomeData(context, contactId);

                //Posting on ui thread to update ui with processed data
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        searchView.hideProgress();
                        searchView.updateView(homeData);
                    }
                });
            }
        };

        thread.start();
    }

    @Override
    public void getContactIdList() {

        final Handler handler = new Handler();

        searchView.showProgress();
        //Starting a worker thread to perform heavy background operations
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {

                List<String> contactList = DBManager.getContactIdList(context);

                if(AppUtils.isCollectionEmpty(contactList)) {
                    ArrayList<HomeData> homeDataList = getDummyData();
                    DBManager.populateContact(context, homeDataList);

                    contactList = DBManager.getContactIdList(context);
                }

                final List<String> cList = contactList;

                //Posting on ui thread to update ui with processed data
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        searchView.hideProgress();
                        searchView.updateContactId(cList);
                    }
                });
            }
        };

        thread.start();
    }

    private ArrayList<HomeData> getDummyData() {
        ArrayList<HomeData> homeDataList = new ArrayList<>();

        HomeData homeData = new HomeData();
        homeData.setUserId("test_one@gmail.com");
        homeData.setStatus("1");
        homeData.setStagingId("1196");
        homeData.setContext("Gmail");
        homeData.setContactId("48f3");
        homeDataList.add(homeData);

        HomeData homeData1 = new HomeData();
        homeData1.setUserId("test_one@gmail.com");
        homeData1.setStatus("1");
        homeData1.setStagingId("f1fe");
        homeData1.setContext("Gmail");
        homeData1.setContactId("3e47");
        homeDataList.add(homeData1);

        HomeData homeData2 = new HomeData();
        homeData2.setUserId("test_two@gmail.com");
        homeData2.setStatus("0");
        homeData2.setStagingId("036e");
        homeData2.setContext("Gmail1");
        homeData2.setContactId("2cac");
        homeDataList.add(homeData2);


        return homeDataList;
    }
}
