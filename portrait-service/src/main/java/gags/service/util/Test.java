

/**
 * Created by Administrator on 2017-09-26.
 */
package gags.service.util;

        import java.sql.SQLFeatureNotSupportedException;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;

        import org.apache.commons.lang.StringUtils;
        import org.apache.commons.lang.math.NumberUtils;
        import org.elasticsearch.action.search.SearchRequestBuilder;
        import org.elasticsearch.action.search.SearchResponse;
        import org.elasticsearch.action.search.SearchType;
        import org.elasticsearch.client.Client;
        import org.elasticsearch.client.transport.TransportClient;
        import org.elasticsearch.common.transport.InetSocketTransportAddress;
        import org.elasticsearch.index.query.QueryBuilder;
        import org.elasticsearch.index.query.QueryBuilders;
        import org.elasticsearch.search.SearchHit;
        import org.elasticsearch.search.sort.FieldSortBuilder;
        import org.elasticsearch.search.sort.SortBuilder;
        import org.elasticsearch.search.sort.SortBuilders;
        import org.elasticsearch.search.sort.SortOrder;
        import org.springframework.web.bind.annotation.RequestParam;

        import static org.elasticsearch.index.query.QueryBuilders.*;
        import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;


public class Test {

    public static void main2(String[] args)     {
//        TransportClient client = ESDataUtil.getTransportClient();
//        // Query
//        QueryBuilder queryBuilder = QueryBuilders.termQuery("area_name", "0301");
//        QueryBuilder queryBuilder1 = QueryBuilders.termQuery("statistic_time", "1491408000");
//
//        QueryBuilder clqb  =  boolQuery()
//                .must(QueryBuilders.termQuery("area_name","0301"))
//                .must(QueryBuilders.termQuery("statistic_time", "1491408000"));
//
//        // Search
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("lpl");
//
//        FieldSortBuilder sort = new FieldSortBuilder("statistic_time");
//        sort.ignoreUnmapped(true);
//        sort.order(SortOrder.DESC);
//
//        searchRequestBuilder.setTypes("t_project_dvb_channel_group_day").setQuery(clqb);
//
//        SearchResponse searchResponse = searchRequestBuilder.setSize(100).execute().actionGet();
//        queryResult(searchResponse);

        Test t = new Test();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");

        String timestamp = Integer.parseInt(t.getKpiTimeES("month","20160201"))+"";
        String timestamp1 = Integer.parseInt(t.getKpiTimeES("month","20170201"))+"";

        Date startDate = new Date(Long.valueOf(timestamp+"000"));
        Date endDate = new Date(Long.valueOf(timestamp1+"000"));

        long dd = startDate.getTime();

        while(dd < convertMonth(endDate,0).getTime()){

            dd =  convertMonth(new Date(dd),1).getTime();
            System.out.println(dd);
        }

        //convertESDate(timestamp,timestamp1);

    }

    private static String [] convertESDate(String startTime,String endTime,String period) {
        Test t = new Test();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        List indies = new ArrayList();
        try {

            Date startDate = new Date(Long.valueOf(startTime+"000"));
            String startDateStr = dateFormat.format(startDate);

            int startYear = Integer.parseInt(startDateStr.substring(0, 4));
            int startMonth = Integer.parseInt(startDateStr.substring(4, 6));

            if (endTime == null) {
                if (period.equals("year")) {
                    indies.add(startYear + "12");
                } else if (period.equals("quarter")) {
                    indies.add(startYear + "" + (startMonth + 2));
                } else {
                    indies.add(startDateStr);
                }
            } else {
                Date endDate = new Date(Long.valueOf(endTime+"000"));
                String endDateStr = dateFormat.format(endDate);

                int endYear = Integer.parseInt(endDateStr.substring(0, 4));
                int endMonth = Integer.parseInt(endDateStr.substring(4, 6));


                if (period.equals("year")) {
                    while (startYear >= endYear) {
                        indies.add(startYear + "12");
                        startYear++;
                    }
                } else if (period.equals("quarter")) {
                    long startTimes = startDate.getTime();
                    while(startTimes < convertMonth(endDate,2).getTime()){
                        startTimes =  convertMonth(new Date(startTimes),2).getTime();
                        Date esDate = new Date(startTimes);
                        indies.add(dateFormat.format(esDate));
                        System.out.println(dateFormat.format(esDate));
                    }

                } else {

                    indies.add(startDateStr);

                    long startTimes = startDate.getTime();
                    while(startTimes < convertMonth(endDate,0).getTime()){
                        startTimes =  convertMonth(new Date(startTimes),1).getTime();
                        Date esDate = new Date(startTimes);
                        indies.add(dateFormat.format(esDate));
                        System.out.println(dateFormat.format(esDate));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String[])indies.toArray(new String[indies.size()]);
    }

    private static Date convertMonth(Date startDate,int count) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + count);
        Date date = cal.getTime();
        return date;
    }

    private String getKpiTimeES( String period,  String kpiTime) {
        try {
            if (NumberUtils.isNumber(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return dateFormat.parse(kpiTime).getTime() / 1000 + "";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                return dateFormat.parse(DateUtil.getTransdate(period,kpiTime)).getTime() / 1000 + "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * Term Query
     * @param client
     * @param index
     * @param type
     */
    public static void termQuery(Client client, String index, String type) {
        // Query
        QueryBuilder queryBuilder = QueryBuilders.termQuery("program", "初一英语集锦");
        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        queryResult(searchResponse);
    }

    /**
     * 返回结果
     *
     * @param searchResponse
     */
    public static void queryResult(SearchResponse searchResponse) {
        // 结果
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        System.out.println("--termMatch size -> "+searchHits.length);
        for (SearchHit searchHit : searchHits) {
            System.out.println("--hit source ->" + searchHit.getSource());
        } // for
    }
}
