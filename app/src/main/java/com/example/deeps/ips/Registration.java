package com.example.deeps.ips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends Activity implements View.OnClickListener {

    private static final String REGISTER_URL = myconnect.url + "FvolleyRegister.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERID = "userid";
    public static final String KEY_MOBILENO = "mobileno";
    public static final String KEY_PASSWORD = "password";

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextUserid;
    private EditText editTextMobile_no;
    private EditText editTextPassword;
    private EditText getEditTextConfPass;


    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextUsername = (EditText) findViewById(R.id.usrusr);
        editTextEmail= (EditText) findViewById(R.id.mail);
        editTextUserid = (EditText) findViewById(R.id.usrusrid);
        editTextMobile_no = (EditText) findViewById(R.id.mobphone);
        editTextPassword = (EditText) findViewById(R.id.pswrdd);
        getEditTextConfPass = (EditText) findViewById(R.id.cpswrdd);

        TextView sk=(TextView) findViewById(R.id.lin);
        sk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                return false;
            }
        });

        buttonRegister = (Button) findViewById(R.id.sup);
        buttonRegister.setOnClickListener(this);

    }



    private void registerUser(){
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String userid = editTextUserid.getText().toString().trim();
        final String mobileno = editTextMobile_no.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String username = editTextUsername.getText().toString();
                        if (!isValidField(username)) {
                            editTextUsername.setError("Invalid UserName");
                        }

                        String email = editTextEmail.getText().toString();
                        if (!isEmailValid(email)) {
                            editTextEmail.setError("Invalid E_mail");
                        }

                        String userid = editTextUserid.getText().toString();
                        if (!isValidField(userid)) {
                            editTextUserid.setError("Invalid E_mail");
                        }

                        String phn = editTextMobile_no.getText().toString();
                        if (!ispValid(phn)) {
                            editTextMobile_no.setError("Invalid Mobile_No");
                        }

                        String password = editTextPassword.getText().toString();
                        if(!isValidPass(password)){
                            editTextPassword.setError("Password Should Contain Atleast 6 Character ");
                        }

                        String Conf = getEditTextConfPass.getText().toString();

                        if(isValidField(username) && isEmailValid(email) && isValidField(userid) && ispValid(phn)&& isValidPass(password) && Conf.equals(password)) {
                            openProfile();
                            Toast.makeText(Registration.this, response, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "PassWord Doesn't Match OR Some Field Is Worng", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Registration.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,username);
                params.put(KEY_EMAIL, email);
                params.put(KEY_USERID,userid);
                params.put(KEY_MOBILENO,mobileno);
                params.put(KEY_PASSWORD,password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openProfile(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){

        }
        registerUser();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean ispValid(String ph) {
        boolean isValid = false;

        String expression = "^[7-9][0-9]{9}$";
        CharSequence inputStr = ph;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean isValidField(String field) {
        if (field != null && field.length() >= 1 ) {
            return true;
        }
        return false;
    }

    public boolean isValidPass(String pass) {
        if (pass != null && pass.length() >= 6 ) {
            return true;
        }
        return false;
    }

    /*public boolean isValidPhn(String phn) {
        if (phn != null && phn.length() <= 10 ) {
            return true;
        }
        return false;
    }*/

}