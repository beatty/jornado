package jornado;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimingInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation invocation) throws Throwable {
        RequestProfile.push(activityName(invocation));
        final Object rv = invocation.proceed();
        RequestProfile.pop();
        return rv;
    }

    private static String activityName(MethodInvocation methodInvocation) {
        final String name = methodInvocation.getThis().getClass().getName();
        return name.substring(0, name.indexOf("$$EnhancerByGuice")) + "." + methodInvocation.getMethod().getName();
    }
}