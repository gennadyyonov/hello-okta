package lv.gennadyyonov.hellookta.logging;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.logging.LoggingUtils.DEFAULT;
import static lv.gennadyyonov.hellookta.logging.LoggingUtils.LOG_FORMAT;
import static lv.gennadyyonov.hellookta.logging.LoggingUtils.PERFORMANCE_MARKER;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

@Aspect
@Slf4j
public class PerformanceLoggingAspect {

  @Pointcut("@annotation(PerformanceLogging) || @within(PerformanceLogging)")
  public void performanceLogging() {
    // Pointcut for annotation based logging
  }

  @Around("performanceLogging()")
  public Object logAroundAnnotation(ProceedingJoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    PerformanceLogging annotation = getPerformanceLoggingAnnotation(methodSignature);
    String layer = ofNullable(annotation).map(PerformanceLogging::layer).orElse(DEFAULT);
    return logAround(joinPoint, layer);
  }

  @SneakyThrows
  private Object logAround(ProceedingJoinPoint joinPoint, String layer) {
    long start = currentTimeMillis();
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodFullName = className + "." + joinPoint.getSignature().getName();
    try {
      return joinPoint.proceed();
    } finally {
      long stop = currentTimeMillis();
      log.info(LOG_FORMAT, PERFORMANCE_MARKER, layer, methodFullName, "", (stop - start) + " ms");
    }
  }

  private PerformanceLogging getPerformanceLoggingAnnotation(MethodSignature methodSignature) {
    PerformanceLogging annotation =
        getMergedAnnotation(methodSignature.getMethod(), PerformanceLogging.class);
    if (annotation == null) {
      annotation =
          getMergedAnnotation(
              methodSignature.getMethod().getDeclaringClass(), PerformanceLogging.class);
    }
    return annotation;
  }
}
