package gags.service.util;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zhangtao on 2017-09-26.
 */
public class ESDataCRUDUtil {

    public static void main1(String[] args) {

        // 创建索引及类型
//        for(int i =5;i<=12;i++) {
//            String index ="";
//            if(i<10){
//                index = "20170"+i;
//            }else{
//                index = "2017"+i;
//            }
//            System.out.println(index);
//            synchronized (index) {
//                createIndexTypes(index);
//            }
//        }

//        createIndexEcomTypes("201712");

//        createIndexOtt2Types("201806");
//        createIndexPie("201806");
//
//        createIndexTypeGameRoom("201806");
//        createIndexTypeGameVisit("201806");
//        createIndexTypeGameVisitSingle("201806");
//        createIndexTypeGameVisitRetention("201806");

//        createIndexTypesVODSectionThree("threedata");
//         createIndexTypesReplayThree("threedata");
//
//          createIndexTypeSeriesThree("threedata");
//        createIndexTypesDVBThree("threedata");
//        createIndexChannelTypesThree("threedata");
//        createIndexTypeAdvertThree("threedata");

//        createEducationIndexTypes("201806");
//        createIndexTypesApp("201806");
//        createIndexTypes("201806");
//        createIndexTypesFlow("201806");
//        createIndexTypesDVB("201806");
//        createIndexChannelTypes("201806");
//        createIndexTypeRetention("201806");
//        createIndexTypeSave("201806");
//        createIndexTypeAdvert("201806");
        // 删除types数据
        //deleteData();

//        String result = restTemplate.getForObject(
//                "http://example.com/hotels/{hotel}/bookings/{booking}", String.class,"42", "21");

//        TransportClient client = ESDataUtil.getTransportClient();
//        deleteESData(client, "t_config_channel_group","config");
//        deleteById(client, "t_project_overview_common_day");

    }


    private static void deleteById(TransportClient client, String type) {
        String indices = "201711";
        String id = "AWAJ2ldcOopBaE1LXpjP";
        DeleteResponse dResponse = client.prepareDelete(indices, type, id).execute().actionGet();
        if (dResponse.isFound()) {
            System.out.println(type + ": 10000 delete ok");
        } else {
            System.out.println(type + ": 10000 delete fail! id=" + id);
        }
    }


    private static void deleteData() {

        TransportClient client = ESDataUtil.getTransportClient();

        String[] periods = new String[]{"15", "30", "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"dvb", "ts", "replay"};
        String[] businessGroup2 = new String[]{"education", "ott", "vod"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_channel_group_" + period;
                    deleteESData(client, esBusinessUserTableName);
                }
            }
        }

        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_channel_" + period;
                    deleteESData(client, esBusinessUserTableName);

                }
            }
        }

        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_section_" + period;
                    deleteESData(client, esBusinessUserTableName);
                }
            }
        }

        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_program_" + period;
                    deleteESData(client, esBusinessUserTableName);
                }
            }
        }
    }


    private static void deleteESData(TransportClient client, String tableName,String indices) {
//        String indices = "201710";
//        String value = "20171115";
        String areaName = "0104";
        String l_date = "20180505";

//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        SearchResponse response = client.prepareSearch(indices).setTypes(type)
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(QueryBuilders.termQuery("l_date", value))
//                .setQuery(QueryBuilders.termQuery("area_name", areaName))
//                .setSize(10000).setExplain(true).execute().actionGet();

        QueryBuilder dfqb  =  boolQuery()
                .must(QueryBuilders.termQuery("l_date", l_date))
                .must(QueryBuilders.termQuery("area_name", areaName));
//                .filter(rangeQuery("statistic_time").from("1523030400").to("1523635200"));

        SearchResponse response = client.prepareSearch(indices)
                .setTypes(tableName.toLowerCase())
                .addFields("_id")
//                .setQuery(dfqb)
                .setSize(10000)
                // .addSort("user_index",SortOrder.DESC)
                .execute()
                .actionGet();

        for(SearchHit hit : response.getHits()){
            String id = hit.getId();
            System.out.println(id+"-------------------");
            DeleteResponse dResponse = client.prepareDelete(indices, tableName, id).execute().actionGet();
            if (dResponse.isFound()) {
                System.out.println(tableName+": 10000 delete ok");
            } else {
                System.out.println(tableName+": 10000 delete fail! id="+id);
            }
        }

//        if (bulkRequest.numberOfActions() > 0) {
//            BulkResponse bulkResponse = bulkRequest.get();
//            if (bulkResponse.hasFailures()) {
//                for (BulkItemResponse item : bulkResponse.getItems()) {
//                    System.out.println(item.getFailureMessage());
//                }
//            } else {
//                System.out.println(type+": 10000 delete ok");
//            }
//        }
    }

    private static void deleteESData(TransportClient client, String tableName) {
        String indices = "201710";
        String value = "20171115";
        String areaName = "0101";

//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        SearchResponse response = client.prepareSearch(indices).setTypes(type)
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(QueryBuilders.termQuery("l_date", value))
//                .setQuery(QueryBuilders.termQuery("area_name", areaName))
//                .setSize(10000).setExplain(true).execute().actionGet();

        QueryBuilder dfqb  =  boolQuery()
                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery());

        SearchResponse response = client.prepareSearch(indices)
                .setTypes(tableName.toLowerCase())
                .addFields("_id")
                .setSize(10000)
                // .addSort("user_index",SortOrder.DESC)
                .execute()
                .actionGet();

        for(SearchHit hit : response.getHits()){
            String id = hit.getId();
            System.out.println(id+"-------------------");
            System.out.println(hit);
            DeleteResponse dResponse = client.prepareDelete(indices, tableName, id).execute().actionGet();
            if (dResponse.isFound()) {
                System.out.println(tableName+": 10000 delete ok");
            } else {
                System.out.println(tableName+": 10000 delete fail! id="+id);
            }
        }

//        if (bulkRequest.numberOfActions() > 0) {
//            BulkResponse bulkResponse = bulkRequest.get();
//            if (bulkResponse.hasFailures()) {
//                for (BulkItemResponse item : bulkResponse.getItems()) {
//                    System.out.println(item.getFailureMessage());
//                }
//            } else {
//                System.out.println(type+": 10000 delete ok");
//            }
//        }
    }

    private static synchronized void createIndexChannelTypes(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"1", "5"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"dvb"};

        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_channel_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }


    }

    private static synchronized void createIndexChannelTypesThree(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] periods2 = new String[]{"day","month"};
        String[] regionRanks = new String[]{"project"};
        String[] businessGroup1 = new String[]{"dvb","replay"};
        String[] businessGroup2 = new String[]{"three","three_zj"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    for (String business2 : businessGroup2) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_channel_" + period + "_" +business2;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

        for (String period : periods2) {
            for (String rank : regionRanks) {
                for (String business2 : businessGroup2) {
                    String esBusinessUserTableName = "t_" + rank + "_vod_program_" + period + "_" + business2;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAppMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }


    }

    private static synchronized void createEducationIndexTypes(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"15", "30", "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"education"};
        String[] businessTypeGroup1 = new String[]{"section2"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_" + businessType + "_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMappingSection());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "data_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getRetentionMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "flow_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getFlowMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }


    private static synchronized void createIndexTypes(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"15", "30", "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"dvb", "ts", "replay"};
        String[] businessGroup2 = new String[]{"education", "ott", "vod"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_channel_group_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMappingChannelGroup());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_channel_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_section_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMappingSection());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_program_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMappingProgram());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }
    }

    private static synchronized void createIndexOtt2Types(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"15", "30", "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "province", "china"};
        String[] businessGroup2 = new String[]{"ott"};



        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_section2_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMappingSection());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

    }

    private static synchronized void createIndexEcomTypes(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{ "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "province", "china"};
        String[] businessGroup2 = new String[]{"ecom"};



        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_scan_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMappingEcom());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

    }

    private static synchronized void createIndexTypesFlow(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"15", "30", "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"dvb", "ts", "replay"};
        String[] businessGroup2 = new String[]{"education", "ott", "vod"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "flow_"+ rank +"_" + business + "_channel_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getFlowMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "flow_"+ rank +"_" + business + "_section_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getFlowMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

        for (String business : businessGroup2) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "flow_"+ rank +"_" + business + "_program_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getFlowMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }
    }

    private static synchronized void createIndexTypesApp(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);



        String[] periods = new String[]{"15", "30", "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"app"};
        String[] businessGroup2 = new String[]{"app", "type1", "type2"};

        String[] businessGroup3 = new String[]{"appstore"};
        String[] businessGroup4 = new String[]{"appname", "type1", "type2"};

        for (String business : businessGroup1) {
            for (String businessType : businessGroup2) {
                if(!businessType.equals("app"))
                    continue;
                for (String period : periods) {
                    if(!period.equals("day"))
                        continue;
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "data_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAppMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }


        for (String business : businessGroup1) {
            for (String businessType : businessGroup2) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "flow_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getFlowMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

        for (String business : businessGroup1) {
            for (String businessType : businessGroup2) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMappingSection());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

        for (String business : businessGroup3) {
            for (String businessType : businessGroup4) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAppStoreMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypesDVB(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"1","5","15", "30", "60", "day"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"dvb"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_program_" + period;
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getDVBMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

    }

    private static synchronized void createIndexTypesDVBThree(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{ "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project"};
        String[] businessGroup1 = new String[]{"dvb"};
        String[] businessGroup2 = new String[]{"three","three_zj"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    for (String business2 : businessGroup2) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_program_" + period + "_" + business2;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getDVBMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypesReplayThree(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{ "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project"};
        String[] businessGroup1 = new String[]{"replay"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_program_" + period+"_three_zj";
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getDVBMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

    }

    private static synchronized void createIndexTypesVODSectionThree(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{ "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project"};
        String[] businessGroup1 = new String[]{"vod"};


        for (String business : businessGroup1) {
            for (String period : periods) {
                for (String rank : regionRanks) {
                    String esBusinessUserTableName = "t_"+ rank +"_" + business + "_section_" + period+"_three_zj";
                    PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAppMapping());
                    client.admin().indices().putMapping(mapping).actionGet();
                }
            }
        }

    }

    private static synchronized void createIndexTypeRetention(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"15", "30", "60", "day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"education"};
        String[] businessTypeGroup1 = new String[]{"section","program"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "data_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getRetentionMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeSave(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"ott"};
        String[] businessTypeGroup1 = new String[]{"media"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMediaMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeSeriesThree(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60","day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project"};
        String[] businessGroup1 = new String[]{"vod"};
        String[] businessTypeGroup1 = new String[]{"series"};
        String[] businessTypeGroup2 = new String[]{"three","three_zj"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        for (String businessType2 : businessTypeGroup2) {
                            String esBusinessUserTableName = "t_" + rank + "_" + business + "_" + businessType + "_" + period + "_" +businessType2;
                            PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAppMapping());
                            client.admin().indices().putMapping(mapping).actionGet();
                        }
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeAdvertThree(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60","day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project"};
        String[] businessGroup1 = new String[]{"advert"};
        String[] businessTypeGroup1 = new String[]{"common"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period +"_three";
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAdvertMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeAdvert(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60","day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"advert"};
        String[] businessTypeGroup1 = new String[]{"common"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAdvertMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexPie(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"day", "week",  "month",  "year"};
        String[] regionRanks = new String[]{"project", "province", "china"};
        String[] businessGroup1 = new String[]{"all"};
        String[] businessTypeGroup1 = new String[]{"layer"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getPieMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexAllCommon(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "region", "province", "china"};
        String[] businessGroup1 = new String[]{"all"};
        String[] businessTypeGroup1 = new String[]{"common"};


        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getMediaMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeGameRoom(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60","day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "province", "china"};
        String[] businessGroup1 = new String[]{"game_room"};
        String[] businessTypeGroup1 = new String[]{"use_single_game"};

        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period ;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAppMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeGameVisit(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60","day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "province", "china"};
        String[] businessGroup1 = new String[]{"game_visit"};
        String[] businessTypeGroup1 = new String[]{"type1","type2"};

        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period ;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getGameTypeMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeGameVisitSingle(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60","day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "province", "china"};
        String[] businessGroup1 = new String[]{"game_visit"};
        String[] businessTypeGroup1 = new String[]{"single_game"};

        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "t_" + rank + "_" + business + "_"+businessType+"_" + period ;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getGameSingleTypeMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

    private static synchronized void createIndexTypeGameVisitRetention(String index) {

        TransportClient client = ESDataUtil.getTransportClient();

        // 创建索引
//        CreateIndexRequest request = new CreateIndexRequest(index);
//        client.admin().indices().create(request);

        String[] periods = new String[]{"60","day", "week", "halfmonth", "month", "quarter", "year"};
        String[] regionRanks = new String[]{"project", "province", "china"};
        String[] businessGroup1 = new String[]{"game_visit"};
        String[] businessTypeGroup1 = new String[]{"single_game","type1","type2","common"};

        for (String business : businessGroup1) {
            for (String businessType : businessTypeGroup1) {
                for (String period : periods) {
                    for (String rank : regionRanks) {
                        String esBusinessUserTableName = "data_" + rank + "_" + business + "_"+businessType+"_" + period ;
                        PutMappingRequest mapping = Requests.putMappingRequest(index).type(esBusinessUserTableName).source(getAppMapping());
                        client.admin().indices().putMapping(mapping).actionGet();
                    }
                }
            }
        }

    }

//    public static void createBangMapping(){
//        TransportClient client = ESDataUtil.getTransportClient();
//
//        CreateIndexRequest request = new CreateIndexRequest("lpl");
//        client.admin().indices().create(request);
//
//        PutMappingRequest mapping = Requests.putMappingRequest("lpl").type("t_project_dvb_channel_60").source(getMapping());
//        client.admin().indices().putMapping(mapping).actionGet();
//
//    }

    public static XContentBuilder getRetentionMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("subtype_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("program_label")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getMediaMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("programtype")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("programtypename")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }


    public static XContentBuilder getAdvertMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("adplacename")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("adtype")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getPieMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")

                    .startObject("subtype_name1")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("subtype_name2")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()

                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getAllCommonMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("programtype")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("programtypename")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getDVBMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("channel_label")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("program_label")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getFlowMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("first_sort")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("second_sort")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("subtype_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getAppMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("subtype_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getGameTypeMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("game_visit_type_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getGameSingleTypeMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("game_visit_single_game_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getAppStoreMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("appname")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }


    public static XContentBuilder getMapping(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("channel_group_label")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("channel_label")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getMappingChannelGroup(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("channel_group_label")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getMappingSection(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("section_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getMappingEcom(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("shop_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("type_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("goods_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static XContentBuilder getMappingProgram(){

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    //开启倒计时功能
                    .startObject("_ttl")
                    .field("enabled",false)
                    .endObject()
                    .startObject("properties")
                    .startObject("program_name")
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }
}
