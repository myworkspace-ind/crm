package mks.myworkspace.crm.common;

public class CommonUtil {
    // Checks if an array is not null, not empty, and has non-null elements
    public static boolean isNNNE(Object[] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        for (Object obj : data) {
            if (obj != null) {
                return true;
            }
        }
        return false;
    }
}
