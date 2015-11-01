package com.example.haylin2002.loveStory;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends ActionBarActivity {
    protected String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radiobutton_female:
                if (checked)
                    gender = "female";
                    break;
            case R.id.radiobutton_male:
                if (checked)
                    gender = "male";
                    break;
        }
    }

    public void register(View v) {
        EditText editText = (EditText) findViewById(R.id.name);
        final String username = editText.getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String password2 = ((EditText) findViewById(R.id.password2)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        if (!(password.equals(password2))) {
            Toast.makeText(SignUp.this, R.string.toast_password, Toast.LENGTH_SHORT).show();
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.put("gender",gender);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Toast.makeText(SignUp.this, " ",
                                Toast.LENGTH_SHORT).show();

                        ParseUser.logInInBackground(username, password, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, com.parse.ParseException e) {
                                if (e == null) {
                                    Toast.makeText(SignUp.this, R.string.toast_signup,
                                            Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(SignUp.this, LoveStory.class);
//                                    startActivity(intent);
                                    Intent intent = new Intent(SignUp.this, MatchWithPartner.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignUp.this, e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


}

