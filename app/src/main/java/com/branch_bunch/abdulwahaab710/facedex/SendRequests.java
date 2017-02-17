package com.branch_bunch.abdulwahaab710.facedex;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Abdulwahaab710 on 2017-02-15.
 */

public class SendRequests extends AsyncTask<String, Void, String[]>{

        // Create GetText Metod
        public  void  GetText(String filePath) throws IOException {

            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            // Defined URL  where to send data
            URL url = new URL("https://face-dex.herokuapp.com/recognize");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setRequestProperty("image",encodedImage);
                client.setDoOutput(true);
                OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                Log.e("out: ", String.valueOf(outputPost));
                outputPost.flush();
                outputPost.close();
            }
            catch(MalformedURLException error) {
                //Handles an incorrectly entered URL
                Log.e("MalformedURLException: ", String.valueOf(error));
            }
            catch(SocketTimeoutException error) {
                //Handles URL access timeout.
                Log.e("SocketTimeoutException ", String.valueOf(error));
            }
            catch (IOException error) {
                //Handles input and output errors
                Log.e("IOException ", String.valueOf(error));
            }
            finally {
                if(client != null) // Make sure the connection is not null.
                    client.disconnect();
            }



    }

    public static String encodeImage(Bitmap thumbnail) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.URL_SAFE);
        return imageEncoded;
    }

    @Override
    protected String[] doInBackground(String... path) {
        try {
            //File img = new File(String.valueOf(path));
            Log.e("File path", path[0]);
            Bitmap bitimg = BitmapFactory.decodeFile(path[0]);
            String base64img = encodeImage(bitimg);
            Log.e("base64", base64img);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image", base64img);
            String data = jsonObject.toString();
            URL url = new URL("http://face-dex.herokuapp.com/recognize");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setFixedLengthStreamingMode(data.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();
            connection.connect();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            String result = sb.toString();
            Log.d("Vicky", "Response from php = " + result);
            //Response = new JSONObject(result);
            connection.disconnect();
        } catch (Exception e) {
            Log.d("Vicky", "Error Encountered");
            e.printStackTrace();
        }
        return new String[]{"done", "h1"};
    }
}
