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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements View.OnClickListener{

    //private static final String REGISTER_URL = myconnect.url + "FvolleyRegister.php";
    public static final String LOGIN_URL = myconnect.url + "FvolleyLogin.php";

    public static final String KEY_USERID="userid";
    public static final String KEY_PASSWORD="password";

    private EditText editTextUserid;
    private EditText editTextPassword;
    private Button buttonLogin;

    private String userid;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserid = (EditText) findViewById(R.id.usrid1);
        editTextPassword = (EditText) findViewById(R.id.pswrdd1);

        SpinnerDatabaseHandler db1=new SpinnerDatabaseHandler(getApplicationContext());
        db1.deleteAll();
        db1.vlaueDB();

        TextView sk=(TextView) findViewById(R.id.sup1);
        sk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent i = new Intent(getApplicationContext(), Registration.class);
                startActivity(i);
                finish();
                return false;
            }
        });

        buttonLogin = (Button) findViewById(R.id.lin1);

        buttonLogin.setOnClickListener(this);

    }


    private void userLogin() {
        userid = editTextUserid.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            openProfile();
                        }else{
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERID,userid);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //openProfile();
    }

    private void openProfile(){
        Intent intent = new Intent(this, HomeActivity.class);
        //intent.putExtra(KEY_USERID, userid);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        userLogin();
//        Intent myIntent = new Intent(LoginActivity.this,
//                HomeActivity.class);
//        startActivity(myIntent);

    }
}