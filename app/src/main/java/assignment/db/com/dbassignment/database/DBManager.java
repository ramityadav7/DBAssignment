package assignment.db.com.dbassignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import assignment.db.com.dbassignment.data.AccountsData;
import assignment.db.com.dbassignment.data.ContactData;
import assignment.db.com.dbassignment.data.HomeData;
import assignment.db.com.dbassignment.utils.AppUtils;

public class DBManager {

    public static List<String> getContactIdList(Context context) {
        SQLiteDatabase sqliteDatabase;
        ApplicationDatabase applicationDatabase;
        Cursor cursor = null;
        List<String> contactIdList = null;

        try {
            //Get Readable Database
            applicationDatabase = ApplicationDatabase.getInstance(context);
            sqliteDatabase = applicationDatabase.openDatabase();

            cursor = sqliteDatabase.query(ApplicationDatabase.TABLE_NAME_CONTACTS , null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                contactIdList = new ArrayList<>();

                do {
                    String contactId = cursor.getString(cursor.getColumnIndex(ApplicationDatabase.COLUMN_CONTACT_ID));
                    contactIdList.add(contactId);
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception exception) {
        }
        finally {
            AppUtils.closeCursor(cursor);
        }

        return contactIdList;
    }

    public static HomeData getHomeData(Context context, String contactId) {
        HomeData homeData = new HomeData();

        ContactData contactData = getContactData(context, contactId);
        String extensionContext = getContextFromExtensions(context, contactData.getId());
        AccountsData accountsData = getAccountsData(context, extensionContext);

        homeData.setStagingId(contactData.getStagingId());
        homeData.setContext(extensionContext);
        homeData.setStatus(accountsData.getStatus());
        homeData.setUserId(accountsData.getUserId());

        return homeData;
    }

    private static ContactData getContactData(Context context, String contactId) {
        SQLiteDatabase sqliteDatabase;
        ApplicationDatabase applicationDatabase;
        Cursor cursor = null;
        ContactData contactData = null;

        try {
            //Get Readable Database
            applicationDatabase = ApplicationDatabase.getInstance(context);
            sqliteDatabase = applicationDatabase.openDatabase();

            cursor = sqliteDatabase.query(ApplicationDatabase.TABLE_NAME_CONTACTS , null, ApplicationDatabase.COLUMN_CONTACT_ID+" =?", new String[] {contactId}, null, null, null);

            if (cursor != null) {
                contactData = new ContactData();
                if(cursor.moveToFirst()) {
                    contactData.setId(cursor.getString(cursor.getColumnIndex(ApplicationDatabase.COLUMN_NAME_ID)));
                    contactData.setStagingId(cursor.getString(cursor.getColumnIndex(ApplicationDatabase.COLUMN_STAGING_ID)));
                }
            }
        }
        catch (Exception exception) {
        }
        finally {
            AppUtils.closeCursor(cursor);
        }

        return contactData;
    }

    private static String getContextFromExtensions(Context context, String contactId) {
        SQLiteDatabase sqliteDatabase;
        ApplicationDatabase applicationDatabase;
        Cursor cursor = null;
        String extensionContext = null;

        try {
            //Get Readable Database
            applicationDatabase = ApplicationDatabase.getInstance(context);
            sqliteDatabase = applicationDatabase.openDatabase();

            cursor = sqliteDatabase.query(ApplicationDatabase.TABLE_NAME_EXTENSIONS , null, ApplicationDatabase.COLUMN_PHONE_CONTACT_ID+" =?", new String[] {contactId}, null, null, null);

            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    extensionContext = cursor.getString(cursor.getColumnIndex(ApplicationDatabase.COLUMN_CONTEXT));
                }
            }
        }
        catch (Exception exception) {
        }
        finally {
            AppUtils.closeCursor(cursor);
        }

        return extensionContext;
    }

    private static AccountsData getAccountsData(Context context, String contextAccount) {
        SQLiteDatabase sqliteDatabase;
        ApplicationDatabase applicationDatabase;
        Cursor cursor = null;
        AccountsData accountsData = null;

        try {
            //Get Readable Database
            applicationDatabase = ApplicationDatabase.getInstance(context);
            sqliteDatabase = applicationDatabase.openDatabase();

            cursor = sqliteDatabase.query(ApplicationDatabase.TABLE_NAME_ACCOUNTS , null, ApplicationDatabase.COLUMN_CONTEXT_ACCOUNTS+" =?", new String[] {contextAccount}, null, null, null);

            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    accountsData = new AccountsData();
                    accountsData.setStatus(cursor.getString(cursor.getColumnIndex(ApplicationDatabase.COLUMN_STATUS)));
                    accountsData.setUserId(cursor.getString(cursor.getColumnIndex(ApplicationDatabase.COLUMN_USER_ID)));
                }
            }
        }
        catch (Exception exception) {
        }
        finally {
            AppUtils.closeCursor(cursor);
        }

        return accountsData;
    }

    public static long populateContact(Context context, ArrayList<HomeData> homeDataList) {
        SQLiteDatabase sqliteDatabase = null;
        ApplicationDatabase applicationDatabase = null;
        long result = 0;

        try
        {
            //Get Readable Database
            applicationDatabase = ApplicationDatabase.getInstance(context);
            sqliteDatabase = applicationDatabase.openDatabase();

            //Begin Transaction
            beginTransaction(sqliteDatabase);

            if( !AppUtils.isCollectionEmpty(homeDataList) ) {
                ContentValues contactContentValues;

                for (HomeData homeData : homeDataList) {
                    contactContentValues = new ContentValues();

                    contactContentValues.put(ApplicationDatabase.COLUMN_CONTACT_ID, homeData.getContactId());
                    contactContentValues.put(ApplicationDatabase.COLUMN_STAGING_ID, homeData.getStagingId());

                    result = sqliteDatabase.insert(ApplicationDatabase.TABLE_NAME_CONTACTS, null, contactContentValues);
                    populateExtension(context, homeData.getContext(), String.valueOf(result));
                    AccountsData accountsData = new AccountsData();
                    accountsData.setStatus(homeData.getStatus());
                    accountsData.setUserId(homeData.getUserId());
                    populateAccount(context, accountsData, homeData.getContext());
                }
            }
            //Set Transaction Successful
            setTransactionSuccessful(sqliteDatabase);
        }
        catch(Exception exception) {
        }
        finally {
            //End Transaction
            endTransaction(sqliteDatabase);
            applicationDatabase.closeDatabase();
        }

        return result;
    }

    private static long populateExtension(Context context, String extensionContext, String id) {
        SQLiteDatabase sqliteDatabase = null;
        ApplicationDatabase applicationDatabase = null;
        long result = 0;

        try
        {
            //Get Readable Database
            applicationDatabase = ApplicationDatabase.getInstance(context);
            sqliteDatabase = applicationDatabase.openDatabase();

            ContentValues extensionContentValues;

            extensionContentValues = new ContentValues();

            extensionContentValues.put(ApplicationDatabase.COLUMN_CONTEXT, extensionContext);
            extensionContentValues.put(ApplicationDatabase.COLUMN_PHONE_CONTACT_ID, id);

            result = sqliteDatabase.insert(ApplicationDatabase.TABLE_NAME_EXTENSIONS, null, extensionContentValues);
        }
        catch(Exception exception) {
        }
        finally {
        }

        return result;

    }

    private static long populateAccount(Context context, AccountsData accountsData, String accountContext) {
        SQLiteDatabase sqliteDatabase = null;
        ApplicationDatabase applicationDatabase = null;
        long result = 0;

        try
        {
            //Get Readable Database
            applicationDatabase = ApplicationDatabase.getInstance(context);
            sqliteDatabase = applicationDatabase.openDatabase();

            ContentValues accountContentValues;

            accountContentValues = new ContentValues();

            accountContentValues.put(ApplicationDatabase.COLUMN_STATUS, accountsData.getStatus());
            accountContentValues.put(ApplicationDatabase.COLUMN_USER_ID, accountsData.getUserId());
            accountContentValues.put(ApplicationDatabase.COLUMN_CONTEXT_ACCOUNTS, accountContext);

            result = sqliteDatabase.update(ApplicationDatabase.TABLE_NAME_ACCOUNTS, accountContentValues, ApplicationDatabase.COLUMN_CONTEXT_ACCOUNTS+" =?", new String[] {accountContext});

            if(result == 0) {
                result = sqliteDatabase.insert(ApplicationDatabase.TABLE_NAME_ACCOUNTS, null, accountContentValues);
            }
        }
        catch(Exception exception) {
        }
        finally {
        }

        return result;
    }

    /**
     * Sql Transaction Methods
     */
    private static void beginTransaction(SQLiteDatabase sqliteDatabase)
    {
        try
        {
            if (sqliteDatabase != null)
            {
                sqliteDatabase.beginTransactionNonExclusive();
            }
        }
        catch (Exception exception)
        {
            //Consume
        }
    }

    private static void setTransactionSuccessful(SQLiteDatabase sqliteDatabase)
    {
        try
        {
            if (sqliteDatabase != null)
            {
                sqliteDatabase.setTransactionSuccessful();
            }
        }
        catch (Exception exception)
        {
            //Consume
        }
    }

    private static void endTransaction(SQLiteDatabase sqliteDatabase)
    {
        try
        {
            if (sqliteDatabase != null)
            {
                sqliteDatabase.endTransaction();
            }
        }
        catch (Exception exception)
        {
            //Consume
        }
    }
}
