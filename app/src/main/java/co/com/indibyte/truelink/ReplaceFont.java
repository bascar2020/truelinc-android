package co.com.indibyte.truelink;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by user on 21/11/15.
 */
public class ReplaceFont {

    public static void replaceDefaultFont(Context context,
                                          String nameOfFontBeingReplaced,
                                          String nameOfFontInAssets){

        Typeface customFontTypeFonface = Typeface.createFromAsset(context.getAssets(), nameOfFontInAssets);
        replaceFont(nameOfFontBeingReplaced, customFontTypeFonface);


    }

    private static void replaceFont(String nameOfFontBeingReplaced, Typeface customFontTypeFonface) {
        try {
            Field myfield = Typeface.class.getDeclaredField(nameOfFontBeingReplaced);
            myfield.setAccessible(true);
            myfield.set(null, customFontTypeFonface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
