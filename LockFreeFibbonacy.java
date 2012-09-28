import java.math.*;
import java.util.concurrent.atomic.*;

public class LockFreeFibbonacy {
    private static class Store {
        public BigInteger a;
        public BigInteger b;
    }


    public static AtomicReference<Store> as;
    public static void main(String[] args) {
        Store s = new Store();
        s.a = BigInteger.ZERO;
        s.b = BigInteger.ONE;
        as = new AtomicReference(s);
        R r = new R();
        for (int i = 0; i < 20; i++)
            new Thread(r).start();
    }

    private static class R implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++)
                System.out.println(next());
        }
    }


    public static BigInteger next() {
        Store s = as.get();
        Store ns = new Store();
        ns.a = s.b;
        ns.b = s.a.add(s.b);
        if (as.compareAndSet(s, ns))
            return ns.b;
        else {        
            do {
                s = as.get();
                ns.a = s.b;
                ns.b = s.a.add(s.b);
            } while (!as.compareAndSet(s, ns));
        }
        return ns.b;
        
    }
}
