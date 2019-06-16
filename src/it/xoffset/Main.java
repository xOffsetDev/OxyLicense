package it.xoffset;

import it.xoffset.utils.Auth;

import javax.swing.*;

public class Main {
    private static Auth auth = new Auth();

    public static void main(String[] args){
        auth.initLocalAuthorized();
        if(auth.LocalAuth() && auth.OnlineAuth()){
            JOptionPane.showMessageDialog(null, "Success :)");
        }else{
            JOptionPane.showMessageDialog(null, "Failed :c");
        }
    }
}
