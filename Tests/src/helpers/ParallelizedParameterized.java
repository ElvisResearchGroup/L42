package helpers;


import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* The code below is lightly modified from:
 * http://stackoverflow.com/questions/13504796/running-junit-parameterized-tests-in-parallel-in-eclipse
 * which credits:
 * http://hwellmann.blogspot.pt/2009/12/running-parameterized-junit-tests-in.html
 */


public class ParallelizedParameterized extends Parameterized {
    private static int numThreads = 2;
    
    public static void setNumThreads(int newNumThreads) {
      assert newNumThreads >= 1;
      numThreads = newNumThreads;
    }
    
    private static class ThreadPoolScheduler implements RunnerScheduler {
        private ExecutorService executor; 

        public ThreadPoolScheduler() {
          // Previous plan for setting threads looked in the environment
          // String threads = System.getProperty("junit.parallel.threads", "16");
          // int numThreads = Integer.parseInt(threads);
            executor = Executors.newFixedThreadPool(numThreads);
        }

        @Override
        public void finished() {
            try {
                while (true) {
                    if (executor.awaitTermination(1, TimeUnit.MINUTES)) {
                        break;
                    }
                    System.out.println("ParallelizedParameterized.finish() waiting to time out.");
                }
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        public void schedule(Runnable childStatement) {
            executor.submit(childStatement);
        }
    }

    public ParallelizedParameterized(Class klass) throws Throwable {
        super(klass);
        setScheduler(new ThreadPoolScheduler());
    }
}
