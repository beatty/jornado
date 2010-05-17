package jornado;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Tree-structured profile of a request.
 * Currently assumes all work is on a single thread, which is ridiculous. TODO.
 */
public class RequestProfile {
    private static ThreadLocalProfile tl = new ThreadLocalProfile();

    private final Stack<Activity> activityStack = new Stack<Activity>();
    private Activity root;

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public RequestProfile() {
        init();
    }

    public static void render(PrintWriter printWriter) throws IOException {
        root().render(printWriter, 0);
    }

    public static void finish() {
        root().end();
    }

    public void init() {
        root = new Activity(null, "request", System.currentTimeMillis());
        activityStack.push(root);
    }

    private static class ThreadLocalProfile extends ThreadLocal<RequestProfile> {
        public RequestProfile initialValue() {
            return new RequestProfile();
        }
    }

    public static void push(String activity) {
        final Stack<Activity> stack = tl.get().getActivityStack();
        final Activity currentActivity = stack.peek();
        final Activity newActivity = new Activity(currentActivity, activity, System.currentTimeMillis());
        if (currentActivity == null) {
            throw new IllegalStateException("no parent activity");
        }
        currentActivity.addChild(newActivity);
        stack.push(newActivity);
    }

    public static void pop() {
        Activity activity = tl.get().getActivityStack().pop();
        activity.end();
    }

    public static void clear() {
        tl.get().reset();
    }

    public static Activity root() {
        return tl.get().root;
    }

    private void reset() {
        init();
    }


    public static class Activity {
        private final Activity parent;
        private final String name;
        private final long startTime;
        private long endTime;
        private List<Activity> children = new LinkedList<Activity>();

        public Activity(Activity parent, String name, long startTime) {
            this.parent = parent;
            this.name = name;
            this.startTime = startTime;
        }

        public void end() {
            this.endTime = System.currentTimeMillis();
        }

        public String getName() {
            return name;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public long getElapsed() {
            return endTime - startTime;
        }

        public Activity getParent() {
            return parent;
        }

        public void addChild(Activity activity) {
            children.add(activity);
        }

        public List<Activity> getChildren() {
            return children;
        }

        public void render(PrintWriter writer, int level) throws IOException {
            writeAtLevel(writer, level, String.format("%s - %d ms (%.2f)", getName(), getElapsed(),
                    computePercentage()));
            final int nextLevel = level + 1;
            for (Activity activity : getChildren()) {
                activity.render(writer, nextLevel);
            }
        }

        private double computePercentage() {
            return getParent() != null ?
                    (getParent().getElapsed() > 0 ?
                            (100 * (double) getElapsed() / getParent().getElapsed()) : 0) :
                    100.0f;
        }

        private static void writeAtLevel(PrintWriter writer, int level, String value) throws IOException {
            for (int i = 0; i < level; i++) {
                writer.write("  ");
            }
            writer.println(value);
        }
    }
}
