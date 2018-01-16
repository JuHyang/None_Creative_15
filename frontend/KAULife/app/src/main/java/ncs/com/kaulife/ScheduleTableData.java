package ncs.com.kaulife;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2018-01-15.
 */

public class ScheduleTableData {
    String subject;
    String professor;
    String room;

    public ScheduleTableData () {
        this.subject = "";
        this.professor = "";
        this.room = "";
    }

    public ScheduleTableData (String subject) {
        this.subject = subject;
        this.professor = "";
        this.room = "";
    }
}
