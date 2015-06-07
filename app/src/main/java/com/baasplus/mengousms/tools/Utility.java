package com.baasplus.mengousms.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by abner-l on 15/6/7.
 */
public class Utility {

    public static <E extends Object> List<E> setToList(Set<E> set) {
        List<E> list = new ArrayList<>();
        for (E entry : set) {
            list.add(entry);
        }
        return list;
    }

}
