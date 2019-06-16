package it.xoffset.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;

public class Auth {
    private HashSet<String> authorized = new HashSet<>();

    public void initLocalAuthorized(){
        authorized.add(getIdentifier());
    }

    private boolean security(){
        notify("YourID");
        return true;
    }

    public String getIdentifier() {
        try{
            String toEncrypt =  System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] encrypted = md.digest(toEncrypt.getBytes("UTF-8"));
            return HexBin.encode(encrypted);
        }catch(Exception exc){ return null;}
    }

    public String request(String url){
        try{
            StringBuilder sb = new StringBuilder();
            URLConnection urlConnection = new URL(url).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String s = null;
            while((s = br.readLine()) != null){
                sb.append(s);
            }
            return sb.toString();
        }catch(Exception exc){return Config.EMPTY;}
    }

    public boolean LocalAuth(){
        return authorized.contains(getIdentifier()) ? security() : false;
    }

    public boolean OnlineAuth(){
        /*
              PHP Code

        $HWID = $_GET['hwid'];
        $authorized = array("123");
        echo in_array($HWID,$authorized) ? "valid" : "invalid";

         */
        return request(Config.API_ENDPOINT).contains("valid") ? security() : false;
    }

    public void notify(String chatID){
        String ip = request("https://api.ipify.org/");
        request(Config.NOTIFY_ENDPOINT + "/sendMessage?chat_id="+chatID+"&text="+"Authorized Access from <code>"+ip+"</code>"+"&parse_mode=HTML");
    }


}
