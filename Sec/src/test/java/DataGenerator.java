import Core.util.JacksonUtil;
import Core.util.JwtHelper;
import Sec.SApp;
import Sec.dao.SDao;
import Sec.model.po.GoodsPo;
import Sec.model.po.OrderPo;
import Sec.model.vo.GoodsInfoVo;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SApp.class)
@AutoConfigureMockMvc
@Transactional
public class DataGenerator {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RedisTemplate<String, Serializable> redis;

    @Test
    public void createToken(){
        for (long id = 1L; id <= 2; id++) {
            String token = new JwtHelper().createToken(id, -1L, 36000);
            System.out.println(token);
        }
    }

    @Test
    public void buySomething() throws JSONException {
        String token = new JwtHelper().createToken(1L, -1L, 36000);
        GoodsInfoVo testVo = new GoodsInfoVo();
        testVo.setId(1L);
        String responseString = null;
        String postURL = "/orders";
        for (int i = 0; i < 100; i++){
            try {
                testVo.setAmount(10);
                String testVoJson = JacksonUtil.toJson(testVo);
                responseString = this.mvc.perform(post(postURL).header("authorization", token)
                        .contentType("application/json;charset=UTF-8").content(testVoJson))
                        .andExpect(status().isCreated())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andReturn().getResponse().getContentAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject response = new JSONObject();
            response.put("errno", 0);
            response.put("errmsg", "成功");
            String expectedResponse = response.toString();
            try {
                JSONAssert.assertEquals(expectedResponse, responseString,true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void loadGoodsIntoRedis(){
        for (int i = 0; i < 10; i++){
//            String key = "Goods_" + "1";
            String key = "Goods_" + i + "_1";
            redis.opsForValue().set(key, 1000);
        }
    }

    @Test
    public void checkStores(){
        for (int i = 0; i < 10; i++){
            String key = "Goods_" + i + "_1";
            System.out.println(redis.opsForValue().get(key));
        }
    }
}
