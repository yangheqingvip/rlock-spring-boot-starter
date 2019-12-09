package dawn.dlock.rlock.core.compensator;

import dawn.dlock.rlock.core.lock.LockContext;

/**
 * 加锁失败补偿器
 *
 * @author HEBO
 */
public interface LockFailedCompensator {

	/**
	 * 加锁失败会调用该方法:
	 * 该方法如果返回true, 则表示补偿成功继续执行方法, 否则就抛出异常
	 *
	 * @param lockContext 锁信息
	 * @return
	 * @throws Throwable
	 */
	Boolean compensate(LockContext lockContext) throws Throwable;

}
