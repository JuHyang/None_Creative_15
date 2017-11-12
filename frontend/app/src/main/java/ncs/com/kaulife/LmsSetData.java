package ncs.com.kaulife;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2017-11-12.
 */

public class LmsSetData extends SugarRecord<LmsSetData> {
    boolean status;

    public LmsSetData () {

    }

    public LmsSetData (boolean status) {
        this.status = status;
    }
}
