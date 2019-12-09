package dawn.dlock.rlock.core.lock;

import dawn.dlock.rlock.annotation.Rlock;
import dawn.dlock.rlock.core.compensator.LockFailedCompensator;
import dawn.dlock.rlock.core.lock.name.LockNameBuilder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 锁上下文
 *
 * @author HEBO
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LockContext {

	/**
	 * 锁名称
	 */
	private String key;

	/**
	 * 有效时长
	 *
	 * @return
	 */
	private Long expire;

	/**
	 * 时间单位
	 * key有效时长都是使用
	 *
	 * @return
	 */
	private TimeUnit timeunit;


	/**
	 * 锁名称构造策略
	 *
	 * @return 锁名称构造策略
	 */
	private Class<? extends LockNameBuilder> lockNameBuilder;

	/**
	 * 失败补偿策略
	 *
	 * @return 补偿策略
	 */
	private Class<? extends LockFailedCompensator> compensator;

	/**
	 * 失败重试补偿: 重试次数
	 *
	 * @return 重试次数
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator;
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator#DEFAULT_RETRY;
	 */
	private Integer retry;

	/**
	 * 失败重试补偿: 重试等待时长
	 *
	 * @return 重试次数
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator;
	 * @see dawn.dlock.rlock.core.compensator.RetryLockCompensator#DEFAULT_RETRY_WAIT_TIME;
	 */
	private Long retryWaitTime;

	/**
	 * 构造器
	 *
	 * @return
	 */
	public static LockContextBuilder builder() {
		return new LockContextBuilder();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class LockContextBuilder {

		/**
		 * 锁名称
		 */
		private String key;

		/**
		 * 注解对象
		 */
		private Rlock rlock;

		public LockContextBuilder key(String key) {
			this.key = key;
			return this;
		}

		public LockContextBuilder rlock(Rlock rlock) {
			this.rlock = rlock;
			return this;
		}

		public LockContext build() {
			LockContext context = new LockContext();
			context.setKey(key);
			context.setExpire(rlock.expire());
			context.setTimeunit(rlock.timeunit());
			context.setRetry(rlock.retry());
			context.setRetryWaitTime(rlock.retryWaitTime());
			context.setCompensator(rlock.compensator());
			context.setLockNameBuilder(rlock.lockNameBuilder());
			return context;
		}

	}

}
