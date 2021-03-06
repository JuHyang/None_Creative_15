package ncs.com.kaulife;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kkss2 on 2017-11-12.
 */

public interface ServerInterface {
    @POST("/lms/data")
    @FormUrlEncoded
    public Call<ArrayList<LmsData>> GetLmsData (@Field("studentNum") String studentNum, @Field("password") String password);

    @POST("/login")
    @FormUrlEncoded
    public Call<String> LmsLogin(@Field("studentNum") String studentNum, @Field("password") String password);

    @GET("/schedule/{label}")
    public Call<ArrayList<ScheduleData>> GetScheduleData (@Path("label") String label);

    @POST("/grade/now")
    @FormUrlEncoded
    public Call<ArrayList<GradeData>> GetGradeNow (@Field("studentNum") String studentNum, @Field("password") String password);


}
