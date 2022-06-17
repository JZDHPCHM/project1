package gbicc.irs.fbinterface.jpa.support.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 实体类设置值
 * 
 * @author songxubei
 * @version v1.0 2019年9月17日
 */
public class FbBeanSetValueUtil{
    
    /**
     * 实体类设置值
     * resultMap为AppUtil.getMap，其中data为request请求结果
     * @param companyId
     * @param clz
     * @param resultMap
     * @param entityMap 
     * @return
     * @throws Exception
     */
    public static <T> List<T> setOneEntityValueByRequestMap(String companyId, Class<T> clz,
            Map<String, Object> requestMap, Map<String, String> entityMap) throws Exception{
        
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        
        return setOneEntityValueByJsonObject(companyId, clz, jsonObject, entityMap);
        
    }
    /**
     * 实体类设置值，适用于jsonObject为单实体类的数组
     *
     * @param companyId
     * @param clz
     * @param jsonObject
     * @param entityMap
     * @return
     * @throws Exception
     */
    public static <T> List<T> setOneEntityValueByJsonObject(String companyId, Class<T> clz,
            JSONObject jsonObject, Map<String, String> entityMap) throws Exception{
        JSONArray jsonArray = jsonObject.getJSONArray(FbInterfaceEnums.HITS.getValue());
        
        if(jsonArray.size()<=0) {
            return null;
        }
        List<T> list = new ArrayList<>();
        int size = jsonArray.size();
        for(int i=0;i<size;i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            T entity = setValue(clz, object, entityMap);
            entity = setCompanyId(entity,companyId);
            list.add(entity);
        }
        return list;
    }

    /**
     * 为实体类中companyId字段设置值
     *
     * @param entity
     * @param companyId
     * @return
     * @throws Exception
     */
    private static<T> T setCompanyId(T entity,String companyId) throws Exception{
        Field field = entity.getClass().getDeclaredField(FbCommonEnums.ENTITY_COMPANY_ID.getValue());
        field.setAccessible(true);
        field.set(entity, companyId);
        return entity;
    }
    /**
     * 实体类设置值，适用于jsonObject为单实体类对象
     *
     * @param clz
     * @param jsonObject
     * @param entityMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static<T> T setValue(Class<T> clz,JSONObject jsonObject,Map<String, String> entityMap) throws Exception{
        T entity = clz.newInstance();
        Set<String> keys = jsonObject.keySet();
        for(String key:keys) {
            
            if(jsonObject.get(key)==null || FbCommonUtil.stringIsNotValid(jsonObject.get(key).toString()) || jsonObject.get(key).toString().equals(FbInterfaceEnums.JSONOBJECT_NULL.getValue())) {
                continue;
            }
            
            String columnName = entityMap.get(key);
            if(FbCommonUtil.stringIsNotValid(columnName)) {
                continue;
            }
            Field field = clz.getDeclaredField(columnName);
            field.setAccessible(true);
            
            if(field.getGenericType().toString().equals(FbCommonEnums.BIGDECIMAL_TYPE.getValue())) {
                field.set(entity,new BigDecimal(jsonObject.get(key).toString()));
            }else if(field.getGenericType().toString().equals(FbCommonEnums.LONG_TYPE.getValue())){
                field.set(entity, Long.parseLong(jsonObject.get(key).toString()));
            }else if(field.getGenericType().toString().equals(FbCommonEnums.INTEGER_TYPE.getValue())){
                field.set(entity, Integer.parseInt(jsonObject.get(key).toString()));
            }else {
                field.set(entity,jsonObject.get(key).toString());
            }
        }
        
        return entity;
    }
    
    /**
     * 游标翻页递归获取所有数据
     * getRecrusionJsonResultMap
     * @param clz
     * @param resultList
     * @param companyId
     * @param url
     * @param pageId
     * @param entityMap
     * @param accessLogService
     * @return
     * @throws Exception
     */
    public static<T> Map<String, Object> getJsonResultMap(Class<T> clz, List<T> resultList, String companyId,
            String url, String pageId, Map<String, String> entityMap,AccessLogService accessLogService) throws Exception {
        
        //获取封装url
        Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrl(url,pageId);
        
        if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
            accessLogService.failed(clz.getName(), AccessType.ADD, null,null, requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
            return requestUrlMap;
        }
        //获取请求结果
        Map<String, Object> requestMap = FbHttpUtil.requestGet(requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getJsonResultMap(clz,resultList,companyId, url, pageId,entityMap,accessLogService);
                
            }
            accessLogService.failed(clz.getName(), AccessType.ADD, null,null, requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
            return requestMap;
        }
        //判断请求结果是否有下一页，是否继续请求
        Map<String, Object> pageMap = getRequestMapContinue(requestMap);
        if(!FbCommonUtil.getApputilMapFlag(pageMap)) {
            accessLogService.success(requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), AccessType.ADD, null, requestMap);
            return AppUtil.getMap(true,resultList);
        }else {
            //jsonArray入库
            resultList.addAll(setOneEntityValueByRequestMap(companyId, clz, requestMap, entityMap));
            accessLogService.success(requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), AccessType.ADD, null, requestMap);
            //pageId递归
            return getJsonResultMap(clz,resultList,companyId, url, pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(),entityMap,accessLogService);
        }
    }
    
    /**
     * 获取请求结果next_page_id
     *
     * @param requestMap
     * @return
     */
    public static Map<String, Object> getRequestMapContinue(Map<String, Object> requestMap) {
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        //结果
        JSONArray jsonArray = jsonObject.getJSONArray(FbInterfaceEnums.HITS.getValue());
        String pageId = (String) jsonObject.get(FbInterfaceEnums.NEXT_PAGE_ID.getValue());
        if(jsonArray.size()==0 || FbCommonUtil.stringIsNotValid(pageId)) {
            return AppUtil.getMap(false);
        }else {
            return AppUtil.getMap(true,pageId);
        }
    }
    
    /**
     * 设置实体数组entity
     *
     * @param jsonObject
     * @param key
     * @param clz
     * @param service
     * @param entityMap
     * @param columnValueMap
     * @throws Exception
     */
    public static <T> List<T> saveDetailArrayEntity(JSONObject jsonObject, String key, Class<T> clz, Map<String, String> entityMap,Map<String, String> columnValueMap) throws Exception{
        List<T> list = new ArrayList<>();
        if(jsonObject.get(key)!=null) {
            JSONArray array = jsonObject.getJSONArray(key);
            if(array!=null && array.size()>0) {
                for(int i=0;i<array.size();i++) {
                    JSONObject entityObject = array.getJSONObject(i);
                    T entity = FbBeanSetValueUtil.setValue(clz, entityObject, entityMap);
                    for(Entry<String, String> entry:columnValueMap.entrySet()) {
                        String columnName = entry.getKey();
                        String columnValue = entry.getValue();
                        Field column = clz.getDeclaredField(columnName);
                        column.setAccessible(true);
                        column.set(entity, columnValue);
                    }
                    list.add(entity);
                }
            }
        }
        return list;
    }
    
    /**
     * 设置实体对象entity
     *
     * @param jsonObject
     * @param key
     * @param clz
     * @param service
     * @param entityMap
     * @param columnValueMap
     * @throws Exception
     */
    public static <T> List<T> saveDetailObjectEntity(JSONObject jsonObject, String key, Class<T> clz, Map<String, String> entityMap, Map<String, String> columnValueMap) throws Exception{
        List<T> list = new ArrayList<>(); 
        if(jsonObject.get(key)!=null && !jsonObject.get(key).toString().equals(FbInterfaceEnums.JSONOBJECT_NULL.getValue())) {
            JSONObject entityObject = jsonObject.getJSONObject(key);
            T entity = FbBeanSetValueUtil.setValue(clz, entityObject, entityMap);
            
            for(Entry<String, String> entry:columnValueMap.entrySet()) {
                String columnName = entry.getKey();
                String columnValue = entry.getValue();
                Field column = clz.getDeclaredField(columnName);
                column.setAccessible(true);
                column.set(entity, columnValue);
            }
            list.add(entity);
        }
        return list;
    }
    /**
     * 设置
     *
     * @param jsonObject
     * @param key
     * @param clz
     * @param service
     * @param fixedColumnName 固定字段名称
     * @param columnValueMap
     * @throws Exception
     */
    public static <T> List<T> saveDetailStringArrayEntity(JSONObject jsonObject, String key, Class<T> clz, String fixedColumnName, Map<String, String> columnValueMap) throws Exception{
        List<T> list = new ArrayList<>();
        if(jsonObject.get(key)!=null) {
            //获取公告来源
            JSONArray entityArray = jsonObject.getJSONArray(key);
            if(entityArray!=null && entityArray.size()>0) {
                for(int k=0;k<entityArray.size();k++) {
                    T entity = clz.newInstance();
                    Field fixedColumn = clz.getDeclaredField(fixedColumnName);
                    fixedColumn.setAccessible(true);
                    fixedColumn.set(entity, entityArray.get(k));
                    for(Entry<String, String> entry:columnValueMap.entrySet()) {
                        String columnName = entry.getKey();
                        String columnValue = entry.getValue();
                        Field column = clz.getDeclaredField(columnName);
                        column.setAccessible(true);
                        column.set(entity, columnValue);
                    }
                    list.add(entity);
                }
            }
        }
        return list;
    }
    
    /**
     * 批量插入数据
     *
     * @param list
     * @param clz
     * @param jdbcTemplate
     * @return
     * @throws Exception
     */
    public static <T> Map<String, Object> saveDataToDB(List<T> list,Class<T> clz, JdbcTemplate jdbcTemplate) throws Exception{
        if(CollectionUtils.isEmpty(list)) {
            return AppUtil.getMap(true);
        }
        
        String sql = "";
        String values = "";
        
        Field[] fields = clz.getDeclaredFields();
        int length = fields.length-1;
        Table table = clz.getAnnotation(Table.class);
        sql = "insert into " + table.name() + "(";
        values = " values(";
        for(Field field:fields) {
            if(field.getName().equals("serialVersionUID")) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            sql += column.name() + ",";
            values += "?,";
        }
        sql += "fd_creator,fd_create_date,fd_last_modifier,fd_last_modifydate)";
        values += "?,?,?,?)";
        
        jdbcTemplate.batchUpdate(sql+values, new BatchPreparedStatementSetter() {
            
            @SuppressWarnings("unlikely-arg-type")
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                T entity = list.get(i);
                for(int j=1;j<=length;j++) {
                    try {
                        Field field = fields[j];
                        field.setAccessible(true);
                        if(field.getName().equalsIgnoreCase("id") && (field.get(entity)==null || FbCommonUtil.stringIsNotValid(field.get(entity).toString()))) {
                            ps.setString(j, UUID.randomUUID().toString());
                        }else if(field.getGenericType().equals(FbCommonEnums.INTEGER_TYPE.getValue())) {
                            ps.setInt(j, field.getInt(entity));
                        }else if(field.getGenericType().equals(FbCommonEnums.BIGDECIMAL_TYPE.getValue())) {
                            ps.setBigDecimal(j, new BigDecimal(field.get(entity).toString()));
                        }else if(field.getGenericType().equals(FbCommonEnums.LONG_TYPE.getValue())) {
                            ps.setLong(j, field.getLong(entity));
                        }else {
                            ps.setString(j, field.get(entity)==null?"":field.get(entity).toString());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                ps.setString(length+1, SecurityUtil.getLoginName());
                ps.setTimestamp(length+2, new java.sql.Timestamp(new Date().getTime()));
                ps.setString(length+3, SecurityUtil.getLoginName());
                ps.setTimestamp(length+4, new java.sql.Timestamp(new Date().getTime()));
            }
          
            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
        
        return AppUtil.getMap(true,FbCommonEnums.SAVE_SUCCESS.getValue());
    }
    
    /**
     * 封装请求结果到Map<type,List<T>>
     *
     * @param jsonObject
     * @param method
     * @param columnValueMap
     * @param entityMap
     * @param type
     * @param clz
     * @param resultMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> parseToMap(JSONObject jsonObject, String method, Map<String,String> columnValueMap, Object entityMap, String type,Class<T> clz, Map<String,Object> resultMap) throws Exception{
        List<T> newEntityList = new ArrayList<>();
        if(FbCommonUtil.stringIsNotValid(method)) {
            return resultMap;
        }if(method.equals("array")) {
            newEntityList = saveDetailArrayEntity(jsonObject, type, clz, (Map<String, String>)entityMap, columnValueMap);
        }else if(method.equals("object")){
            newEntityList = saveDetailObjectEntity(jsonObject, type, clz, (Map<String, String>)entityMap, columnValueMap);
        }else {
            newEntityList = saveDetailStringArrayEntity(jsonObject, type, clz, entityMap.toString(), columnValueMap);
        }
        //以下用来判断map中是否有同类型数据
        List<T> oldEntityList = (List<T>) resultMap.get(type);
        
        if(CollectionUtils.isEmpty(oldEntityList)) {
            resultMap.put(type, newEntityList);
        }else {
            oldEntityList.addAll(newEntityList);
            resultMap.put(type, oldEntityList);
        }
        return resultMap;
    }
    
    /**
     * 保存数据进数据库
     *
     * @param entityResultMap 结果
     * @param useMapList 对应map类型和实体
     * @param jdbcTemplate
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void saveDataToDB(Map<String, Object> entityResultMap,List<Map<String, Object>> useMapList,JdbcTemplate jdbcTemplate) throws Exception {
        
        if(entityResultMap.isEmpty()) {
            return;
        }
        
        for(Map<String,Object> entrySet:useMapList) {
            if(entityResultMap.get(entrySet.get("type").toString())==null) {
                continue;
            }
            saveDataToDB((List)entityResultMap.get(entrySet.get("type").toString()), (Class)entrySet.get("entity"), jdbcTemplate);
        }
    }
    /**
     * 合并map结果
     *
     * @param oldEntityResultMap
     * @param newEntityResultMap
     * @param useMaplist
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> mergeMap(Map<String, Object> oldEntityResultMap, Map<String, Object> newEntityResultMap,
            List<Map<String, Object>> useMaplist) {
        if(oldEntityResultMap==null || oldEntityResultMap.isEmpty()) {
            return newEntityResultMap;
        }
        if(newEntityResultMap==null || newEntityResultMap.isEmpty()) {
            return oldEntityResultMap;
        }
        
        if(!oldEntityResultMap.isEmpty() && !newEntityResultMap.isEmpty()) {
            for(Map<String, Object> entrySet:useMaplist) {
                List oldList = (List) oldEntityResultMap.get(entrySet.get("type"));
                List newList = (List) newEntityResultMap.get(entrySet.get("type"));
                if(CollectionUtils.isEmpty(oldList) && CollectionUtils.isEmpty(newList)) {
                    continue;
                }
                if(CollectionUtils.isNotEmpty(oldList) && CollectionUtils.isNotEmpty(newList)) {
                    oldList.addAll(newList);
                }
                if(CollectionUtils.isEmpty(oldList) && CollectionUtils.isNotEmpty(newList)) {
                    oldList = new ArrayList<>();
                    oldList.addAll(newList);
                }
                oldEntityResultMap.put(entrySet.get("type").toString(), oldList);
            }
        }
        return oldEntityResultMap;
        
    }
}
