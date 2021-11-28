import parcs.*;

public class Count implements AM {
    public void run(AMInfo info) {
        long a, b;

        a = info.parent.readLong();
        b = info.parent.readLong();
        long k = 0;
        System.out.println("Worker started");
        for (long n = a; n < b + 1; n++) {
            for (long i = 1; i < n / 3 + 1; i++) {
                if (i * i * i == n) {
                    k++;
                    System.out.print(n + ", ");
                }
            }
        }

        info.parent.write(k);

    }
}
