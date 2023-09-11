package org.example.services;

import com.google.common.hash.*;
import org.example.model.Pirate;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public abstract class HashGenerator {
    static public String hashFrom(List<Pirate> pirateList){
        String piratesString = "";
        for(Pirate pirate: pirateList)
            piratesString += pirate.toString();
        HashFunction hf = Hashing.sha256();
        HashCode hc = hf.newHasher().putUnencodedChars(piratesString).hash();
        return hc.toString();
    }

    static public String hashFrom(String someString) {
        String sha ="";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(someString.getBytes("UTF-8"));
            byte byteArr [] = md.digest();
            for(byte b: byteArr){
                String bHex = Integer.toHexString(0xff & b);
                sha += bHex.length() == 1?"0" + bHex:bHex;
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return sha;
    }
}
