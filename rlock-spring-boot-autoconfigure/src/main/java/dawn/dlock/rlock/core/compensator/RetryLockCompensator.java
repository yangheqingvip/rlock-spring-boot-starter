package dawn.dlock.rlock.core.compensator;

import dawn.dlock.rlock.core.LockInvoker;
import dawn.dlock.rlock.core.lock.LockContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 失败重试补偿器
 *
 * @author HEBO
 */
@Slf4j
public class RetryLockCompensator implements LockFailedCompensator {

	@Autowired
	private LockInvoker helper;

	/**
	 * 默认重试次数
	 */
	public static final int DEFAULT_RETRY = 3;

	/**
	 * 默认重试等待时间
	 */
	public static final Long DEFAULT_RETRY_WAIT_TIME = 1L;

	@Override
	public Boolean compensate(LockContext context) throws Throwable {
		for (int i = 0; i < context.getRlock().retry(); i++) {
			Thread.sleep(TimeUnit.MILLISECONDS.convert(context.getRlock().retryWaitTime(), context.getRlock().timeunit()));
			if (helper.lock(context.getKey(), context.getRlock().expire())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

}
