package cloudthat.chirag.pubnub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PubNubLogin extends AppCompatActivity {
    public static final String PREFS_NAME = "PubNubUserFile";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_nub_login);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Button submitButton = findViewById(R.id.submitButton);
        final EditText pubKeyField = findViewById(R.id.editText);
        final EditText subKeyField = findViewById(R.id.editText2);
        final EditText usernameField = findViewById(R.id.editText3);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Intent intent = new Intent(PubNubLogin.this, MainActivity.class);
                editor.putString("pubkey", String.valueOf(pubKeyField.getText()));
                editor.putString("subkey", String.valueOf(subKeyField.getText()));
                editor.putString("username", String.valueOf(usernameField.getText()));
                Log.i("Preferences", "pubkey is " + String.valueOf(pubKeyField.getText()));
                Log.i("Preferences", "subkey is " + String.valueOf(subKeyField.getText()));
                Log.i("Preferences", "username is " + String.valueOf(usernameField.getText()));
                editor.apply();
                startActivity(intent);
            }
        });
    }
}
