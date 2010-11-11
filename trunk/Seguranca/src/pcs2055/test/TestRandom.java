package pcs2055.test;

import pcs2055.sponge.SpongePRNG;
import pcs2055.sponge.SpongeRandom;

public class TestRandom {

    public static void main(String[] args) {

        SpongeRandom random = new SpongePRNG();
        random.init(0);
        random.feed(new byte[100], 100);
        byte[] z = new byte[100];
        z = random.fetch(z, 100);
        for (int i=0; i<25; i++)
            System.out.printf("%x", z[i]);
        
    }

}
