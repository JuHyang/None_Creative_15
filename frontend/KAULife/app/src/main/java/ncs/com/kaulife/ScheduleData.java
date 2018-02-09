package ncs.com.kaulife;

import android.graphics.Color;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2018-01-15.
 */

public class ScheduleData extends SugarRecord{
    String subject;
    int grade;
    String category;
    int credit;
    String professor;
    String major;
    String time;
    String room;
    String target;
    int label;
    String timeNum;

    public ScheduleData () {

    }

    public ScheduleData (String subject, int grade, String category, int credit, String professor,
                         String major, String time, String room, String target, int label, String timeNum) {
        this.subject = subject;
        this.grade = grade;
        this.category = category;
        this.credit = credit;
        this.professor = professor;
        this.major = major;
        this.time = time;
        this.room = room;
        this.target = target;
        this.label = label;
        this.timeNum = timeNum;
    }
}
