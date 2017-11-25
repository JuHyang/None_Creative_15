package ncs.com.kaulife;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by kkss2 on 2017-11-12.
 */

public interface ServerInterface {
    @POST("/lms/data")
    public Call<ArrayList<LmsData>> GetLmsData (@Body LoginData loginData);

    @POST("/login")
    @FormUrlEncoded
    public Call<LoginReceiveData> LmsLogin(@Field("studentNum") String studentNum, @Field("password") String password);


}
