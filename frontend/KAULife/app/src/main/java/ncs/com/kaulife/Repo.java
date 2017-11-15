package ncs.com.kaulife;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kkss2 on 2017-11-13.
 */

public class Repo {
    public ServerInterface getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerInterface serverInterface = retrofit.create(ServerInterface.class);
        return serverInterface;
    }
}
