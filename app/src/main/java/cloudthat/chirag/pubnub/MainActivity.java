package cloudthat.chirag.pubnub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.pubnub.api.*;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.*;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "PubNubUserFile";
    public static final String pubKey = "pubKey";
    public static final String subKey = "subKey";
    public static final String nameKey = "nameKey";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findPreferences();
        Button submitButton = findViewById(R.id.storeKeys);
        final EditText pubKeyField = findViewById(R.id.pubKeyField);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        /*
        submitButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
        editor.putString("pubkey", String.valueOf(pubKeyField.getText()));
        Log.i("Preferences", "pubkey is " + String.valueOf(pubKeyField.getText()));
        editor.apply();
        startActivity(intent);
        }
        });
        */
        final ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton3);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.i("PubNub", "Checked!");
                    primeAlarm();

                } else {
                    // The toggle is disabled
                    Log.i("PubNub", "Unchecked!");
                    unPrimeAlarm();
                }
            }
        });
    }

    private void findPreferences() {
    }

    private void primeAlarm() {
        String prime = "{'state':'O'}";
        sendMessage(prime);
    }

    private void unPrimeAlarm() {
        String unPrime = "{'state':'F'}";
        sendMessage(unPrime);
    }

    private void sendMessage(String msg) { //not using configVar; will do that later
        PNConfiguration pnConfiguration = new PNConfiguration();
        //String pubKey = "pub-c-0a3429ae-b9ef-49ec-a7ab-49be12e262da";
        //String subKey = "sub-c-984dc138-5118-11e7-8e91-0619f8945a4f";
        SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Log.i("Preferences", "Bagged SharedPreferences File");
        String pubKey = sharedPref.getString("pubkey","default");
        String subKey = sharedPref.getString("subkey","default");
        Log.i("Preferences", "Pub Key from preferences: " + sharedPref.getString("pubkey","default"));
        Log.i("Preferences", "Sub Key from preferences: " + sharedPref.getString("subkey","default"));
        Log.i("Preferences", String.valueOf(sharedPref.getAll()));
//        Log.i("Preferences", pubKey);
//        Log.i("Preferences", subKey);
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        PubNub pubnub = new PubNub(pnConfiguration);
        Log.i("PubNub", "PubNub Set up!");
        pubnub.publish().channel("test_channel2").message(msg).async(new PNCallback<PNPublishResult>() {
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
    }



    //    public void sendMessage(View view, String msg) {
public void sendMessage2(String msg) {
    Log.i("PubNub", "Button clicked");
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-0a3429ae-b9ef-49ec-a7ab-49be12e262da");
        pnConfiguration.setSubscribeKey("sub-c-984dc138-5118-11e7-8e91-0619f8945a4f");
        PubNub pubnub = new PubNub(pnConfiguration);
        Log.i("PubNub", "PubNub Set up");
        pubnub.publish().channel("test_channel2").message(msg).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
                if (!status.isError()) {
                    Log.i("PubNub", String.valueOf(status.getStatusCode()));
                    // Message successfully published to specified channel.
                }
                // Request processing failed.
                else {
                    Log.i("PubNub", String.valueOf(status.getStatusCode()));
                    // Handle message publish error. Check 'category' property to find out possible issue
                    // because of which request did fail.
                    //
                    // Request can be resent using: [status retry];
                }
            }
        });
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {


                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                        pubnub.publish().channel("test_channel2").message("Subbed to test_channel2").async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (!status.isError()) {

                                    // Message successfully published to specified channel.
                                }
                                // Request processing failed.
                                else {

                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                }
                            }
                        });
                    }
                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                    Log.i("pubNub", String.valueOf(message.getMessage()));
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
//                    Log.i("PubNub", message.getSubscription());
                    Log.i("pubNub", String.valueOf(message.getMessage()));
                }
            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
        pubnub.subscribe().channels(Arrays.asList("test_channel")).execute();
    }
}