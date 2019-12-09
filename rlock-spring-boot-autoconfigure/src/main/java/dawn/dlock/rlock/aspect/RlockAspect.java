package dawn.dlock.rlock.aspect;

import dawn.dlock.rlock.annotation.Rlock;
import dawn.dlock.rlock.config.RlockConfig;
import dawn.dlock.rlock.core.LockInvoker;
import dawn.dlock.rlock.core.compensator.LockFailedCompensator;
import dawn.dlock.rlock.core.lock.LockContext;
import dawn.dlock.rlock.core.lock.name.LockNameBuilder;
import dawn.dlock.rlock.core.lock.name.LockNameContext;
import dawn.dlock.rlock.exception.RlockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * redis lock aspect
 * <p>
 * 核心类: 通过aop加锁和释放锁
 *
 * @author HEBO
 */
@Slf4j
@Order(0)
@Aspect
@Component
public class RlockAspect {

	@Resource
	private RlockConfig rlockConfig;

	@Autowired
	private LockInvoker lockInvoker;

	@Autowired
	private List<LockFailedCompensator> failedCompensates;

	@Autowired
	private List<LockNameBuilder> lockNameBuilders;

	@Pointcut("@annotation(dawn.dlock.rlock.annotation.Rlock)")
	public void lockPointcut() {
	}

	@Around("lockPointcut()")
	public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			// 获取锁信息
			LockContext context = loadLockContext(joinPoint);

			// 加锁
			Boolean locked = lockInvoker.lock(context);

			// 加锁异常, 执行失败补偿
			if (BooleanUtils.isNotTrue(locked)) {
				locked = doFailed(context);
			}

			// 失败补偿执行返回false, 直接抛出异常
			if (BooleanUtils.isNotTrue(locked)) {
				throw new RlockException(String.format("[Rlock] lock failed, lockName:%s", context.getKey()));
			}
			// 补偿执行返回true, 继续执行方法
			log.info("[Rlock] locked lockName:{}", context.getKey());
			return joinPoint.proceed();
		} finally {
//			if (context.getLock() != null) {
//				context.getLock().unlock();
//				log.info("[ZkLock] released lock lockName:{}", context.getLockName());
//			}
		}
	}

	/**
	 * 解析锁相关信息
	 *
	 * @param point 连接点
	 * @return 锁信息
	 * @throws RlockException
	 */
	private LockContext loadLockContext(ProceedingJoinPoint point) throws RlockException, NoSuchMethodException {
		// 获取连接的方法对象
		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		Method method = point.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());

		// 必须通过Spring的AnnotationUtils获取ZkLock, 否则@AlisFor无效
		Rlock rlock = AnnotationUtils.findAnnotation(method, Rlock.class);

		// 获取锁key
		String key = buildKey(rlock, method, point.getArgs());

		// 构建锁信息
		return LockContext.builder()
				.key(key)
				.locked(false)
				.rlock(rlock)
				.build();
	}

	private String buildKey(Rlock rlock, Method method, Object... args) throws RlockException {
		try {
			Class<? extends LockNameBuilder> builderClass = rlock.lockNameBuilder();
			for (LockNameBuilder nameBuilder : lockNameBuilders) {
				if (nameBuilder.getClass().equals(builderClass)) {
					return nameBuilder.build(LockNameContext.builder()
							.config(rlockConfig)
							.method(method)
							.params(args)
							.key(rlock.key())
							.build());
				}
			}
			log.warn("Rlock nonsupport LockNameBuilder: {}, please check or change your config", rlock.lockNameBuilder().getName());
		} catch (Throwable throwable) {
			log.warn("Rlock build key exception, LockNameBuilder: {}", rlock.lockNameBuilder().getName(), throwable);
		}
		throw new RlockException(String.format("[Rlock] buildKey failed, %s#%s, rlock key: %s", method.getDeclaringClass().getName(), method.getName(), rlock.key()));
	}

	/**
	 * 执行失败补偿
	 *
	 * @param context 锁信息
	 * @return 执行结果
	 * @throws Throwable
	 */
	private Boolean doFailed(LockContext context) throws Throwable {
		try {
			Class<? extends LockFailedCompensator> compensator = context.getRlock().compensator();
			for (LockFailedCompensator failedCompensator : this.failedCompensates) {
				if (failedCompensator.getClass().equals(compensator)) {
					return failedCompensator.compensate(context);
				}
			}
			log.warn("Rlock nonsupport LockFailedCompensator: {}, please check or change your config", context.getRlock().compensator().getName());
		} catch (Exception e) {
			log.warn(String.format("Rlock invoke  LockFailedCompensator: %s exception", context.getRlock().compensator().getName()), e);
		}
		return Boolean.FALSE;
	}

}
