import parcs.*;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;

public class Count implements AM {
    public void run(AMInfo info) {
        BigInteger from = (BigInteger)info.parent.readObject();
        BigInteger to = (BigInteger)info.parent.readObject();

        BigInteger a = (BigInteger)info.parent.readObject();
        BigInteger b = (BigInteger)info.parent.readObject();
        BigInteger m = (BigInteger)info.parent.readObject();

        ArrayList<BigInteger> roots = new ArrayList<BigInteger>();

        for(BigInteger x = from; x.compareTo(to) < 0; x = x.add(BigInteger.valueOf(1))) {
            BigInteger y = a.modPow( x, m );
            if( y.compareTo(b) == 0 ) {
                roots.add(x);
            }
        }

        info.parent.write(roots);
    }
}
