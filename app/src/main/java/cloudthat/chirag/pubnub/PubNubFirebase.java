package cloudthat.chirag.pubnub;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

/**
 * Created by ChiragS on 14-11-2017.
 */

public class PubNubFirebase extends FirebaseInstanceIdService {
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("FireBaseID", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToPubNubFunctions(refreshedToken);
    }
    private void sendRegistrationToPubNubFunctions(String refreshedToken) {
        //Send to PubNub/Firebase; I don't know
        Log.i("FireBaseID", "Token is " + refreshedToken);
        String msg = "{registrationToken : " + refreshedToken + "}";
        String pubKey = "pub-c-0a3429ae-b9ef-49ec-a7ab-49be12e262da";
        String subKey = "sub-c-984dc138-5118-11e7-8e91-0619f8945a4f";
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.publish().channel("test_channel3").message(msg).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
                if (!status.isError()) {
                    Log.i("PubNub", String.valueOf(status.getStatusCode()));
                    Log.i("PubNub", status.getCategory().toString());
                    // Message successfully published to specified channel.
                }
                // Request processing failed.
                else {
                    Log.i("PubNub", String.valueOf(status.getStatusCode()));
                    Log.i("PubNub", status.getCategory().toString());
                    Log.i("PubNub", status.getErrorData().toString());
                    // Handle message publish error. Check 'category' property to find out possible issue
                    // because of which request did fail.
                    //
                    // Request can be resent using: [status retry];
                }
            }
        });
        //Send this data to a specific channel that will store the data in PubNub vault using PubNub functions
        //When a msg from DIY kit hits PubNub, a PubNub function will trigger
        //The triggered PubNub function will pull the registration token from the vault and send to FireBase
        //Firebase will then send the push notification to the app
    }
}
