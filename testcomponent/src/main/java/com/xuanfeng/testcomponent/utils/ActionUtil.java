package com.xuanfeng.testcomponent.utils;

public class ActionUtil {
    private ActionUtil() {
    }

    public static String getActionName(int code) {
        switch (code) {
            case 0:
                return "ACTION_DOWN";
            case 1:
                return "ACTION_UP";

            case 2:
                return "ACTION_MOVE";

            case 3:
                return "ACTION_CANCEL";

            case 4:
                return "ACTION_OUTSIDE";

            case 5:
                return "ACTION_POINTER_DOWN";
            case 6:
                return "ACTION_POINTER_UP";
            default:
                break;

        }
        return "其他action";
    }
}
