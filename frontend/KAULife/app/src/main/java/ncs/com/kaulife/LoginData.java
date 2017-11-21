package ncs.com.kaulife;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2017-11-12.
 */

public class LoginData extends SugarRecord<LoginData> {
    String studentNum;
    String password;
    boolean lmsAuto;


    public LoginData () {

    }

    public LoginData (String studentNum, String password, boolean lmsAuto) {
        this.studentNum = studentNum;
        this.password = password;
        this.lmsAuto = lmsAuto;
    }
}