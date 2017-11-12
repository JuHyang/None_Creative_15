package ncs.com.kaulife;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2017-11-12.
 */

public class LmsData extends SugarRecord<LmsData> {
    String subject;
    String time;
    String content;

    public LmsData () {

    }

    public LmsData (String subject, String time, String content) {
        this.subject = subject;
        this.time = time;
        this.content = content;
    }
}
