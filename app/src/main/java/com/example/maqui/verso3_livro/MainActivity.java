package com.example.maqui.verso3_livro;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<Livro> livros= new ArrayList<>(  );
    Button add;

    private static final String URL_JSON = "http://stadsifba.pythonanywhere.com/api_rest/livros/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );
        MyTask task = new MyTask();
        task.execute( URL_JSON );

        add=(Button)findViewById( R.id.add);
        add.setOnClickListener( this );

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener( getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Livro livro= livros.get( position );

                Toast.makeText( getApplicationContext(),livro.getTitulo(),Toast.LENGTH_SHORT ).show();
                startActivity( new Intent( MainActivity.this,Detalhar_Activity.class ) );
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Livro livro= livros.get( position );

                Toast.makeText( getApplicationContext(),livro.getTitulo(),Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        } ) );
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.add)
            startActivity( new Intent( MainActivity.this,Cadastrar_Activity.class ) );
        else if(v.getId()==R.id.editar)
            startActivity( new Intent( MainActivity.this,Editar_Activity.class ) );
    }

    public class MyTask extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl= strings[0];
            HttpURLConnection httpURLConnection= null;
            InputStream inputStream= null;
            InputStreamReader inputStreamReader= null;
            StringBuffer stringBuffer= null;

            try {
                URL url= new URL( stringUrl );
                httpURLConnection= (HttpURLConnection)url.openConnection();

                httpURLConnection.connect();

                inputStream= httpURLConnection.getInputStream();
                inputStreamReader= new InputStreamReader( inputStream );
                BufferedReader reader= new BufferedReader( inputStreamReader );
                stringBuffer= new StringBuffer(  );
                String line="";

                while((line= reader.readLine())!=null){
                    stringBuffer.append( line );
                }


            } catch (MalformedURLException e) {//url invalida
                e.printStackTrace();
            } catch (IOException e) {//erro de conexão com o servidor
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute( s );
           livros=list( s );

            Adapter adapter = new Adapter( livros );

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( MainActivity.this );
            recyclerView.setLayoutManager( layoutManager );
            recyclerView.setHasFixedSize( true );//otimiza, tamanho fixo
            recyclerView.addItemDecoration( new DividerItemDecoration( MainActivity.this, LinearLayout.VERTICAL ) );//cria um divisor vertical
            recyclerView.setAdapter( adapter );

        }

        public List<Livro> list(String json){
            List<Livro> livros= new ArrayList<>(  );

            try {
                JSONArray array= new JSONArray( json );

                for(int i=0; i<array.length(); i++){
                    JSONObject object= array.getJSONObject( i );

                    int codigo= object.getInt( "codigo" ) ;
                    String ISBN= object.getString( "ISBN" ) ;
                    String titulo= object.getString( "titulo" ) ;
                    String autor=  object.getString( "autor" ) ;
                    String ano= object.getString( "ano" ) ;
                    String editora= object.getString( "editora" );

                    livros.add(new Livro( codigo, ISBN, titulo, autor,  ano,editora, MainActivity.this) );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return livros;
        }
    }


}



