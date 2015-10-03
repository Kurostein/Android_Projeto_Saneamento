package br.com.lorencity.fotoesgoto;




import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;




/**
 * Created by Diego on 01/10/2015.
 */
public class ConnectClient {
    private String url;

    public ConnectClient(String url) {
        this.url = url;
    }

    public String doPost(Map parametros) {
        String resultado = null;
        try {
            String texto = retonarString(parametros);
            resultado = enviarParametros(texto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private String retonarString(Map parametros) throws Exception {
        if (parametros == null || parametros.size() == 0) {
            return null;
        }

        String urlParametros = null;
        Iterator e = (Iterator) parametros.keySet().iterator();
        while (e.hasNext()) {
            String chave = (String) e.next();
            Object objValor = parametros.get(chave);
            String valor = objValor.toString();
            urlParametros = urlParametros == null ? "" : urlParametros + "&";
            urlParametros += chave + "=" + valor;
        }
        return urlParametros;
    }

    public String enviarParametros(String parametros) throws IOException {


        URL conexao = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) conexao.openConnection();

        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();

        OutputStream out = httpURLConnection.getOutputStream();
        byte[] bytes = parametros.getBytes("UTF-8");
        out.write(bytes);
        out.flush();
        out.close();

        InputStream msg = httpURLConnection.getInputStream();
        String resultado = msg.toString();
        httpURLConnection.disconnect();

        return resultado;
    }
}

