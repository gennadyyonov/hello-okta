package lv.gennadyyonov.hellookta.bff.config.graphql;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static lv.gennadyyonov.hellookta.bff.config.graphql.AsyncContextUtils.createAsyncContext;
import static lv.gennadyyonov.hellookta.bff.config.graphql.AsyncContextUtils.populate;

@RequiredArgsConstructor
final class ContextCopyingDelegateExecutorService implements ExecutorService {

  private final AsyncContext asyncContext;

  @Delegate(excludes = Executor.class)
  private final ExecutorService delegate;

  @Override
  public void execute(Runnable command) {
    Runnable contextCopyingRunnable = new ContextCopyingRunnable(command, asyncContext);
    Runnable runnable =
        new DelegatingSecurityContextRunnable(
            contextCopyingRunnable, asyncContext.getSecurityContext());
    delegate.execute(runnable);
  }

  @RequiredArgsConstructor
  private static final class ContextCopyingRunnable implements Runnable {

    private final Runnable delegate;
    private final AsyncContext delegateAsyncContext;

    @Override
    public void run() {
      AsyncContext originalAsyncContext = createAsyncContext();
      try {
        populate(delegateAsyncContext);
        delegate.run();
      } finally {
        populate(originalAsyncContext);
      }
    }
  }
}
