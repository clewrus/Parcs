import java.io.*;
import java.math.*;
import java.util.*;

import parcs.*;

public class Solver implements AM
{

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
        long n, a, b;

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(info.curtask.findFile("input.txt")));

            n = new Long(in.readLine()).longValue();
            a = new Long(in.readLine()).longValue();
            b = new Long(in.readLine()).longValue();
        }
        catch (IOException e)
        {
            System.out.print("Error while reading input\n");

            e.printStackTrace();

            return;
        }

        System.out.print("class Solver method run read data from file\na");

        long tStart = System.nanoTime();

        long res = solve(info, n, a, b);

        long tEnd = System.nanoTime();

        System.out.println("Count = " + res);

        System.out.println("time = " + ((tEnd - tStart) / 1000000) + "ms");
    }

    static public long solve(AMInfo info, long n, long a, long b) {
        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();

        long remainder = (b - a) % n;
        long length = (b - a) / n;

        for (int index = 0; index < n; ++index) {
            long currentStart = index * length;
            long currentEnd = (index + 1) * length + ((n - index - 1 < remainder) ? 1 : 0);

            System.out.println(index + " worker range: " + currentStart + " - " + currentEnd);

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();

            channels.add(newChannel);
            points.add(newPoint);

            newPoint.execute("Count");
            newChannel.write(currentStart);
            newChannel.write(currentEnd);
            newChannel.write(index);
        }

        long result = 0;
        for (int index = 0; index < n; ++index) {
            long threadResult = channels.get(index).readLong();
            System.out.println("Worker result: " + threadResult);
            result += threadResult;
        }

        return result;
    }

}