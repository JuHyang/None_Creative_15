package ncs.com.kaulife;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2018-01-24.
 */

public class GradeData extends SugarRecord {
    String subject;
    String grade;
    String hakgi;
    int credit = 0;
    int ranking = 0;

    public GradeData() {
    }

    public GradeData (String subject, String grade, String hakgi, int credit, int ranking) {
        this.subject = subject;
        this.grade = grade;
        this.hakgi = hakgi;
        this.credit = credit;
        this.ranking = ranking;
    }
}
