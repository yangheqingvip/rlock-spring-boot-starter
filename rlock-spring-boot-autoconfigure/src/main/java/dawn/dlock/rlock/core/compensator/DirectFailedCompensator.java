package dawn.dlock.rlock.core.compensator;

import dawn.dlock.rlock.core.lock.LockContext;

/**
 * 加锁失败补偿器: 直接返回失败
 *
 * @author HEBO
 */
public class DirectFailedCompensator implements LockFailedCompensator {
	@Override
	public Boolean compensate(LockContext lockContext) throws Throwable {
		return false;
	}
}
