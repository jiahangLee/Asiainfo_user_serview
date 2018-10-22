package dolphin.account.service;


import dolphin.account.domain.dict.Dict;
import dolphin.account.domain.dict.DictRepository;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by lyl on 16/5/30.
 */

@Service
public class DictService {

    private static Logger logger= LoggerFactory.getLogger(DictService.class);
    private String cmExcelFileName="cm_dict.xls";

    private Map<String,List<Dict>> dictMap=new HashMap();


    private Map<Integer,Dict> dictIdMap=new HashMap();

    private int dictId=0;

    private int dictGroupId=0;

    private DictRepository dictRepository;



    @Autowired
    public DictService(DictRepository dictRepository){
        this.dictRepository=dictRepository;
    }

    @PostConstruct
    private void postInit() throws Exception{
        List<Dict> list=this.dictRepository.findAll();
        for(Dict d:list){
            saveDict(d,false);
            dictId=Math.max(d.getId(),dictId);
            dictGroupId=Math.max(d.getGroupId(), dictGroupId);
        }

        dictId++;
    }

    public void initFromFile() throws Exception{
        //Resource resource=new ClassPathResource("classpath*:"+cmExcelFileName);
        //ResourceLoader resourceLoader=new ClassRelativeResourceLoader();
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        resourceResolver.getResource("/"+cmExcelFileName);

        Workbook wb = WorkbookFactory.create(resourceResolver.getResource("/"+cmExcelFileName).getInputStream());
        processDicPublicSheet(wb.getSheet("T_DIC_PUBLIC"));
    }

    public List<Dict> getDicts(String groupName){
        return dictMap.get(groupName);
    }


    public List<Dict> getDicts(){
        List<Dict> list=new ArrayList<>();
        for(List<Dict> l:dictMap.values()){
            list.addAll(l);
        }
        return list;
    }

    private List<Dict> getDictsFallback(){
        return new ArrayList<>();
    }

    public Dict saveDict(Dict dict, boolean saveToDb){
        if(dict.getId()==0){
            dict.setId(dictId++);
        }

        if(dict.getGroupId()==0){
            dict.setGroupId(++dictGroupId);
        }


        List<Dict> list=dictMap.get(dict.getGroupName());
        if(list==null){
            list=new ArrayList<>();
            dictMap.put(dict.getGroupName(), list);
        }
        Iterator<Dict> iter=list.iterator();
        while(iter.hasNext()){
            Dict d=iter.next();
            if(d.getId()==dict.getId()){
                iter.remove();
            }
        }
        list.add(dict);

        dictIdMap.put(dict.getId(), dict);
        if(saveToDb) {
            this.dictRepository.save(dict);
        }

        return dict;
    }

    public Dict deleteDict(int id){
        Dict dict=dictIdMap.get(id);
        if(dict!=null){
            List<Dict> list=dictMap.get(dict.getGroupName());
            if(list!=null){
                Iterator<Dict> iter=list.iterator();
                while(iter.hasNext()){
                    Dict d=iter.next();
                    if(d.getId()==dict.getId()){
                        iter.remove();
                    }
                }
            }
        }
        this.dictRepository.delete(dict);
        return dict;
    }





    private void processDicPublicSheet(Sheet sheet){
        logger.debug("dicPublic begin");

        Iterator<Row> rowIterator= sheet.rowIterator();
        boolean firstRow=true;

        while(rowIterator.hasNext()){
            Row row=rowIterator.next();
            if(firstRow){
                firstRow=false;
                continue;
            }

            Dict dict=new Dict();
            dict.setId(dictId++);
            dict.setGroupId(new Float(Float.parseFloat(getCellValue(row.getCell(0)))).intValue());
            dict.setGroupName(getCellValue(row.getCell(1)));
            dict.setGroupLabel(getCellValue(row.getCell(2)));
            dict.setItemId(new Float(Float.parseFloat(getCellValue(row.getCell(3)))).intValue());
            dict.setItemName(getCellValue(row.getCell(4)));
            dict.setItemLabel(getCellValue(row.getCell(5)));
            dict.setFromExcel(true);

            List<Dict> list=dictMap.get(dict.getGroupName());
            if(list==null){
                list=new ArrayList<>();
                dictMap.put(dict.getGroupName(), list);
            }
            list.add(dict);

            dictIdMap.put(dict.getId(), dict);

            dictGroupId=Math.max(dict.getGroupId(), dictGroupId);

            saveDict(dict, true);
        }

        logger.debug("dicPublic end");

    }


    private static String getCellValue(Cell cell) {
        String str = null;
        if(cell==null){
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                str = "";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                str = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                str = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                str = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                str = String.valueOf(cell.getStringCellValue());
                break;
            default:
                str = null;
                break;
        }
        return str;
    }
}
