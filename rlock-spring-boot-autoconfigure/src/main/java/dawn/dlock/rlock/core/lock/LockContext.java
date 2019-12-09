package dawn.dlock.rlock.core.lock;

import dawn.dlock.rlock.annotation.Rlock;
import lombok.Builder;
import lombok.Data;

/**
 * 锁上下文
 *
 * @author HEBO
 */
@Data
@Builder
public class LockContext {

	/**
	 * 锁名称
	 */
	private String key;

	/**
	 * 是否已经锁定
	 */
	private boolean locked;

	/**
	 * 注解对象
	 */
	private Rlock rlock;

}
