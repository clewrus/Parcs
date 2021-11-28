import java.io.*;
import java.lang.reflect.Array;
import java.math.*;
import java.util.*;

import parcs.*;

public class Solver implements AM
{

    //test 228 ^ 18 = 322 (mod 2287)
    //test 322 ^ 894 = 228 (mod 3221)

    public static void main(String[] args)
    {
        System.out.print("class Solver start method main\n");

        task mainTask = new task();

        mainTask.addJarFile("Solver.jar");
        mainTask.addJarFile("Count.jar");

        System.out.print("class Solver method main adder jars\n");
        (new Solver()).run(new AMInfo(mainTask, (channel)null));
        System.out.print("class Solver method main finish work\n");

        mainTask.end();
    }

    public void run(AMInfo info)
    {
        int workerNum;
        BigInteger a, b, m;

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(info.curtask.findFile("input.txt")));

            // a^x = b (mod m)
            workerNum = Integer.parseInt( in.readLine() );
            a = new BigInteger( in.readLine() );
            b = new BigInteger( in.readLine() );
            m = new BigInteger( in.readLine() );
        }
        catch (IOException e)
        {
            System.out.println("Error while reading input.\nExpected workerNum, a, b, m in separate lines (a^x = b (mod m))");
            e.printStackTrace();
            return;
        }

        System.out.println("Read input file, got");
        System.out.println("workerNum = " + workerNum);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("m = " + m);

        long tStart = System.nanoTime();
        ArrayList<BigInteger> res = solve(info, workerNum, a, b, m);
        long tEnd = System.nanoTime();

        res.sort(BigInteger::compareTo);

        System.out.println("Founded roots: ");
        for( BigInteger x : res ) {
            System.out.println("x = " + x.toString());
        }

        System.out.println();
        System.out.println("time = " + ((tEnd - tStart) / 1000000) + "ms");
    }

    static public ArrayList<BigInteger> solve(AMInfo info, int workersNum, BigInteger a, BigInteger b, BigInteger m) {
        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();

        BigInteger remainder = m.mod( BigInteger.valueOf( workersNum ) );
        BigInteger length = m.divide( BigInteger.valueOf( workersNum) );

        for (int index = 0; index < workersNum; ++index) {
            BigInteger currentStart = length.multiply( BigInteger.valueOf(index) );
            BigInteger currentEnd = length.multiply(( BigInteger.valueOf(index + 1)));

            if( index == workersNum - 1) {
                currentEnd = currentEnd.add( remainder );
            }

            System.out.println(index + " worker range: " + currentStart + " - " + currentEnd);

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();

            channels.add(newChannel);
            points.add(newPoint);

            newPoint.execute("Count");
            newChannel.write(currentStart);
            newChannel.write(currentEnd);
            newChannel.write(a);
            newChannel.write(b);
            newChannel.write(m);
        }

        ArrayList<BigInteger> result = new ArrayList<>();
        for (int index = 0; index < workersNum; ++index) {
            ArrayList<BigInteger> threadResult = (ArrayList<BigInteger>)channels.get(index).readObject();
            result.addAll(threadResult);
        }

        return result;
    }

}