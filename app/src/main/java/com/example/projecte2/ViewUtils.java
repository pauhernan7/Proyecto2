package com.example.projecte2;

import android.content.Context;

public class ViewUtils {
    public static int dpToPx(Context context, float dp) {
        // Ahora acepta float como parámetro y devuelve int
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}