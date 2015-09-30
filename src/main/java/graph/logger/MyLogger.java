package graph.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class MyLogger {

	public Object watchTime(ProceedingJoinPoint joinpoint) {
		long start = System.currentTimeMillis();
		System.out.println("method begin: " + joinpoint.getSignature().toShortString());
		Object output = null;
		try {
			output = joinpoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		long time = System.currentTimeMillis() - start;
		System.out.println("method end: " + joinpoint.getSignature().toShortString() + ", time=" + time + " ms");

		return output;
	}

}
