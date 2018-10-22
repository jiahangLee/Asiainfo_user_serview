package gags.service.controller;

import com.alibaba.fastjson.JSONObject;
import gags.service.dao.DataReportDAO;
import gags.service.dao.DataReportLogDAO;
import gags.service.entity.DataReport;
import gags.service.entity.DataReportLog;
import gags.service.util.BaseDataUtil;
import gags.service.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import gags.service.dao.RelOperAuthObjDAO;
//import gags.service.dao.RoleDAO;
//import gags.service.entity.RelOperAuthObj;
//import gags.service.util.AESUtil;
//import gags.service.util.BaseDataUtil;

/**
 * Created by zhangzc Lee on 2018/1/31.
 */
@RestController
@RequestMapping("/dataReport")
public class DataReportController {

    @Autowired
    private DataReportDAO dataReportDAO;
    @Autowired
    private DataReportLogDAO dataReportLogDAO;
//    @Autowired
//    private RelOperAuthObjDAO relOperAuthObjDAO;
//    @Autowired
//    private RoleDAO roleDAO;

    private String uploadPath = "file/";

    @RequestMapping(path = "/list")
    public String getDataReportList(@RequestParam Long beginRow,
                                  @RequestParam Long pageSize,
                                  @RequestParam(required = false) String search,
                                  @RequestParam(required = false) String searchType
                                ) {
        Map map = new HashMap();
        DataReport dataReport = new DataReport();
        switch (searchType){
            case "1":
                dataReport.setReport_name(search);
                break;
            case "2":
                dataReport.setOrganize_id(search);
                break;
            case "3":
                dataReport.setKey_word(search);
                break;
            case "4":
                dataReport.setReport_date(search);
                break;
            case "5":
                dataReport.setReport_type(search);
                break;
        }

        dataReport.setBeginRow(beginRow);
        dataReport.setPageSize(pageSize);
        List<DataReport>list = dataReportDAO.list(dataReport);
        Integer listCount = dataReportDAO.listCount(dataReport);
        map.put("data",list);
        map.put("total",listCount);
        return JSONObject.toJSONString(map);
    }



    @RequestMapping(value  = "/update", method=RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map update(  DataReport dataReport,HttpServletRequest request){
        Map result = new HashMap();
        try {
            if( "-1".equals(dataReport.getOrganize_id())||dataReport.getOrganize_id() .equals( "undefined")){
                dataReport.setOrganize_id(null);
            }
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            MultipartFile file = multipartRequest.getFile("file");
            if(file != null){
                String filePath = dataReportDAO.getDataReportByReportId(dataReport.getReport_id()).getFile_save_name();
                this.deleteFile(filePath);
                this.saveFile(dataReport,file);
            }
            dataReportDAO.update(dataReport);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error",e.getMessage());
            return result;
        } finally {
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        return result;
    }
    @RequestMapping(value  = "/add", method=RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map add(DataReport dataReport,  HttpServletRequest request){
        Map result = new HashMap();
        try {
           if(dataReport.getOrganize_id() == null ||dataReport.getOrganize_id() .equals( "undefined")){
                dataReport.setOrganize_id("0");
            }
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            MultipartFile file = multipartRequest.getFile("file");
            this.saveFile(dataReport,file);
            dataReportDAO.insert(dataReport);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error",e.getMessage());
            return result;
        } finally {
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        return result;
    }
    @RequestMapping(path = "/del")
    public Map del( @RequestParam Long reportId){
        Map result = new HashMap();
        if(reportId == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","标识不能为空");
            return result;
        }
        try {
            DataReport dataReport = dataReportDAO.getDataReportByReportId(reportId);
            this.deleteFile(dataReport.getFile_save_name());
            dataReportDAO.delete(reportId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result",BaseDataUtil.FAILD_VALUE);
            result.put("error",e.getMessage());
            return result;
        } finally {
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        return result;
    }

    @RequestMapping(path = "/down", method = RequestMethod.GET)
    public void download(@RequestParam Long reportId, @RequestParam String operatorId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = true;
        try {
            List<DataReportLog> list = dataReportLogDAO.query(operatorId, reportId.toString());
            if (list.size() > 0) {
                DataReportLog reportLog = list.get(0);
                int downTimes = reportLog.getDownloadTimes();
                int maxTimes = reportLog.getMaxTimes();
                if (downTimes >= maxTimes) {
                    flag = false;
                    reportLog.setLockStatus(BaseDataUtil.STATUS_INVALID);
                }
                reportLog.setDownloadTimes(++downTimes);
                dataReportLogDAO.update(reportLog);

            } else {
                DataReportLog dataReportLog = new DataReportLog();
                dataReportLog.setReportId(reportId.toString());
                dataReportLog.setOperatorId(operatorId);
                dataReportLog.setDownloadTimes(1);
                dataReportLogDAO.insert(dataReportLog);
            }
            if (flag) {
                DataReport dataReport = dataReportDAO.getDataReportByReportId(reportId);
                String path = dataReport.getFile_save_name();
                // path是指欲下载的文件的路径。
                File file = new File(path);
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(path));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(dataReport.getFile_name().getBytes(), "ISO-8859-1"));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(path = "/checkDownAuth",method=RequestMethod.GET)
    public Map checkDownAuth(@RequestParam String reportId ,
                             @RequestParam String operatorId
            ,HttpServletRequest request, HttpServletResponse response)  {
        Map<String,String> result = new HashMap<>();
        StringBuffer buffer = new StringBuffer();
        String msg = "";
        String reportIds[] = reportId.split(",");
        try {
            for(String dataReportId : reportIds) {
                List<DataReportLog> list = dataReportLogDAO.query(operatorId, dataReportId.toString());
                if (list.size() > 0) {
                    DataReportLog reportLog = list.get(0);
                    int downTimes = reportLog.getDownloadTimes();
                    int maxTimes = reportLog.getMaxTimes();
                    DataReport dataReport = dataReportDAO.getDataReportByReportId(Long.valueOf(dataReportId));
                    int lockStatus = reportLog.getLockStatus();
                    if (lockStatus == BaseDataUtil.STATUS_INVALID || downTimes >= maxTimes) {
                        buffer.append("【"+dataReport.getReport_name()+"】");
                        msg = "单日下载次数超过上限【" + reportLog.getMaxTimes() + "】次";
                    }
                }
            }
            if(buffer.length() > 0){
                result.put("result", BaseDataUtil.FAILD_VALUE);
                result.put("msg", buffer.toString()+msg);
                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("result",BaseDataUtil.FAILD_VALUE);
            result.put("msg","调用查询权限接口异常");
            return result;
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        result.put("msg","授权成功");
        return result;
    }

    public void saveFile(DataReport report,MultipartFile file) throws Exception{
//        String rootPath=getClass().getResource("/").getFile().toString().substring(1);
//        rootPath = java.net.URLDecoder.decode(rootPath,"utf-8");
        String rootPath = System.getProperty("user.dir")+ File.separator;
        if(file != null){
            if(file.getSize()>0){
                String filename=file.getOriginalFilename();
                report.setFile_name(file.getOriginalFilename());
                try {
                    //创建文件目录
                    File dir = new File(rootPath + uploadPath);
                    if (!dir.exists()&&!dir.isDirectory()) {
                        dir.mkdir();
                    }
                    filename =DateUtil.formatDate(DateUtil.getGlobalDate(),"yyyyMMddHHmmss") +report.getOperator_id()+filename;
                    filename = rootPath + uploadPath+ filename;
                    report.setFile_save_name(filename);
                    FileOutputStream fs=new FileOutputStream(  filename);
                    InputStream stream = file.getInputStream();
                    byte[] buffer =new byte[1024*1024];
                    int bytesum = 0;
                    int byteread = 0;
                    while ((byteread=stream.read(buffer))!=-1)
                    {
                        bytesum+=byteread;
                        fs.write(buffer,0,byteread);
                        fs.flush();
                    }
                    fs.close();
                    stream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new Exception("上传失败");
                }
            }
        }
    }
    public void deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
            }
        }
    }

    @RequestMapping(path = "/down/app",method=RequestMethod.GET)
    public void downloadApp(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = "/opt/zxga_background/app/app-release.apk";
            String fileName = "app-release.apk";
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),"ISO-8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(path = "/down/helpDoc/{id}",method=RequestMethod.GET)
    public void downloadDoc(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) {
        try {
            String path ="/opt/zxga_background/helpDoc/";
            String fileName = "";
            if(id == 100L){
                fileName = "中信国安广视网络大数据系统系统操作手册.docx";
            }else if(id == 200L){
                fileName = "中信国安广视网络大数据平台指标.xls";
            }else if(id == 300L){
                fileName = "中信国安广视大数据中心平台详细设计文档.docx";
            }
            path = path + id;
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),"ISO-8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}