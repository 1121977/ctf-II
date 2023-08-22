package org.example.services;

import com.google.common.hash.*;
import org.example.model.Pirate;
import java.util.List;

public abstract class URIGenerator {
    static public String hashFrom(List<Pirate> pirateList){
        String piratesString = "";
        for(Pirate pirate: pirateList)
            piratesString += pirate.toString();
        HashFunction hf = Hashing.sha256();
        HashCode hc = hf.newHasher().putUnencodedChars(piratesString).hash();
        return hc.toString();
    }
}
