package ncs.com.kaulife;

import com.orm.SugarRecord;

/**
 * Created by kkss2 on 2017-11-12.
 */

public class LoginData extends SugarRecord<LoginData> {
    String id;
    String password;

    public void LoginData () {

    }

    public void LoginData (String id, String password) {
        this.id = id;
        this.password = password;
    }
}
