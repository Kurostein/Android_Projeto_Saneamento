package br.com.lorencity.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by usuario on 03/10/2015.
 */
public class ServerConnection {

    private static final String TAG = "PROCESS STAGE";

    private URL url;
    private String imageServletAddress = "http://10.0.2.2:8080/SANIT/ServletImagem";
    private String userServletAddress = "http://10.0.2.2:8080/SANIT/ServletUsuario";

    private HttpURLConnection httpConn;

    public ServerConnection(){

    }

    public void setServerAddress(String url) throws MalformedURLException{
        this.url = new URL(url);
    }

    public String getImageServletAddress(){
        return this.imageServletAddress;
    }

    public String getUserServletAddress(){
        return this.userServletAddress;
    }

    public String sendImageToServer(String data) throws ExecutionException, InterruptedException {
        return new ImageDataWriterTask().execute(data).get();
    }

    public String sendTextDataToServer(String data) throws ExecutionException, InterruptedException{
        return new TextDataWriterTask().execute(data).get();
    }

    private void prepareConnection() throws IOException{
        URLConnection urlConn = url.openConnection();

        this.httpConn = (HttpURLConnection) urlConn;
        this.httpConn.setReadTimeout(10000);
        this.httpConn.setConnectTimeout(15000);
        this.httpConn.setDoInput(true);
        this.httpConn.setDoOutput(true);
        this.httpConn.setUseCaches(false);
    }

    public void openPostConnection() throws IOException{
        prepareConnection();
        httpConn.setRequestMethod("POST");
        httpConn.connect();
    }

    public void openGetConnection() throws IOException{
        prepareConnection();
        httpConn.setRequestMethod("GET");
    }

    public void writeDataUTF(String data) throws IOException{
        DataOutputStream dataStream = getDataOutStream();

        dataStream.writeUTF(data);
        dataStream.flush();
        dataStream.close();
    }

    public void writeData(String data) throws IOException{
        DataOutputStream dataStream = getDataOutStream();

        dataStream.writeBytes(data);
        dataStream.flush();
        dataStream.close();
    }

    private DataOutputStream getDataOutStream() throws IOException{
        return new DataOutputStream(httpConn.getOutputStream());
    }

    public String getServerResponseData() throws IOException{
        int responseCode = httpConn.getResponseCode();
        String response;

        if(responseCode != HttpURLConnection.HTTP_OK){
            throw new ConnectException("Connection not completed.");
        }

        InputStream inStream = httpConn.getInputStream();

        response = getServerResponseToString(inStream);

        inStream.close();
        closeConnection();

        return response;
    }

    private String getServerResponseToString(InputStream inStream) throws IOException{
        String line = null;
        StringBuilder sBuilder = new StringBuilder();

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream));

        try{
            while((line = bufReader.readLine()) != null){
                sBuilder.append(line + " ");
            }
        }catch (IOException e){
            Log.i("STRING APPEND ERROR", e.toString());
        }

        bufReader.close();
        return sBuilder.toString();
    }

    public void closeConnection(){
        httpConn.disconnect();
    }

    private class ImageDataWriterTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.v(TAG, "In background");
                Log.v(TAG, "In connection: "+"Encoded imagem, sendding to server.");
                setServerAddress(getImageServletAddress());
                openPostConnection();
                writeDataUTF(urls[0]);
                Log.v(TAG, "Wrote data in server.");
                return getServerResponseData();
            }catch (IOException e){
                return e.getMessage();
            }
        }
    }

    private class TextDataWriterTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.v(TAG, "In background");
                Log.v(TAG, "In connection: "+"Text data, sendding to server. ("+urls[0]+")");
                setServerAddress(getUserServletAddress());
                openPostConnection();
                writeData(urls[0]);
                Log.v(TAG, "Wrote data in server.");
                return getServerResponseData();
            }catch (IOException e){
                return e.getMessage();
            }
        }
    }
}
