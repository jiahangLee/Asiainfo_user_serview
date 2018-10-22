package gags.service.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import gags.service.dao.ChannelGroupQueryDAO;
import gags.service.dao.ChannelQueryDAO;
import gags.service.dao.ConfigQueryDAO;
import gags.service.entity.ChannelAndChannelGroupEntity;
import gags.service.entity.ConfigTreeEntity;
import gags.service.util.ESDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static gags.service.util.ESDataUtil.ES_INDICES_CONFIG;
import static gags.service.util.ESDataUtil.ES_INDICES_DVB_PROGRAM_CONFIG;
import static gags.service.util.ESDataUtil.getTransportClient;
import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Created by jiahang Lee on 2017/9/5.
 */
@RestController
@RequestMapping("/config")
@Api(value = "基础配置信息API")
public class ESConfigController {
    private static String fields = "keyss";
    private static String fields2 = "projectno";
    private static int esDataSize = 6000;
    private List biz = new ArrayList();

    {
        biz.add("vod");
        biz.add("ott");
        biz.add("education");
        biz.add("program_three");
        biz.add("series_three");
    }

        @Autowired
        private ConfigQueryDAO configQueryDAO;

        @Autowired
        private ChannelGroupQueryDAO channelGroupQueryDAO;

        @Autowired
        private ChannelQueryDAO channelQueryDAO;

        @GetMapping(path = "/areas")
        @ApiOperation(value = "获取所有区域信息")
        public String getAllArea() {
            TransportClient client= ESDataUtil.getTransportClient();
            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes("t_config_area")
                    .addFields("id","label","name","parent_id")
//                .setQuery(QueryBuilders.ma)
//                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();
            List<Map<String, Object>> data = new ArrayList<>();
            ESDataUtil.makeKpiDataMap(data,response,null);
            return JSONObject.toJSONString(data);
        }
/*
    @GetMapping(path = "/channels")
    @ApiOperation(value = "获取所有channel信息")
    public ResponseEntity<List<ConfigEntity>> getChannels() {
        String tableName = "T_CONFIG_CHANNEL";
        return new ResponseEntity<List<ConfigEntity>>(configQueryDAO.getCommonConfig(tableName), HttpStatus.OK);
    }*/
        @GetMapping(path = "/appsections")
        @ApiOperation(value = "获取所有应用配置信息")
        public String getAppSections(@RequestParam String business,@RequestParam String biz){

            List<String> ls = new ArrayList<String>();
            String tableName="T_CONFIG_APP";
            String label = business;
            if(business.equals("appname")) {
                label = "app";
            }
            if(biz.equals("appstore")){
                label = "";
            }

            TransportClient client= ESDataUtil.getTransportClient();

            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("section_id","label","name","app_type")
                    .setSize(esDataSize)
        //                .setQuery(QueryBuilders.ma)
                    .setQuery(StringUtils.isNotBlank(label)?QueryBuilders.termQuery("app_type",label):matchAllQuery())
                    .execute()
                    .actionGet();

            //获取数据方式
            JSONArray array = new JSONArray();
            for(SearchHit hit:response.getHits().getHits()){
                if(fields != null){
                    Set<Map.Entry<String, SearchHitField>> fieldEntry =  hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for(Map.Entry<String, SearchHitField> entry:fieldEntry){
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                }else{
                    array.add(hit.getSourceAsString());
                }
            }
            //System.out.print(array);
            for(int i=0;i<array.size();i++){
                JSONObject job = array.getJSONObject(i);
                ls.add(job.get("label").toString());
            }
            // System.out.print(ls);

            if(biz.equals("appstore")){
                return JSONObject.toJSONString(array);
            }


            return JSONObject.toJSONString(ls);

        //            return new ResponseEntity<List<String>>(configQueryDAO.getSectionList(tableName),HttpStatus.OK);
        }

        @GetMapping(path = "/sections")
        @ApiOperation(value = "获取所有栏目信息")
        public ResponseEntity<List<String>> getSections(@RequestParam String business){

            List<String> ls = new ArrayList<String>();
            String tableName="T_CONFIG_"+business+"_SECTION";
            if(business.equals("vod_three")){
                tableName="T_CONFIG_VOD_SECTION_THREE";
            }

            TransportClient client= ESDataUtil.getTransportClient();

            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("section_id","label","name")
                    .setSize(esDataSize)
//                .setQuery(QueryBuilders.ma)
//                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            JSONArray array = new JSONArray();
            for(SearchHit hit:response.getHits().getHits()){
                if(fields != null){
                    Set<Map.Entry<String, SearchHitField>> fieldEntry =  hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for(Map.Entry<String, SearchHitField> entry:fieldEntry){
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                }else{
                    array.add(hit.getSourceAsString());
                }
            }
            //System.out.print(array);
            for(int i=0;i<array.size();i++){
                JSONObject job = array.getJSONObject(i);
                ls.add(job.get("label").toString());
            }
           // System.out.print(ls);


            return new ResponseEntity<List<String>>(ls,HttpStatus.OK);

//            return new ResponseEntity<List<String>>(configQueryDAO.getSectionList(tableName),HttpStatus.OK);
        }



        @GetMapping(path = "/gameVisits")
        @ApiOperation(value = "获取所有栏目信息")
        public ResponseEntity<List<String>> getSingle(@RequestParam String business,@RequestParam(required = false) String biz){

            List<String> ls = new ArrayList<String>();
            String tableName="T_CONFIG_GAME_VISIT_"+business;
            TransportClient client= ESDataUtil.getTransportClient();

            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("label","name")
                    .setSize(esDataSize)
//                .setQuery(QueryBuilders.ma)
//                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            JSONArray array = new JSONArray();
            for(SearchHit hit:response.getHits().getHits()){
                if(fields != null){
                    Set<Map.Entry<String, SearchHitField>> fieldEntry =  hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for(Map.Entry<String, SearchHitField> entry:fieldEntry){
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                }else{
                    array.add(hit.getSourceAsString());
                }
            }
            //System.out.print(array);
            for(int i=0;i<array.size();i++){
                JSONObject job = array.getJSONObject(i);
                ls.add(job.get("label").toString());
            }
           // System.out.print(ls);


            return new ResponseEntity<List<String>>(ls,HttpStatus.OK);

//            return new ResponseEntity<List<String>>(configQueryDAO.getSectionList(tableName),HttpStatus.OK);
        }

        @GetMapping(path = "/sections2")
        @ApiOperation(value = "获取所有栏目信息")
        public ResponseEntity<List<String>> getSections2(@RequestParam String business){

            List<String> ls = new ArrayList<String>();
            String tableName="T_CONFIG_"+business+"_SECTION2";

            TransportClient client= ESDataUtil.getTransportClient();

            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("section_id","label","name")
                    .setSize(esDataSize)
    //                .setQuery(QueryBuilders.ma)
    //                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            JSONArray array = new JSONArray();
            for(SearchHit hit:response.getHits().getHits()){
                if(fields != null){
                    Set<Map.Entry<String, SearchHitField>> fieldEntry =  hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for(Map.Entry<String, SearchHitField> entry:fieldEntry){
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                }else{
                    array.add(hit.getSourceAsString());
                }
            }
            //System.out.print(array);
            for(int i=0;i<array.size();i++){
                JSONObject job = array.getJSONObject(i);
                ls.add(job.get("label").toString());
            }
            // System.out.print(ls);


            return new ResponseEntity<List<String>>(ls,HttpStatus.OK);

        }

        @GetMapping(path = "/programs")
        @ApiOperation(value = "获取所有节目信息")
        public ResponseEntity<List<String>> getPrograms(@RequestParam String business,
                                                        @RequestParam(required = false) String label) {
            String tableName = "T_CONFIG_" + business + "_PROGRAM";
            if("program_three".equals(business)){
                tableName= "T_CONFIG_VOD_PROGRAM_THREE";
            }else if("series_three".equals(business)){
                tableName= "T_CONFIG_VOD_SERIES_THREE";
            }
            int dataSize = 200;
            if(!biz.contains(business)){
                label = "";
                dataSize = 6000;
            }

            List<String> ls = new ArrayList<String>();

            TransportClient client = ESDataUtil.getTransportClient();

            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("label")
                    .setSize(dataSize)
                    .setQuery(StringUtils.isNotBlank(label)?QueryBuilders.queryStringQuery(label):matchAllQuery())
//                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            JSONArray array = new JSONArray();
            for (SearchHit hit : response.getHits().getHits()) {
                if (fields != null) {
                    Set<Map.Entry<String, SearchHitField>> fieldEntry = hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for (Map.Entry<String, SearchHitField> entry : fieldEntry) {
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                } else {
                    array.add(hit.getSourceAsString());
                }
            }
            //System.out.print(array);
            for (int i = 0; i < array.size(); i++) {
                JSONObject job = array.getJSONObject(i);
                ls.add(job.get("label").toString());
            }
            //System.out.print(ls);
            return new ResponseEntity<List<String>>(ls, HttpStatus.OK);
        }

        @GetMapping(path = "/programsOfChannel")
        @ApiOperation(value = "获取所有频道下节目信息")
        public ResponseEntity<JSONArray> getProgramsOfChannel(@RequestParam String business,@RequestParam String startTime) {
            String tableName = "T_CONFIG_DVB_PROGRAM_CHANNEL";

            List<String> ls = new ArrayList<String>();

            TransportClient client = ESDataUtil.getTransportClient();

            if("0".equals(startTime)){
                startTime = "";
            }

            SearchResponse response = client.prepareSearch(ES_INDICES_DVB_PROGRAM_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("program_name","channel_label")
                    .setSize(10000)
                    .setQuery(StringUtils.isNotBlank(startTime)?termsQuery("l_date",startTime):matchAllQuery())
    //                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            JSONArray array = new JSONArray();
            for (SearchHit hit : response.getHits().getHits()) {
                if (fields != null) {
                    Set<Map.Entry<String, SearchHitField>> fieldEntry = hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for (Map.Entry<String, SearchHitField> entry : fieldEntry) {
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                } else {
                    array.add(hit.getSourceAsString());
                }
            }

            return new ResponseEntity<JSONArray>(array, HttpStatus.OK);
        }

    @GetMapping(path = "/programsOfChannelOfThree")
    @ApiOperation(value = "获取第三方频道下节目信息")
    public ResponseEntity<JSONArray> getProgramsOfChannelThree(@RequestParam String business,@RequestParam String startTime ,@RequestParam String regionCode) {
        String tableName = "t_config_"+business+"_program_channel_three";

        List<String> ls = new ArrayList<String>();

        TransportClient client = ESDataUtil.getTransportClient();

        if("0".equals(startTime)){
            startTime = "";
        }
        if("".equals(startTime) || "".equals(regionCode)){
            return new ResponseEntity<JSONArray>(new JSONArray(), HttpStatus.OK);
        }

        QueryBuilder qb  =  boolQuery()
                .must(termQuery("l_date",startTime))
                .must(termQuery("projectno",regionCode));

        QueryBuilder qb2  =  boolQuery()
                .must(termQuery("projectno",regionCode));

        SearchResponse response = null;
        if(business.equals("dvb")) {
            response = client.prepareSearch(ES_INDICES_DVB_PROGRAM_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("program_name", "channel_label")
                    .setSize(10000)
                    .setQuery(qb)
                    .execute()
                    .actionGet();
        }else if(business.equals("replay")){
            response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName.toLowerCase())
                    .addFields("program_name", "channel_label")
                    .setSize(10000)
                    .setQuery(qb2)
                    .execute()
                    .actionGet();
        }

        //获取数据方式
        JSONArray array = new JSONArray();
        if(response !=null) {
            for (SearchHit hit : response.getHits().getHits()) {
                if (fields != null) {
                    Set<Map.Entry<String, SearchHitField>> fieldEntry = hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for (Map.Entry<String, SearchHitField> entry : fieldEntry) {
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                } else {
                    array.add(hit.getSourceAsString());
                }
            }
        }

        return new ResponseEntity<JSONArray>(array, HttpStatus.OK);
    }

    @GetMapping(path = "/mediasOfOtt")
    @ApiOperation(value = "获取所有媒咨配置信息")
    public ResponseEntity<JSONArray> getOttMedias() {
        String tableName = "T_CONFIG_OTT_MEDIA";

        List<String> ls = new ArrayList<String>();

        TransportClient client = ESDataUtil.getTransportClient();

        SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                .setTypes(tableName.toLowerCase())
                .addFields("programtype","programtypename")
                .setSize(10000)
                //.setQuery(StringUtils.isNotBlank(startTime)?termsQuery("l_date",startTime):matchAllQuery())
                //                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                .execute()
                .actionGet();

        //获取数据方式
        JSONArray array = new JSONArray();
        for (SearchHit hit : response.getHits().getHits()) {
            if (fields != null) {
                Set<Map.Entry<String, SearchHitField>> fieldEntry = hit.getFields().entrySet();
                JSONObject json = new JSONObject();
                for (Map.Entry<String, SearchHitField> entry : fieldEntry) {
                    json.put(entry.getValue().getName(), entry.getValue().getValue());
                }
                array.add(json);
            } else {
                array.add(hit.getSourceAsString());
            }
        }

        return new ResponseEntity<JSONArray>(array, HttpStatus.OK);
    }

    @GetMapping(path = "/advertOfCommon")
    @ApiOperation(value = "获取所有广告配置信息")
    public ResponseEntity<JSONArray> getAdverts() {
        String tableName = "T_CONFIG_ADVERT_COMMON";

        List<String> ls = new ArrayList<String>();

        TransportClient client = ESDataUtil.getTransportClient();

        SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                .setTypes(tableName.toLowerCase())
                .addFields("adplacename","adtype")
                .setSize(10000)
                //.setQuery(StringUtils.isNotBlank(startTime)?termsQuery("l_date",startTime):matchAllQuery())
                //                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                .execute()
                .actionGet();

        //获取数据方式
        JSONArray array = new JSONArray();
        for (SearchHit hit : response.getHits().getHits()) {
            if (fields != null) {
                Set<Map.Entry<String, SearchHitField>> fieldEntry = hit.getFields().entrySet();
                JSONObject json = new JSONObject();
                for (Map.Entry<String, SearchHitField> entry : fieldEntry) {
                    json.put(entry.getValue().getName(), entry.getValue().getValue());
                }
                array.add(json);
            } else {
                array.add(hit.getSourceAsString());
            }
        }

        return new ResponseEntity<JSONArray>(array, HttpStatus.OK);
    }

    @GetMapping(path = "/advertOfThree")
    @ApiOperation(value = "获取第三方广告配置信息")
    public ResponseEntity<JSONArray> getAdvertsOfThree(@RequestParam String regionCode) {
        String tableName = "T_CONFIG_ADVERT_THREE";

        List<String> ls = new ArrayList<String>();
        if("".equals(regionCode)){
            return new ResponseEntity<JSONArray>(new JSONArray(), HttpStatus.OK);
        }

        TransportClient client = ESDataUtil.getTransportClient();

        SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                .setTypes(tableName.toLowerCase())
                .addFields("adplacename","adtype")
                .setSize(10000)
                .setQuery(termsQuery("area_name",regionCode))
                //                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                .execute()
                .actionGet();

        //获取数据方式
        JSONArray array = new JSONArray();
        for (SearchHit hit : response.getHits().getHits()) {
            if (fields != null) {
                Set<Map.Entry<String, SearchHitField>> fieldEntry = hit.getFields().entrySet();
                JSONObject json = new JSONObject();
                for (Map.Entry<String, SearchHitField> entry : fieldEntry) {
                    json.put(entry.getValue().getName(), entry.getValue().getValue());
                }
                array.add(json);
            } else {
                array.add(hit.getSourceAsString());
            }
        }

        return new ResponseEntity<JSONArray>(array, HttpStatus.OK);
    }

        @GetMapping(path = "/programsOfSection")
        @ApiOperation(value = "获取某个栏目下的所有节目")
        public ResponseEntity<List<String>> getProgramsOfSection(@RequestParam String business,@RequestParam String sectionName){
            List<String> ls = new ArrayList<String>();
            TransportClient client= ESDataUtil.getTransportClient();
            String s = "T_CONFIG_"+business+"_PROGRAM";
            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(s.toLowerCase())
                    .addFields("label")
                    .setSize(esDataSize)
//                .setQuery(QueryBuilders.ma)
//                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            JSONArray array = new JSONArray();
            for(SearchHit hit:response.getHits().getHits()){
                if(fields != null){
                    Set<Map.Entry<String, SearchHitField>> fieldEntry =  hit.getFields().entrySet();
                    JSONObject json = new JSONObject();
                    for(Map.Entry<String, SearchHitField> entry:fieldEntry){
                        json.put(entry.getValue().getName(), entry.getValue().getValue());
                    }
                    array.add(json);
                }else{
                    array.add(hit.getSourceAsString());
                }
            }
            System.out.print(array);
            System.out.print(array);
            for(int i=0;i<array.size();i++){
                JSONObject job = array.getJSONObject(i);
                ls.add(job.get("label").toString());
            }
            //System.out.print(ls);
            return  new ResponseEntity<List<String>>(ls,HttpStatus.OK);

//            return new ResponseEntity<List<String>>(configQueryDAO.getProgramsOfSection(business,sectionName),HttpStatus.OK);
        }

        @RequestMapping(path = "/areas2")
        public List<Map<String, Object>> areas() {
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            Map provice = new HashMap();
            provice.put("id", 1);
            provice.put("name", "hunan");
            provice.put("parent_id", 0);
            provice.put("label", "湖南省");
            provice.put("type", "province");
            data.add(provice);

            Map city = new HashMap();
            city.put("id", 2);
            city.put("name", "cs");
            city.put("parent_id", 1);
            city.put("label", "长沙市");
            city.put("type", "city");
            data.add(city);

            Map project = new HashMap();
            project.put("id", 3);
            project.put("name", "proj");
            project.put("parent_id", 2);
            project.put("label", "长沙项目1");
            project.put("type", "project");
            data.add(project);
            return data;
        }

        @GetMapping(path = "/channelGroups")
        @ApiOperation(value = "获取频道组信息")
        public String getChannelGroup() {

            String tableName="t_config_channel_group";

            List<String> ls = new ArrayList<String>();

            TransportClient client= ESDataUtil.getTransportClient();

            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName)
                    .addFields("id","label")
                    .setSize(esDataSize)
//                .setQuery(QueryBuilders.ma)
//                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            List<Map<String, Object>> data = new ArrayList<>();
            ESDataUtil.makeKpiDataMap(data,response,null);
            return JSONObject.toJSONString(data);
        }

        @GetMapping(path = "/channels")
        @ApiOperation(value = "获取频道信息")
        public String getChannel() {

            String tableName="t_config_channel";

            TransportClient client= ESDataUtil.getTransportClient();
            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName)
                    .addFields("id","label")
                    .setSize(esDataSize)
//                .setQuery(QueryBuilders.ma)
//                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            List<Map<String, Object>> data = new ArrayList<>();
            ESDataUtil.makeKpiDataMap(data,response,null);
            return JSONObject.toJSONString(data);
        }

        @GetMapping(path = "/channelsOfThree")
        @ApiOperation(value = "获取第三方频道信息")
        public String getChannelOfThree(@RequestParam String business,@RequestParam String regionCode) {

            String tableName="t_config_channel_three";

            TransportClient client= ESDataUtil.getTransportClient();
            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName)
                    .addFields("id","label")
                    .setSize(esDataSize)
                    .setQuery(termsQuery("projectno",regionCode))
                    //                .setQuery(QueryBuilders.ma)
    //                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            List<Map<String, Object>> data = new ArrayList<>();
            ESDataUtil.makeKpiDataMap(data,response,null);
            return JSONObject.toJSONString(data);
        }

        @GetMapping(path = "/ecoms")
        @ApiOperation(value = "获取电商平台信息")
        public String getEcom() {

            String tableName="t_config_ecom";

            List<String> ls = new ArrayList<String>();

            TransportClient client= ESDataUtil.getTransportClient();

            SearchResponse response = client.prepareSearch(ES_INDICES_CONFIG)
                    .setTypes(tableName)
                    .addFields("shop_name","type_name","goods_name")
                    .setSize(esDataSize)
    //                .setQuery(QueryBuilders.ma)
    //                .setQuery(QueryBuilders.queryStringQuery("*get*"))//全文检索通配符
                    .execute()
                    .actionGet();

            //获取数据方式
            List<Map<String, Object>> data = new ArrayList<>();
            ESDataUtil.makeKpiDataMap(data,response,null);
            return JSONObject.toJSONString(data);
        }

    }

