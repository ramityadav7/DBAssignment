package assignment.db.com.dbassignment.utils;

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
}
