package assignment.db.com.dbassignment.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import assignment.db.com.dbassignment.BaseActivity;
import assignment.db.com.dbassignment.R;
import assignment.db.com.dbassignment.application.DBApplication;
import assignment.db.com.dbassignment.application.DBApplicationComponent;
import assignment.db.com.dbassignment.data.HomeData;
import assignment.db.com.dbassignment.utils.AppUtils;

public class HomeActivity extends BaseActivity implements HomeView, AdapterView.OnItemSelectedListener{

    @Inject
    HomePresenter homePresenter;

    @Inject
    DBApplication dbApplication;


    private Spinner spinnerContactId;
    private LinearLayout linearLayoutHeader;
    private LinearLayout linearLayoutContent;
    private TextView textViewStagingId, textViewContext, textViewStatus, textViewUserId;
    private ProgressBar progressBar;

    private List<String> contactID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();
        getContactIdList();
    }

    private void initializeView() {
        spinnerContactId = findViewById(R.id.spinnerContactId);
        linearLayoutHeader = findViewById(R.id.linearLayoutHeader);
        linearLayoutContent = findViewById(R.id.linearLayoutContent);
        textViewStagingId = findViewById(R.id.textViewStagingId);
        textViewContext = findViewById(R.id.textViewContext);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewUserId = findViewById(R.id.textViewUserId);
        progressBar = findViewById(R.id.progressBar);

        spinnerContactId.setOnItemSelectedListener(this);

        homePresenter.setContext(dbApplication.getApplicationContext());
    }

    private void getContactIdList() {
        homePresenter.getContactIdList();
    }

    @Override
    public void updateContactId(List<String> contactID) {
        if(!AppUtils.isCollectionEmpty(contactID)) {
            this.contactID = new ArrayList<>();
            this.contactID.add("Select");
            this.contactID.addAll(contactID);
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, this.contactID);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerContactId.setAdapter(aa);
        }
    }

    @Override
    public void updateView(HomeData homeData) {
        if(homeData != null) {
            textViewUserId.setText(homeData.getUserId());
            textViewStatus.setText(homeData.getStatus());
            textViewContext.setText(homeData.getContext());
            textViewStagingId.setText(homeData.getStagingId());
        }
    }

    public void resetView() {
        textViewUserId.setText("");
        textViewStatus.setText("");
        textViewContext.setText("");
        textViewStagingId.setText("");
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutHeader.setVisibility(View.GONE);
        linearLayoutContent.setVisibility(View.GONE);
        spinnerContactId.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        linearLayoutHeader.setVisibility(View.VISIBLE);
        linearLayoutContent.setVisibility(View.VISIBLE);
        spinnerContactId.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setUpComponent(DBApplicationComponent dbApplicationComponent) {
        DaggerHomeComponent.builder()
                .dBApplicationComponent(dbApplicationComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {
            homePresenter.getData(contactID.get(position));
        } else {
            resetView();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
