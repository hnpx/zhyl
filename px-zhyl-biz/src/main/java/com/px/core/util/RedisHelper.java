package com.px.core.util;

import com.pig4cloud.pig.common.core.support.cache.CacheManager;
import com.pig4cloud.pig.common.core.support.cache.CacheUtil;
import com.pig4cloud.pig.common.core.support.cache.InstanceUtil;
import com.pig4cloud.pig.common.core.support.context.ApplicationContextHolder;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

/**
 * @author zhouz
 * @Date 2021/7/7 18:24
 * @Description
 */

public final class RedisHelper implements CacheManager {
    private RedisTemplate<String, Serializable> redisTemplate;
    private Integer EXPIRE;

    public RedisHelper(RedisTemplate<String, Serializable> redisTemplate) {
        this(redisTemplate, 60);
    }

    public RedisHelper(RedisTemplate<String, Serializable> redisTemplate, Integer expire) {
        this.EXPIRE = 600;
        this.redisTemplate = redisTemplate;
        CacheUtil.setCacheManager(this);
        this.EXPIRE = expire;
    }

    public RedisTemplate<String, Serializable> getRedisTemplate() {
        if (this.redisTemplate == null) {
            logger.warn("redisTemplate is null");
            this.redisTemplate = (RedisTemplate)ApplicationContextHolder.getBean("lockRedisTemplate");
        }

        return this.redisTemplate;
    }

    @Override
    public final Object get(String key) {
        return this.getRedisTemplate().boundValueOps(key).get();
    }

    @Override
    public final Object get(String key, Integer expire) {
        this.expire(key, expire);
        return this.getRedisTemplate().boundValueOps(key).get();
    }

    @Override
    public final Object getFire(String key) {
        this.expire(key, this.EXPIRE);
        return this.getRedisTemplate().boundValueOps(key).get();
    }

    @Override
    public final Set<Object> getAll(String pattern) {
        Set<Object> values = InstanceUtil.newHashSet();
        Set<String> keys = this.getRedisTemplate().keys(pattern);
        Iterator var4 = keys.iterator();

        while(var4.hasNext()) {
            Serializable key = (Serializable)var4.next();
            values.add(this.getRedisTemplate().opsForValue().get(key));
        }

        return values;
    }

    @Override
    public final Set<Object> getAll(String pattern, Integer expire) {
        Set<Object> values = InstanceUtil.newHashSet();
        Set<String> keys = this.getRedisTemplate().keys(pattern);
        Iterator var5 = keys.iterator();

        while(var5.hasNext()) {
            Serializable key = (Serializable)var5.next();
            this.expire(key.toString(), expire);
            values.add(this.getRedisTemplate().opsForValue().get(key));
        }

        return values;
    }

    @Override
    public final void set(String key, Serializable value, int seconds) {
        this.getRedisTemplate().boundValueOps(key).set(value);
        this.expire(key, seconds);
    }

    @Override
    public final void set(String key, Serializable value) {
        this.getRedisTemplate().boundValueOps(key).set(value);
        this.expire(key, this.EXPIRE);
    }

    @Override
    public final Boolean exists(String key) {
        return this.getRedisTemplate().hasKey(key);
    }

    @Override
    public final void del(String key) {
        this.getRedisTemplate().delete(key);
    }

    @Override
    public final void delAll(String pattern) {
        this.getRedisTemplate().delete(this.getRedisTemplate().keys(pattern));
    }

    @Override
    public final String type(String key) {
        return this.getRedisTemplate().type(key).getClass().getName();
    }

    @Override
    public final Boolean expire(String key, int seconds) {
        return this.getRedisTemplate().expire(key, (long)seconds, TimeUnit.SECONDS);
    }

    @Override
    public final Boolean expireAt(String key, long unixTime) {
        return this.getRedisTemplate().expireAt(key, new Date(unixTime));
    }

    @Override
    public final Long ttl(String key) {
        return this.getRedisTemplate().getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public final void setrange(String key, long offset, String value) {
        this.getRedisTemplate().boundValueOps(key).set(value, offset);
    }

    @Override
    public final String getrange(String key, long startOffset, long endOffset) {
        return this.getRedisTemplate().boundValueOps(key).get(startOffset, endOffset);
    }

    @Override
    public final Object getSet(String key, Serializable value) {
        return this.getRedisTemplate().boundValueOps(key).getAndSet(value);
    }

    @Override
    public boolean setnx(String key, Serializable value) {
        return this.getRedisTemplate().boundValueOps(key).setIfAbsent(value);
    }

    private byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        RedisSerializer keySerializer = this.getRedisTemplate().getKeySerializer();
        if (keySerializer == null && key instanceof byte[]) {
            return (byte[])((byte[])key);
        } else {
            Assert.notNull(keySerializer, "non null keySerializer required");
            return keySerializer.serialize(key);
        }
    }

    private byte[] rawValue(Object value) {
        RedisSerializer valueSerializer = this.getRedisTemplate().getValueSerializer();
        if (valueSerializer == null && value instanceof byte[]) {
            return (byte[])((byte[])value);
        } else {
            Assert.notNull(valueSerializer, "non null valueSerializer required");
            return valueSerializer.serialize(value);
        }
    }

    @Override
    public boolean lock(final String key, final String requestId, final long seconds) {
        return (Boolean)this.getRedisTemplate().execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.set(RedisHelper.this.rawKey(key), RedisHelper.this.rawValue(requestId), Expiration.seconds(seconds), SetOption.ifAbsent());
            }
        });
    }

    @Override
    public boolean unlock(String key, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return (Boolean)this.getRedisTemplate().execute(new DefaultRedisScript(script, Boolean.class), InstanceUtil.newArrayList(new String[]{key}), new Object[]{requestId});
    }

    @Override
    public void hset(String key, Serializable field, Serializable value) {
        this.getRedisTemplate().boundHashOps(key).put(field, value);
    }

    @Override
    public Object hget(String key, Serializable field) {
        return this.getRedisTemplate().boundHashOps(key).get(field);
    }

    @Override
    public void hdel(String key, Serializable field) {
        this.getRedisTemplate().boundHashOps(key).delete(new Object[]{field});
    }

    @Override
    public Long incr(String key) {
        return this.getRedisTemplate().boundValueOps(key).increment(1L);
    }

    @Override
    public Long incr(String key, int seconds) {
        Long incr = this.getRedisTemplate().boundValueOps(key).increment(1L);
        this.expire(key, seconds);
        return incr;
    }
}