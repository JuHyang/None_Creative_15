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

    public void ChangeTimeForm () {
        if (this.time.equals("")) {
            this.timeNum = "";
            return;
        }
        int index = 0;
        String result = "";

        String[] time_arr = this.time.split("/");

        for (int i = 0; i < time_arr.length; i ++) {
            if (i != 0) {
                result += "/";
            }
            String temp_time = time_arr[i];
            if (temp_time.contains("월")) {
                index = 1;
            } else if (temp_time.contains("화")) {
                index = 2;
            } else if (temp_time.contains("수")) {
                index = 3;
            } else if (temp_time.contains("목")) {
                index = 4;
            } else if (temp_time.contains("금")) {
                index = 5;
            }

            temp_time = temp_time.substring(2);

            String[] timeNum_arr = temp_time.split("∼");
            int hour;
            String[] timeHour_arr;
            timeHour_arr = timeNum_arr[0].split(":");
            hour = Integer.parseInt(timeHour_arr[0]);
            int timeNum_1 = ((hour - 9) * 2 + 1) * 6 + index;
            if (timeHour_arr[1].equals("30")) {
                timeNum_1 += 6;
            }
            timeHour_arr = timeNum_arr[1].split(":");
            hour = Integer.parseInt(timeHour_arr[0]);
            int timeNum_2 = ((hour - 9) * 2 + 1) * 6 + index;
            if (timeHour_arr[1].equals("30")) {
                timeNum_2 += 6;
            }

            for (; timeNum_1 < timeNum_2; timeNum_1 += 6) {
                result += String.valueOf(timeNum_1);
                if (timeNum_2 - timeNum_1 != 6) {
                    result += ",";
                }
            }
        }

        this.timeNum = result;
        return;
    }
}
