package cn.daliu.service.impl;

import cn.daliu.dao.ProvinceDao;
import cn.daliu.dao.impl.ProvinceDaoImpl;
import cn.daliu.domain.Province;
import cn.daliu.jedis.JedisPoolUtils;
import cn.daliu.service.ProvinceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.List;

public class ProvinceServiceImpl implements ProvinceService {
    private ProvinceDao dao = new ProvinceDaoImpl();

    @Override
    public List<Province> findAll() {
        return dao.findAll();
    }

    // 使用redis缓存需要注意的是:
    // 如果对数据库的表执行了增删改的相关操作, 需要将redis缓存相关数据清空然后再次存入
    @Override
    public String findAllJson() {
        // 先从redis中查询数据
        Jedis jedis = JedisPoolUtils.getJedis();
        String provinceJson = jedis.get("province");
        if (provinceJson == null || provinceJson.length() == 0) {
            System.out.println("redis中无数据, 查询数据库");
            List<Province> provinceList = dao.findAll();
            // 序列化并存入redis
            ObjectMapper mapper = new ObjectMapper();
            try {
                provinceJson = mapper.writeValueAsString(provinceList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // 存入redis
            jedis.set("province", provinceJson);
            jedis.close(); // 归还连接
        } else {
            System.out.println("缓存中有数据, 直接返回");
        }
        return provinceJson;
    }
}
