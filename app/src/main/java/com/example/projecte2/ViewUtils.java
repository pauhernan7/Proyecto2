package com.example.projecte2;

import android.content.Context;

public class ViewUtils {
    public static int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}