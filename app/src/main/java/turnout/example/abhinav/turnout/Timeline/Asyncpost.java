package turnout.example.abhinav.turnout.Timeline;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;



public class Asyncpost extends AsyncTask<JSONObject,Void,Void> {

    @Override
    protected Void doInBackground(JSONObject... jsonObjects) {
        JSONObject json=jsonObjects[0];
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json.toString());


       final String fcm="AAAASYicmCY:APA91bHXokrr8OT3o6pqjjNJ_28mthgjnUgdyFEnR7EKZvJifMKE8-mUnBIVRM9AS-yF9iv82COH9FKgp7aD8buj25k9BK6F_KgaVt37ybeWtDNMEAUgvB3fWGkM77w23YQm19sSDJnc";
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
