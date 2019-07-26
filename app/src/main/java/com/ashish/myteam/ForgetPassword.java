package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {

    EditText email;
    Button mButtonResetPass;
    TextView mSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_forget_password);

        email = (EditText)findViewById(R.id.emailForgetlink);
        mButtonResetPass = (Button) findViewById(R.id.button_resetpass);
        mSignup = (TextView) findViewById(R.id.tvsignup);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(ForgetPassword.this,SignUp.class);
                startActivity(signupIntent);
            }
        });

        mButtonResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
//                boolean res = db.checkUser(mail);
//                if(res){
//                    Toast.makeText(ForgetPassword.this,"Password reset link sent to mail",Toast.LENGTH_SHORT).show();
//                    Intent moveLogin = new Intent(ForgetPassword.this,MainActivity.class);
//                    startActivity(moveLogin);
//                }
//                else {
//                    Toast.makeText(ForgetPassword.this,"Email doesn't exist",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}
