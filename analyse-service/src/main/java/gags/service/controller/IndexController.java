package gags.service.controller;

import gags.service.dao.ChickenMapDAO;
import gags.service.dao.IndexQueryDAO;
import gags.service.entity.CoordMapEntity;
import gags.service.util.DateUtil;
import gags.service.util.ESDataUtil;
import gags.service.util.JedisHolder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Created by jiahang Lee on 2017/7/19.
 */
@RestController
public class IndexController {

    @Autowired
    private IndexQueryDAO indexQueryDAO;
    @Autowired
    private ChickenMapDAO chickenMapDAO;

    @Autowired
    private JedisHolder jedisHolder;

    private Integer lastDateHours = 0;

    @RequestMapping(path = "/single")
    public Map indexQuery(@RequestParam String fields) {

        Map result = new HashMap();
        int activeUser = this.getCurrentRedisKeys("*all");
        Object networkUser = this.getNetworkUser("t_china_overview_common_day","0");

        result.put("networkUser", networkUser);
        result.put("activeUser", activeUser);

        return result;
    }

    /**
     * 从ES中获取数据
     * @param tableName
     * @param areaName
     * @return
     */
    public static Object getNetworkUser(String tableName, String areaName) {
        String period = "day";
        Object networkUser = 0;
//        long yesterday = 1506787200L;
        long yesterday = DateUtil.getYester2day(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String startDate = dateFormat.format(yesterday * 1000);

        String kpiStartTime = DateUtil.getKpiTimeES(period, startDate + "0");
        List<Map<String, Object>> data = new ArrayList();
        List<String> newKpis = new ArrayList();
        newKpis.add("network_user");

        String[] indies = DateUtil.convertESDate(kpiStartTime, null, period);

        QueryBuilder clqb = boolQuery()
                .must(termQuery("area_name", areaName))
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));

        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, clqb, period);
        if (data.size() > 0) {
            networkUser = data.get(0).get("network_user");
        }
        return networkUser;
    }

    /**
     * 从redis中获取即时数据
     * @param pattern
     * @return
     */
    public int getCurrentRedisKeys(String pattern) {
        String key = null;
        int activeUser = 0;
        try (ShardedJedis shardedJedis = jedisHolder.getShardedJedisPool().getResource()) {

            for (Jedis jedis : shardedJedis.getAllShards()) {
                Set<String> keys = jedis.keys(pattern);
                Iterator<String> it = keys.iterator();
                List<String> keysList = new ArrayList();
                while (it.hasNext()) {
                    key = it.next();
                    keysList.add(key);
                    System.out.println("--------------rediskey: " + key + "----------------");
                }
                if (keysList.size() > 1) {
                    long firstKey = Long.parseLong(keysList.get(0).split("_")[0]);
                    long lastKey = Long.parseLong(keysList.get(1).split("_")[0]);
                    key = firstKey > lastKey ? keysList.get(1) : keysList.get(0);
                }
                if (key != null) {
                    activeUser = jedis.hkeys(key).size();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return activeUser;
    }



    @RequestMapping(path = "/coordMapList")
    public Map CoordMapQuery() {
        Map result = new HashMap();
        List list = new ArrayList();

        Object hunanNetworkUser = this.getNetworkUser("t_province_overview_common_day","1010");
        Object hunbeiNetworkUser = this.getNetworkUser("t_province_overview_common_day","1030");
        Object hebeiNetworkUser = this.getNetworkUser("t_province_overview_common_day","1060");
        Object guangdNetworkUser = this.getNetworkUser("t_province_overview_common_day","1050");

        list.add(new CoordMapEntity("湖南",hunanNetworkUser.toString()));
        list.add(new CoordMapEntity("湖北",hunbeiNetworkUser.toString()));
        list.add(new CoordMapEntity("河北",hebeiNetworkUser.toString()));
        list.add(new CoordMapEntity("广东",guangdNetworkUser.toString()));


       /* list.add(new CoordMapEntity("承德",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("唐山",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("承德",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("沧州",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("秦皇岛",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("威海",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("郑州",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("洛阳",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("常德",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("湘潭",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("岳阳",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("湖南",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("合肥",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("武汉",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("湖北",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("南京",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("安阳",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("开封",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("济南",String.valueOf(Math.random() * 100)));
        list.add(new CoordMapEntity("邵阳",String.valueOf(Math.random() * 100)));*/

        result.put("coordMapData",list);
        return result;
    }



    @RequestMapping(path = "/kpiList")
    public Map KpiQueryList() {
        Map result = new HashMap();
        Map kpiData = new HashMap();

        List dvbList = new ArrayList();
        List replayList = new ArrayList();
        List tsList = new ArrayList();
        List vodList = new ArrayList();
        List ottList = new ArrayList();
        List appstoreList = new ArrayList();
        List educationList = new ArrayList();
        List communityList = new ArrayList();
        List lifeProductList = new ArrayList();
        List gameList = new ArrayList();
        List ecomList = new ArrayList();

        try(ShardedJedis shardedJedis = jedisHolder.getShardedJedisPool().getResource()) {

            // 获取当前日期
            Date currentDate = DateUtil.getGlobalDate();
            currentDate.setHours(currentDate.getHours());
            // 获取前一天日期
            Long startDate = DateUtil.getYesterday(currentDate);
            Date endDate = new Date(startDate * 1000);

            System.out.println(System.currentTimeMillis());

            for (int i = 0; i < 23; i++) {
                if (i != 0) {
                    endDate.setHours(endDate.getHours() + 1);
                }
                Long dateKey = DateUtil.getCurrentDay(endDate);
                System.out.println(dateKey);

                String dvbKey = dateKey + "_2";
                String tsKey = dateKey + "_3";
                String replayKey = dateKey + "_4";
                String ottKey = dateKey + "_6";
                String vodKey = dateKey + "_5";
                String appstoreKey = dateKey + "_9";
                String educationKey = dateKey + "_10";
                String communityKey = dateKey + "_11";
                String gameKey = dateKey + "_20";
                String ecomKey = dateKey + "_15";
                // 获取老数据
                int dvbActiveUser = shardedJedis.hkeys(dvbKey).size();
                dvbList.add(dvbActiveUser);
                int ottActiveUser = shardedJedis.hkeys(ottKey).size();
                ottList.add(ottActiveUser);
                int replayActiveUser = shardedJedis.hkeys(replayKey).size();
                replayList.add(replayActiveUser);
                int tsActiveUser = shardedJedis.hkeys(tsKey).size();
                tsList.add(tsActiveUser);
                int vodUser = shardedJedis.hkeys(vodKey).size();
                vodList.add(vodUser);
                int appstoreActiveUser = shardedJedis.hkeys(appstoreKey).size();
                appstoreList.add(appstoreActiveUser);
                int educationActiveUser = shardedJedis.hkeys(educationKey).size();
                educationList.add(educationActiveUser);
                int communityActiveUser = shardedJedis.hkeys(communityKey).size();
                communityList.add(communityActiveUser);
                int gameActiveUser = shardedJedis.hkeys(gameKey).size();
                gameList.add(gameActiveUser);
                int ecomActiveUser = shardedJedis.hkeys(ecomKey).size();
                ecomList.add(ecomActiveUser);

                System.out.println(dvbActiveUser);
            }

            // 获取新数据
            int dvbActiveUser = this.getCurrentRedisKeys("*_2_busi");
            dvbList.add(dvbActiveUser);
            int ottActiveUser = this.getCurrentRedisKeys("*_6_busi");
            ottList.add(ottActiveUser);
            int replayActiveUser = this.getCurrentRedisKeys("*_4_busi");
            replayList.add(replayActiveUser);
            int tsActiveUser = this.getCurrentRedisKeys("*_3_busi");
            tsList.add(tsActiveUser);
            int vodUser = this.getCurrentRedisKeys("*_5_busi");
            vodList.add(vodUser);
            int appstoreActiveUser = this.getCurrentRedisKeys("*_9_busi");
            appstoreList.add(appstoreActiveUser);
            int educationActiveUser = this.getCurrentRedisKeys("*_10_busi");
            educationList.add(educationActiveUser);
            int communityActiveUser = this.getCurrentRedisKeys("*_11_busi");
            communityList.add(communityActiveUser);
            int gameActiveUser = this.getCurrentRedisKeys("*_20_busi");
            gameList.add(gameActiveUser);
            int ecomActiveUser = this.getCurrentRedisKeys("*_15_busi");
            ecomList.add(ecomActiveUser);

            System.out.println(System.currentTimeMillis());

            List online = new ArrayList();
            for (int i = 0; i < 24; i++) {
                //online.add(Math.random() * 1000);
            }
            kpiData.put("dvb", dvbList);
            kpiData.put("replay", replayList);
            kpiData.put("ts", tsList);
            kpiData.put("vod", vodList);
            kpiData.put("ott", ottList);
            kpiData.put("appstore", appstoreList);
            kpiData.put("education", educationList);
            kpiData.put("community", communityList);
            kpiData.put("lifeproduct", lifeProductList);
            kpiData.put("game", gameList);
            kpiData.put("ecom", ecomList);
            result.put("result", kpiData);

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
