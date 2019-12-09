package dawn.dlock.rlock.annotation;

import dawn.dlock.rlock.core.compensator.LockFailedCompensator;
import dawn.dlock.rlock.core.compensator.RetryLockCompensator;
import dawn.dlock.rlock.core.lock.name.DefaultNameBuilder;
import dawn.dlock.rlock.core.lock.name.LockNameBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * redis lock 注解
 *
 * @author HEBO
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@LockStrategy
public @interface Rlock {

	/**
	 * key 锁名称
	 *
	 * @return redis key
	 */
	@AliasFor("key")
	String value() default StringUtils.EMPTY;

	/**
	 * key 锁名称
	 *
	 * @return redis key
	 */
	@AliasFor("value")
	String key() default StringUtils.EMPTY;

	/**
	 * 有效时长
	 *
	 * @return
	 */
	long expire() default 60000;

	/**
	 * 时间单位
	 * key有效时长都是使用
	 *
	 * @return
	 */
	TimeUnit timeunit() default TimeUnit.MILLISECONDS;


	/**
	 * 锁名称构造策略
	 *
	 * @return 锁名称构造策略
	 */
	@AliasFor(annotation = LockStrategy.class, attribute = "builder")
	Class<? extends LockNameBuilder> lockNameBuilder() default DefaultNameBuilder.class;

	/**
	 * 失败补偿策略
	 *
	 * @return 补偿策略
	 */
	@AliasFor(annotation = LockStrategy.class, attribute = "compensator")
	Class<? extends LockFailedCompensator> compensator() default RetryLockCompensator.class;

	/**
	 * 失败重试补偿: 重试次数
	 *
	 * @return 重试次数
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator;
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator#DEFAULT_RETRY;
	 */
	@AliasFor(annotation = LockStrategy.class, attribute = "retry")
	int retry() default RetryLockCompensator.DEFAULT_RETRY;

	/**
	 * 失败重试补偿: 重试等待时长
	 *
	 * @return 重试次数
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator;
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator#DEFAULT_RETRY_WAIT_TIME;
	 */
	@AliasFor(annotation = LockStrategy.class, attribute = "retryWaitTime")
	long retryWaitTime() default RetryLockCompensator.DEFAULT_RETRY_WAIT_TIME;

}
