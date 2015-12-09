package co.com.indibyte.truelink.util;

import java.util.List;

/**
 * Created by CharlieMolina on 8/12/15.
 */
public final class utils {
    private  utils() {
    }

    public static boolean isTaginarray(String word, List<String> array){
        if (array!=null) {
            return array.contains(word);
        }else{
            return false;
        }
    }
}
