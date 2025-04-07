package lv.gennadyyonov.hellookta.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.springframework.core.annotation.AnnotatedElementUtils.isAnnotated;

@Slf4j
@UtilityClass
public class LoggingUtils {

  private static final String LOGGING_EXCEPTION = "Object logging throws an exception";
  private static final String SEPARATOR = ",";
  private static final String NULL = "<null>";
  private static final String LOGGING_EXCLUSION_ARG = "<not loggable>";
  private static final String DEFAULT_ARG_NAME = "arg";

  // <marker> | <layer> | <method> | <parameters> | <execution_time>
  public static final String LOG_FORMAT = "{} | {} | {} | {} | {}";
  public static final String PERFORMANCE_MARKER = "Performance Measurements";
  public static final String DEFAULT = "DEFAULT";

  private static final ThreadLocal<JsonMapper> objectMapper =
      ThreadLocal.withInitial(
          () ->
              JsonMapper.builder()
                  .deactivateDefaultTyping()
                  .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                  .build());

  public static Map<String, Object> getLoggableArgs(JoinPoint joinPoint) {
    Map<String, Object> loggableArgs = new LinkedHashMap<>();
    Object[] args = joinPoint.getArgs();
    if (isEmpty(args)) {
      return loggableArgs;
    }
    Signature signature = joinPoint.getStaticPart().getSignature();
    if (signature instanceof MethodSignature methodSignature) {
      Method method = methodSignature.getMethod();
      Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      Parameter[] parameters = method.getParameters();
      range(0, parameterAnnotations.length)
          .forEach(
              index -> {
                Annotation[] annotations = parameterAnnotations[index];
                boolean loggingExclusion =
                    Stream.of(annotations)
                        .anyMatch(annotation -> annotation instanceof LoggingExclusion);
                String name = parameters[index].getName();
                loggableArgs.put(name, loggingExclusion ? LOGGING_EXCLUSION_ARG : args[index]);
              });
      return loggableArgs;
    }
    range(0, args.length).forEach(index -> loggableArgs.put(DEFAULT_ARG_NAME + index, args[index]));
    return loggableArgs;
  }

  public static String composeArguments(Map<String, Object> arguments) {
    if (arguments == null) {
      return NULL;
    }
    return arguments.entrySet().stream()
        .map(
            entry -> {
              String value = getValueAsString(entry.getValue());
              return entry.getKey() + "=" + value;
            })
        .collect(joining(SEPARATOR));
  }

  public static String composeResult(JoinPoint joinPoint, Object result) {
    Signature signature = joinPoint.getStaticPart().getSignature();
    if (signature instanceof MethodSignature methodSignature) {
      if (isAnnotated(methodSignature.getMethod(), LoggingExclusion.class)) {
        return LOGGING_EXCLUSION_ARG;
      }
    }
    return getValueAsString(result);
  }

  private static String getValueAsString(Object value) {
    try {
      return (value == null)
          ? NULL
          : objectMapper.get().writerWithView(View.LoggingView.class).writeValueAsString(value);
    } catch (JsonProcessingException ex) {
      log.warn(LOGGING_EXCEPTION, ex);
      return LOGGING_EXCEPTION;
    }
  }
}
