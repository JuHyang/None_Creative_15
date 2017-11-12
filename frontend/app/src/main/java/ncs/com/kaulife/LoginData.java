package ncs.com.kaulife;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2017-11-12.
 */

public class LoginData extends SugarRecord<LoginData> {
    String studentNum;
    String password;


    public LoginData () {

    }

    public LoginData (String studentNum, String password) {
        this.studentNum = studentNum;
        this.password = password;
    }
}
