package assignment.db.com.dbassignment.utils;

import android.database.Cursor;

import java.util.Collection;

public class AppUtils {
    //Check Collection for Empty
    public static boolean isCollectionEmpty(Collection<? extends Object> collection)
    {
        if(collection == null || collection.isEmpty())
        {
            return true;
        }

        return false;
    }

    public static void closeCursor(Cursor cursor)
    {
        if (cursor != null && !cursor.isClosed())
        {
            try
            {
                cursor.close();
            }
            catch (Exception e)
            {
                //Do Nothing
                e.printStackTrace();
            }
        }
    }
}
