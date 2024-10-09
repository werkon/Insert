
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import de.esnecca.multi.History;

public class Count {

    public static void main(String[] args) throws Exception {

        for (int d = 1; d <= 14; ++d) {
            History history = new History(7, 6, 2, 4);
            Set<BigInteger> set = new HashSet<BigInteger>(1000000);
            calcSet(d, history, set);
            System.out.printf("%d %d\n", d, set.size());
        }

    }

    private static void calcSet(int depth, History history, Set<BigInteger> set) {

        if (history.getInserted() >= depth) {
            for (int x = 0; x < history.getWidth(); ++x) {
                if (!history.isFull(x)) {
                    if (history.test(x)) {
                        return;
                    }
                    // history.insert(x);
                    // for (int x2 = 0; x2 < history.getWidth(); ++x2) {
                    //     if (!history.isFull(x2)) {
                    //         if (history.test(x2)) {
                    //             history.remove();
                    //             return;
                    //         }
                    //     }
                    // }
                    // history.remove();
                }
            }
            set.add(history.getSmallestBigInteger());
            return;
        }

        for (int x = 0; x < history.getWidth(); ++x) {

            if (!history.isFull(x)) {
                if (!history.test(x)) {
                    history.insert(x);
                    // if (history.getInserted() == 3) {
                    //     history.printHistory();
                    // }
                    calcSet(depth, history, set);
                    history.remove();
                }
            }
        }

    }
}
