package pcs2055.test;

import pcs2055.sponge.SpongePRNG;
import pcs2055.sponge.SpongeRandom;

public class TestRandom {

    public static void main(String[] args) {

        int r = 128;
        SpongeRandom random = new SpongePRNG();
        random.init(0);
        random.feed(new byte[r], r);
        byte[] z = new byte[r];
        for (int i=0; i<5; i++) {
            z = random.fetch(z, r);
            for (int j=0; j<25; j++)
                System.out.printf("%x", z[j]);
            System.out.println();
        }
    }

}
