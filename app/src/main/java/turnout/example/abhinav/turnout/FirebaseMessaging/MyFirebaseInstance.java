package turnout.example.abhinav.turnout.FirebaseMessaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Abhinav on 05-Feb-17.
 */

public class MyFirebaseInstance extends FirebaseInstanceIdService
{

    private static final String REG_TOKEN ="REG_TOKEN";

    @Override
    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, recent_token);
    }
}
