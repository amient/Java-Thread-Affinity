package vanilla.java.affinity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author peter.lawrey
 */
public class AffinityLockTest {
    @Test
    public void dumpLocks() {
        AffinityLock[] locks = {
                new AffinityLock(0, true, false),
                new AffinityLock(1, false, false),
                new AffinityLock(2, false, true),
                new AffinityLock(3, false, true),
                new AffinityLock(4, true, false),
                new AffinityLock(5, false, false),
                new AffinityLock(6, false, true),
                new AffinityLock(7, false, true),
        };
        locks[2].assignedThread = new Thread(new InterrupedThread(), "logger");
        locks[2].assignedThread.start();
        locks[3].assignedThread = new Thread(new InterrupedThread(), "engine");
        locks[3].assignedThread.start();
        locks[6].assignedThread = new Thread(new InterrupedThread(), "main");
        locks[7].assignedThread = new Thread(new InterrupedThread(), "tcp");
        locks[7].assignedThread.start();
        final String actual = AffinityLock.dumpLocks0(locks);
        assertEquals("0: General use CPU\n" +
                "1: CPU not available\n" +
                "2: Thread[logger,5,main] alive=true\n" +
                "3: Thread[engine,5,main] alive=true\n" +
                "4: General use CPU\n" +
                "5: CPU not available\n" +
                "6: Thread[main,5,main] alive=false\n" +
                "7: Thread[tcp,5,main] alive=true\n", actual);
        System.out.println(actual);

        locks[2].assignedThread.interrupt();
        locks[3].assignedThread.interrupt();
        locks[7].assignedThread.interrupt();
    }

}