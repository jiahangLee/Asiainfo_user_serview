package gags.service.util;

import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangtao on 2017-09-05.
 * Elasticsearch 工具类
 */
public class ESDataUtil {
    //    public static final String ES_ADDRESS = "10.254.15.106";
    public static final String ES_ADDRESS = "10.2.22.28";
    public static final Integer ES_PORT = 9202;
    public static final String ES_INDICES = "lpl";
    public static final String ES_INDICES_CONFIG = "config";
    public static final String ES_INDICES_DVB_PROGRAM_CONFIG = "dvb_program_config";

    public static TransportClient client = null;

    /**
     * 获取Elasticsearch 连接
     *
     * @return
     */
    public static TransportClient getTransportClient() {
        if (client != null)
            return client;
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", true).build();
        // on startup
        // 获取TransportClient
        try {
            client = TransportClient
                    .builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress
                                    .getByName(ES_ADDRESS), ES_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        List<DiscoveryNode> connectedNodes = client.connectedNodes();
        for (DiscoveryNode discoveryNode : connectedNodes) {
            System.out.println("集群节点：" + discoveryNode.getHostName());
        }
        return client;
    }

    /**
     * 转换结果数据集
     *
     * @param data
     * @param tableName
     * @param newKpis
     * @param qb
     * @param period
     */
    public static void makeESDataMap(String[] indices, List<Map<String, Object>> data, String tableName, List<String> newKpis, QueryBuilder qb, @RequestParam String period) {

        SimpleDateFormat dateFormat = getSimpleDateFormat(period);

        TransportClient client = getTransportClient();

        String[] newKpisFields = (String[]) newKpis.toArray(new String[newKpis.size()]);

        String[] newIndices = filterIndies(indices);
        if (newIndices.length > 0) {
            SearchResponse response = client.prepareSearch(newIndices)
                    .setTypes(tableName.toLowerCase())
                    .addFields(newKpisFields)
                    .setQuery(qb)
                    .setSize(10000)
                    .addSort("statistic_time", SortOrder.ASC)
                    // .addSort("user_index",SortOrder.DESC)
                    .execute()
                    .actionGet();

            makeKpiDataMap(data, response, dateFormat);
        }

    }


    /**
     * 转换结果数据集
     *
     * @param data
     * @param response
     * @param dateFormat
     */
    public static void makeKpiDataMap(List<Map<String, Object>> data, SearchResponse response, SimpleDateFormat dateFormat) {
        int rowNum = 1;
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> kpiMapData = new HashMap<String, Object>();
            Set<Map.Entry<String, SearchHitField>> fieldEntry = hit.getFields().entrySet();
            for (Map.Entry<String, SearchHitField> entry : fieldEntry) {
                String kpiName = entry.getValue().getName();
                String kpiName2 = "";
                String kpiBus = "";
                if (hit.getFields().containsKey("level_type"))
                    kpiName2 = hit.getFields().get("level_type").getValue();
                if (hit.getFields().containsKey("business"))
                    kpiBus = hit.getFields().get("business").getValue();
                String kpiValue = "";
                if (kpiName2.equals("use_time") && kpiName.equals("user_level")) {
                    if (kpiBus.equals("game_room") || kpiBus.equals("game_visit"))
                        kpiValue = entry.getValue().getValue().toString() + "秒";
                    else
                        kpiValue = entry.getValue().getValue().toString() + "分";
                } else if (kpiName2.equals("use_count") && kpiName.equals("user_level"))
                    kpiValue = entry.getValue().getValue().toString() + "次";
                else if (kpiName2.equals("use_day") && kpiName.equals("user_level"))
                    kpiValue = entry.getValue().getValue().toString() + "[天)";
                else
                    kpiValue = entry.getValue().getValue().toString();
                if (entry.getValue().getName().equals("statistic_time")) {
                    try {
                        Long statisticTime = Long.parseLong(kpiValue.toString()) * 1000;
                        kpiMapData.put(kpiName, dateFormat.format(new Date(statisticTime)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    kpiMapData.put(kpiName, kpiValue);
                }
                // System.out.println(JSONObject.toJSONString(kpiMapData));
            }
            kpiMapData.put("row_num", rowNum);
            rowNum++;
            data.add(kpiMapData);
        }
    }

    /**
     * 获取日期转换格式
     *
     * @param period
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String period) {
        SimpleDateFormat kpiDateFormat = null;
        if (NumberUtils.isNumber(period)) {
            kpiDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            kpiDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return kpiDateFormat;
    }

    /**
     * 判断指定的索引名是否存在
     *
     * @param indexName 索引名
     * @return 存在：true; 不存在：false;
     */
    public static boolean isExistsIndex(String indexName) {
        TransportClient client = ESDataUtil.getTransportClient();
        IndicesExistsResponse response =
                client.admin().indices().exists(
                        new IndicesExistsRequest().indices(new String[]{indexName})).actionGet();

        try {
            client.admin().indices().prepareStats(indexName).execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return response.isExists();
    }


    /**
     * 筛选正常的索引
     *
     * @param indies
     * @return
     */
    public static String[] filterIndies(String[] indies) {
        List indexList = new ArrayList();

        for (String indexName : indies) {
            if (isExistsIndex(indexName)) {
                indexList.add(indexName);
            }
        }
        return (String[]) indexList.toArray(new String[indexList.size()]);
    }

}
