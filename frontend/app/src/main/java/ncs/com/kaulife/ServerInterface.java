package ncs.com.kaulife;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by kkss2 on 2017-11-12.
 */

public interface ServerInterface {
    @POST("/lms/data")
    public Call<ArrayList<LmsData>> GetLmsData (@Body LoginData loginData);

    @POST("/login")
    public Call<String> LmsLogin(@Body LoginData loginData);


}
