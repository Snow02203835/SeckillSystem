package Sec.dao;

import Sec.model.bo.*;
import Sec.model.po.GoodsPo;
import Core.util.ResponseCode;
import Sec.mapper.GoodsPoMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Random;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author snow create 2021/05/07 22:12
 */
@Repository
public class SDao {
    private final Integer value = 1;
    private final Integer missStore = -3835;
    private final Random random = new Random();
    @Autowired
    private GoodsPoMapper goodsMapper;
//    @Resource
//    private SnowFlake snowFlake;
//    @Resource
//    private CuratorFramework curatorFramework;
    @Autowired
    private RedisTemplate<String, Serializable> redis;

    /**
     * 从数据库中载入数据至redis
     * @author snow create 2021/05/27 16:35
     * @param id 商品id
     */
    public void findGoodsForDatabaseById(Long id){
        try {
            GoodsPo goodsPo = goodsMapper.selectByPrimaryKey(id);
            String keys = "Goods_" + id;
            Goods goods = new Goods(goodsPo);
            if(goods.getStore() == null){
                goods.setStore(missStore);
            }
//            System.out.println("keys: " + keys + ", value: " + goods.getStore());
            redis.opsForValue().set(keys, goods.getStore());
            redis.expire(keys, 28800+random.nextInt(1000), TimeUnit.SECONDS);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 采用zookeeper加锁，redis扣库存
     * @author snow create 2021/06/02 15:42
     */
//    public ResponseCode decreaseGoodsStore(Long goodsId, Integer amount){
//        InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(curatorFramework, "/seckill/" + snowFlake.nextId());
//        ResponseCode code = ResponseCode.OK;
//        String key = "Goods_" + goodsId;
//        try {
//            boolean hasLock = lock.acquire(60, TimeUnit.SECONDS);
//            if (hasLock) {
//                Integer store = (Integer)redis.opsForValue().get(key);
//                if (store != null){
//                    if(store >= amount) {
//                        redis.opsForValue().decrement(key, amount);
//                    }
//                    else {
//                        code = ResponseCode.GOODS_STORE_NOT_ENOUGH;
//                    }
//                }
//                else {
//                    code = ResponseCode.GOODS_NOT_EXIST;
//                }
//            }
//        }
//        catch (Exception e){
//            try {
//                boolean hasLock = lock.acquire(60, TimeUnit.SECONDS);
//                if (hasLock) {
//                    redis.opsForValue().increment(key, amount);
//                }
//                code = ResponseCode.INTERNAL_SERVER_ERR;
//            }
//            catch (Exception ex){
//                e.printStackTrace();
//            }
//            finally {
//                try {
//                    lock.release();
//                }
//                catch (Exception es){
//                    es.printStackTrace();
//                }
//            }
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                lock.release();
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return code;
//    }

    /**
     * 从Redis中扣除库存
     * @author snow create 2021/05/27 17:18
     * @param goodsId 商品id
     * @param amount 购买数量
     * @return 操作结果
     */
    public ResponseCode decreaseGoodsStoreFromRedis(Long goodsId, Integer amount){
        ResponseCode code = ResponseCode.OK;
        int randomNum = this.random.nextInt(10);
        String key = "Goods_" + randomNum + "_" + goodsId;
        String lockKey = "Lock_Goods_" + randomNum + "_" + goodsId;
//        String key = "Goods_" + goodsId;
//        String lockKey = "Lock_Goods_" + goodsId;
        try {
            Integer store = (Integer)redis.opsForValue().get(key);
            if (store != null) {
                if (store >= amount) {
                    while (true) {
                        Boolean hasLock = redis.opsForValue().setIfAbsent(lockKey, value, 1, TimeUnit.SECONDS);
                        if (hasLock != null && hasLock) {
//                    System.out.println("Get key!");
                            store = (Integer) redis.opsForValue().get(key);
//                    System.out.println("GoodsId = " + goodsId + ", store = " + store);
                            if (store != null) {
                                if (store >= amount) {
                                    redis.opsForValue().decrement(key, amount);
                                } else {
                                    code = ResponseCode.GOODS_STORE_NOT_ENOUGH;
                                }
                            } else {
                                code = ResponseCode.GOODS_NOT_EXIST;
                            }
                            break;
                        }
                        else{
                            Thread.sleep(100 + random.nextInt(50));
                        }
                    }
                }
                else{
                    code = ResponseCode.GOODS_STORE_NOT_ENOUGH;
                }
            }
            else{
                code = ResponseCode.GOODS_NOT_EXIST;
            }
        }
        catch (Exception e){
            code = ResponseCode.INTERNAL_SERVER_ERR;
            e.printStackTrace();
        }
        finally {
//            System.out.println("Ready to release key!");
            try {
                redis.delete(lockKey);
            }
            catch (Exception e){
                e.printStackTrace();
            }
//            System.out.println("Key has been released!");
        }
//        System.out.println(code.getMessage());
        return code;
    }

    /**
     * 从数据库中扣除库存
     * @author snow create 2021/05/18 17:38
     * @param goodsId 商品id
     * @param amount 购买数量
     * @return 操作结果
     */
    public ResponseCode decreaseGoodsStoreFromDataBase(Long goodsId, Integer amount){
        ResponseCode code = ResponseCode.OK;
        try {
            GoodsPo goodsPo = goodsMapper.selectByPrimaryKey(goodsId);
            if (goodsPo != null){
                if (goodsPo.getStore() >= amount){
                    synchronized (value){
                        goodsPo = goodsMapper.selectByPrimaryKey(goodsId);
                        if (goodsPo.getStore() < amount){
                            code = ResponseCode.GOODS_STORE_NOT_ENOUGH;
                        }
                        else {
                            Goods goods = new Goods(goodsPo);
                            goods.setStore(goods.getStore() - amount);
                            goods.setGmtModified(LocalDateTime.now());
                            goodsMapper.updateByPrimaryKeySelective(goods.createPo());
                        }
                    }
                }
                else {
                    code = ResponseCode.GOODS_STORE_NOT_ENOUGH;
                }
            }
            else {
                code = ResponseCode.GOODS_NOT_EXIST;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return code;
    }

}
