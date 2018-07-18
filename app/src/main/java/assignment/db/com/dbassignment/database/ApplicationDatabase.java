package assignment.db.com.dbassignment.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class ApplicationDatabase {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "application.db";

    private Context context;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase sqliteDatabase;
    private static ApplicationDatabase applicationDatabase;
    private static SQLiteOpenHelper sqliteOpenHelper;

    //Table Contacts Table
    public static final String TABLE_NAME_CONTACTS = "contacts";

    //Table Contacts Columns
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_CONTACT_ID = "contactId";
    public static final String COLUMN_STAGING_ID = "stagingId";

    //Create Table Contacts SQL
    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS "  + TABLE_NAME_CONTACTS
            + "("
            + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_CONTACT_ID + " TEXT NOT NULL, "
            + COLUMN_STAGING_ID + " TEXT NOT NULL"
            + ")";

    //Table Extensions Table
    public static final String TABLE_NAME_EXTENSIONS = "extensions";

    //Table Extensions Columns
    public static final String COLUMN_CONTEXT = "context";
    public static final String COLUMN_PHONE_CONTACT_ID = "phoneContactId";

    //Create Table Extensions SQL
    private static final String CREATE_TABLE_EXTENSIONS = "CREATE TABLE IF NOT EXISTS "  + TABLE_NAME_EXTENSIONS
            + "("
            + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_CONTEXT + " TEXT NOT NULL, "
            + COLUMN_PHONE_CONTACT_ID + " TEXT NOT NULL"
            + ")";

    //Table Accounts Table
    public static final String TABLE_NAME_ACCOUNTS = "accounts";

    //Table Accounts Columns
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_CONTEXT_ACCOUNTS = "context";

    //Create Table Accounts SQL
    private static final String CREATE_TABLE_ACCOUNTS = "CREATE TABLE IF NOT EXISTS "  + TABLE_NAME_ACCOUNTS
            + "("
            + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL, "
            + COLUMN_USER_ID + " TEXT NOT NULL, "
            + COLUMN_CONTEXT_ACCOUNTS + " TEXT NOT NULL"
            + ")";

    //Constructor
    private ApplicationDatabase()
    {
    }

    /**
     * Singleton SqLite Object Approach
     * <p>Initialize Instance, Singleton Factory Method. Call this method from once to initialize the "CwayDatabase"
     * <br>and "SqliteOpenHelper" instance throughout the application. If you subclass the Application Class, you can call
     * <br>it from there.
     * @param context
     */
    private static synchronized void initializeInstance(Context context)
    {
        if (applicationDatabase == null)
        {
            applicationDatabase = new ApplicationDatabase();
            sqliteOpenHelper = new OpenHelper(context);
        }
    }

    /**
     * Synchronized method to get the Singleton instance of TestDatabase
     * @return databaseInstance
     */
    public static synchronized ApplicationDatabase getInstance(Context context)
    {
        if (applicationDatabase == null)
        {
            initializeInstance(context);
        }

        return applicationDatabase;
    }

    /**
     * Open Database, return the SqliteDatabase Object.
     * <p>This method increments the counter to track how many threads are accessing the database.
     *
     * @return sqliteDatabase - write database object
     */

    public synchronized SQLiteDatabase openDatabase()
    {
        if(mOpenCounter.incrementAndGet() == 1)
        {
            // Opening new database
            sqliteDatabase = sqliteOpenHelper.getWritableDatabase();

            //Enable Write Ahead Logging
            sqliteDatabase.enableWriteAheadLogging();
        }

        return sqliteDatabase;
    }

    /**
     * CLose Database,
     * <p>This method checks If any Thread is Accessing the Database. If no thread is accessing the database then it
     * <br>close the database.
     */
    public synchronized void closeDatabase()
    {
        if(mOpenCounter.decrementAndGet() == 0)
        {
            if(sqliteDatabase != null )
            {
                // Closing database
                sqliteDatabase.close();
            }
        }
    }

    //OpenHelper
    private static class OpenHelper extends SQLiteOpenHelper
    {
        OpenHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onOpen(SQLiteDatabase sqLiteDatabase)
        {
            super.onOpen(sqLiteDatabase);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase)
        {
            //Log.e(TAG, "Table created");
            sqLiteDatabase.execSQL(CREATE_TABLE_CONTACTS);
            sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNTS);
            sqLiteDatabase.execSQL(CREATE_TABLE_EXTENSIONS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }
}
