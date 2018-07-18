package assignment.db.com.dbassignment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import assignment.db.com.dbassignment.application.DBApplication;
import assignment.db.com.dbassignment.application.DBApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpComponent(DBApplication.get(this).component());
    }

    protected abstract void setUpComponent(DBApplicationComponent dbApplicationComponent);
}
