package turnout.example.abhinav.turnout.Timeline;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

/**
 * Created by Abhishek on 01-08-2017.
 */

public class Asyncpost extends AsyncTask<JSONObject,Void,Void> {

    @Override
    protected Void doInBackground(JSONObject... jsonObjects) {
        JSONObject json=jsonObjects[0];
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json.toString());


       final String fcm="AAAAsu33J24:APA91bHM2kXnx7Cy2HoMCm2X5qrhjcHXsHhmViLIbyOxf2aFGnF0CWreREf3qFViYM8K1PotbwKw2SeouoSh0jBCqYWco5BuSwSDZlp_MhHAB9G7FeojjnaUWf5CNFSrcbTdkd6gTATj";
        Request request=new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","key="+fcm)
                .post(body)
                .build();
        OkHttpClient okHttpClient=new OkHttpClient();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d("TAG",response.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        return null;
    }
}
