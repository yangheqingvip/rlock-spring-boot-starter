package dawn.dlock.rlock.annotation;

import dawn.dlock.rlock.core.compensator.LockFailedCompensator;
import dawn.dlock.rlock.core.compensator.RetryLockCompensator;
import dawn.dlock.rlock.core.lock.name.DefaultNameBuilder;
import dawn.dlock.rlock.core.lock.name.LockNameBuilder;

/**
 * 策略注解
 *
 * @author v_hebo
 */
public @interface LockStrategy {

	/**
	 * 锁名称构造策略
	 *
	 * @return 锁名称构造策略
	 */
	Class<? extends LockNameBuilder> builder() default DefaultNameBuilder.class;

	/**
	 * 失败补偿策略
	 *
	 * @return 补偿策略
	 */
	Class<? extends LockFailedCompensator> compensator() default RetryLockCompensator.class;

	/**
	 * 失败重试补偿: 重试次数
	 *
	 * @return 重试次数
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator;
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator#DEFAULT_RETRY;
	 */
	int retry() default RetryLockCompensator.DEFAULT_RETRY;


	/**
	 * 失败重试补偿: 重试等待时长
	 *
	 * @return 重试次数
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator;
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator#DEFAULT_RETRY_WAIT_TIME;
	 */
	long retryWaitTime() default RetryLockCompensator.DEFAULT_RETRY_WAIT_TIME;

}
