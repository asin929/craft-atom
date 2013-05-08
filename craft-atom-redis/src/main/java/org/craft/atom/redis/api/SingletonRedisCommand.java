package org.craft.atom.redis.api;

/**
 * The atomic commands supported by singleton Redis.
 * 
 * @author mindwind
 * @version 1.0, May 4, 2013
 */
public interface SingletonRedisCommand extends RedisCommand {
	
	
	long del(String... keys);
	long del(byte[]... keys);
	
	
}
