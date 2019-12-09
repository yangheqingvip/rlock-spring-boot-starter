package dawn.dlock.rlock.core;

import dawn.dlock.rlock.util.UUIDUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Collections;

/**
 * redis lock 执行组件
 *
 * @author HEBO
 */
@Slf4j
@AllArgsConstructor
public class LockInvoker {

	/**
	 * redis模板
	 */
	private StringRedisTemplate redisTemplate;

	/**
	 * 解锁脚本
	 */
	private RedisScript<Long> unlockScript;

	/**
	 * 保存当前调用线程的Redis setnx value
	 */
	private ThreadLocal<String> redisValue;

	/**
	 * 初始化构建redis的解锁脚本
	 */
	public void init() {
		StringBuilder builder = new StringBuilder();
		builder.append("if redis.call(\"get\", KEYS[1]) == ARGV[1] then ");
		builder.append("    return redis.call(\"del\", KEYS[1]) ");
		builder.append("else ");
		builder.append("    return 0 ");
		builder.append("end");

		String unlockLua = builder.toString();
		unlockScript = new DefaultRedisScript<>(unlockLua, Long.class);
	}

	/**
	 * 加锁, 使用SET_IF_ABSENT只添加redis key, 保证操作的原子性
	 *
	 * @param key    redis key
	 * @param expire redis key有效时间
	 * @return 是否成功
	 */
	public Boolean lock(String key, Long expire) {
		String uuid = UUIDUtils.random();
		redisValue.set(uuid);
		return setToRedis(key, uuid, expire);
	}

	/**
	 * 解锁
	 * 使用lua脚本删除key,
	 * 如果key不存在或者value和当前线程的value不一致则删除失败
	 *
	 * @param key redis key
	 * @return 是否成功
	 */
	public Boolean unlock(String key) {
		// 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
		try {
			// 使用lua脚本删除redis中匹配value的key，可以避免方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
			Long result = redisTemplate.execute(unlockScript, Collections.singletonList(key), redisValue.get());

			// 移除redisThreadValue, 防止内存溢出, 无法GC问题
			redisValue.remove();

			return result != null && result > 0;
		} catch (Exception e) {
			log.error("Rlock release lock occur an exception", e);
		}
		return Boolean.FALSE;
	}


	/**
	 * redis新增key
	 *
	 * @param key    redis key
	 * @param value  redis key对应的value
	 * @param expire key有效时间
	 * @return 是否成功
	 */
	private Boolean setToRedis(String key, String value, Long expire) {
		return redisTemplate.execute((RedisCallback<Boolean>) connection ->
				connection.set(key.getBytes(), value.getBytes(), Expiration.milliseconds(expire), RedisStringCommands.SetOption.SET_IF_ABSENT));
	}
}
