package dawn.dlock.rlock.core.lock.name;

/**
 * 锁名称构建器
 *
 * @author HEBO
 */
public interface LockNameBuilder {

	String DELIMITER = ":";

	/**
	 * 构建锁名称
	 *
	 * @param context 锁上下文
	 * @return
	 * @throws Throwable
	 */
	String build(LockNameContext context) throws Throwable;

}
