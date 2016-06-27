package br.edu.ifspsaocarlos.sdm.volleyasynctaksws;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {
    private RequestQueue queue;
    private EditText etURL;
    private TextView tvData;
    private Button btAcessarWs;
    private ProgressBar mProgress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etURL = (EditText) findViewById(R.id.et_URL);
        tvData = (TextView) findViewById(R.id.tv_data);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);
        mProgress = (ProgressBar) findViewById(R.id.pb_carregando);
    }

    public void onClick(View v) {
        if (v == btAcessarWs) {
            buscarData(etURL.getText().toString());
        }
    }

    private void buscarData(String url) {
        AsyncTask<String, Void, JSONObject> tarefa = new AsyncTask<String, Void, JSONObject>() {
            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.setVisibility(View.VISIBLE);
                tvData.setText("");
            }

            protected JSONObject doInBackground(String... params) {
                queue = Volley.newRequestQueue(MainActivity.this);
                JSONObject jsonObject = null;
                try {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, params[0], null,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    try {
                                        Log.d("REQUEST", "onResponse: " + s);
                                        jsonObject = new JSONObject(s.toString());
                                    }
                                    catch (Exception e){
                                        Toast.makeText(MainActivity.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                                    }
                                    /*catch (JSONException je) {
                                        Toast.makeText(MainActivity.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                            }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MainActivity.this, "Erro na recuperação do número de contatos!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(jsonObjectRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return jsonObject;
            }

            protected void onPostExecute(JSONObject s) {
                super.onPostExecute(s);
                try {
                    tvData.setText(s.toString());
                }catch (NullPointerException nulle){
                    Toast.makeText(getApplicationContext(), R.string.msg_erro_dados, Toast.LENGTH_SHORT).show();
                }
                mProgress.setVisibility(View.GONE);
            }
        };
        Log.d("PA2", "URL: " + url);
        tarefa.execute(url);
    }
}