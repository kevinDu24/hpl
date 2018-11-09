package cn.net.leadu.utils.commons;

import cn.net.leadu.domain.PurchaseApproval;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zcHu on 2017/05/09.
 */
public class ItemSerialize {


    /**
     * 分割符
     */
    private static final String SEP = "#";


    public static List<PurchaseApproval> serialize(Object obj, String uniqueMark, String version, String userName) throws Exception {
        List<PurchaseApproval> resultList = new ArrayList<>();
        if (obj == null) {
            return resultList;
        }

        serialize(null, obj, resultList);
        for(PurchaseApproval item : resultList){
            item.setUniqueMark(uniqueMark);
            item.setPeriod(version);
            item.setCreateUser(userName);
        }
        return resultList;
    }

    private static void serialize(String parentKey,Object obj, List<PurchaseApproval> resultList) throws Exception {
        Class clazz = obj==null?null:obj.getClass();
        serialize(parentKey,clazz,obj,resultList);

    }

    private static void serialize(String parentKey,Class objType, Object obj, List<PurchaseApproval> resultList) throws Exception {
        if(objType == null){
            if(obj!=null){
                objType = obj.getClass();
            }
        }
        if(objType==null){
            return;
        }
        Field[] declaredFields = objType.getDeclaredFields();
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            ItemColumn itemColumn = field.getAnnotation(ItemColumn.class);
            if (itemColumn == null) {
                continue;
            }
            field.setAccessible(true);

            Object fieldValue = obj==null?null:field.get(obj);

            serialize(parentKey, itemColumn, fieldValue, resultList);
        }
    }


    private static void serialize4List(String parentPathKey, ItemColumn itemColumn, List<Object> valueList, List<PurchaseApproval> resultList) throws Exception {
//        int size = valueList == null?0:valueList.size();
        if(valueList == null){
            return;
        }
        int size = valueList.size();
        PurchaseApproval item = new PurchaseApproval();
        String itemKey = SEP + "COUNT";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(parentPathKey)) {
            itemKey = parentPathKey.trim() + itemKey;
        }
        item.setItemKey(itemKey);
        item.setItemType(ItemDataTypeEnum.IntegerType.getType());
        item.setItemInt32Value(size);
        resultList.add(item);

        if (itemColumn.isListInnerClass()) {
            serialize4ListClass(parentPathKey,itemColumn, valueList, resultList);
            return;
        }
        serialize4ListNormal(parentPathKey, itemColumn.listInnerNormalType().getType(), valueList, resultList);
    }


    private static void serialize4ListNormal(String parentPathKey, int type, List<Object> valueList, List<PurchaseApproval> resultList) throws Exception {
        int size = valueList==null?0:valueList.size();

        for (int i = 0; i < size; i++) {
            String tmpParentKey = parentPathKey + SEP + i;
            serialize4Normal(tmpParentKey, type, valueList.get(i), resultList);
        }
    }

    private static void serialize4ListClass(String parentPathKey, ItemColumn itemColumn,List<Object> valueList, List<PurchaseApproval> resultList) throws Exception {
        int size = valueList==null?0:valueList.size();

        Class<?> classType = itemColumn.listInnerClassType();

        for (int i = 0; i < size; i++) {
            String tmpParentKey = parentPathKey + SEP + i;
            serialize(tmpParentKey,classType,valueList.get(i), resultList);
        }
    }

    private static void serialize(String parentPathKey, ItemColumn itemColumn, Object fieldValue, List<PurchaseApproval> resultList) throws Exception {

        PurchaseApproval item = new PurchaseApproval();
        String itemKey = itemColumn.keyPath();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(parentPathKey)) {
            itemKey = parentPathKey.trim() + itemKey;
        }
        item.setItemKey(itemKey);
        item.setItemType(itemColumn.itemDateType().getType());
        switch (item.getItemType()) {
            case 5:
                serialize(item.getItemKey(),itemColumn.ClassType(),fieldValue, resultList);
                return;
            case 10:
                serialize4List(item.getItemKey(), itemColumn, (List<Object>) fieldValue, resultList);
                return;
            default:
                serialize4Normal(itemKey, item.getItemType(), fieldValue, resultList);
                break;
        }
    }

    private static void serialize4Normal(String pathKey, int type, Object fieldValue, List<PurchaseApproval> resultList) throws Exception {
        if (fieldValue == null) {
            return;
        }
        PurchaseApproval item = new PurchaseApproval();
        item.setItemKey(pathKey);
        item.setItemType(type);
        switch (item.getItemType()) {
            case 1:
                item.setItemStringValue(fieldValue==null?null:String.valueOf(fieldValue));
                break;
            case 2:
                item.setItemInt32Value((Integer) fieldValue);
                break;
            case 3:
                item.setItemInt64Value((Long) fieldValue);
                break;
            case 4:
                item.setItemDataTimeValue((Date) fieldValue);
                break;
            default:
                item.setItemStringValue(fieldValue==null?null:String.valueOf(fieldValue));
                break;
        }
        resultList.add(item);
    }


}
