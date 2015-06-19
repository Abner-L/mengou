package com.baasplus.mengousms.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by abner on 15-6-14.
 */
public class Inbox {
    private List<Sms>smses = Collections.synchronizedList(new ArrayList<Sms>());

    public void add(Sms sms) {
        if (!exist(sms)) {
            smses.add(sms);
        }
    }

    private boolean exist(Sms sms){
        for (Sms s: smses){
            if (s.getId() == sms.getId()){
                return true;
            }
        }
        return false;
    }
    public List<Sms> getSmses() {
        return smses;
    }
}
