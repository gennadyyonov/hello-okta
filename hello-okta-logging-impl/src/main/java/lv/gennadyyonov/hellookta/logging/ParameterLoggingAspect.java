package lv.gennadyyonov.hellookta.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Map;

import static lv.gennadyyonov.hellookta.logging.LoggingUtils.composeArguments;
import static lv.gennadyyonov.hellookta.logging.LoggingUtils.composeResult;

@Aspect
@Slf4j
public class ParameterLoggingAspect {

    @Pointcut("target(ParameterLogging)")
    public void parameterLogging() {
        // Do nothing. Method for declaration
    }

    @Before("parameterLogging()")
    public void printParameters(JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        Map<String, Object> args = LoggingUtils.getLoggableArgs(joinPoint);
        log.info("Calling method {} with parameters  : [{}]", methodName, composeArguments(args));
    }

    @AfterReturning(pointcut = "parameterLogging()", returning = "result")
    public void printResult(JoinPoint joinPoint, Object result) {
        String methodName = getMethodName(joinPoint);
        log.info("Method {} returned  : [{}]", methodName, composeResult(joinPoint, result));
    }

    private String getMethodName(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        return className + "." + signature.getName();
    }
}
