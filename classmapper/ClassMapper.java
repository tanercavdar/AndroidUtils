package tr.com.bracket.trade.utils.classmapper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import tr.com.bracket.trade.utils.annos.DataField;


public abstract class ClassMapper<TDataSourceType,TModel> {

    public static final String FIELD_TYPE_STRING = "string";
    public static final String FIELD_TYPE_BOOLEAN = "boolean";
    public static final String FIELD_TYPE_INTEGER = "integer";
    public static final String FIELD_TYPE_DOUBLE = "double";
    public static final String FIELD_TYPE_DATE = "date";

    private TDataSourceType _dataSource;

    public abstract List<TModel> getList(TDataSourceType dataSource);

    public abstract Object onMap(TDataSourceType dataSource, ClassMapperFieldInfo response) throws Exception;

    public final TModel map(Class<TModel> classModel) throws Exception {
        TModel clazz = classModel.newInstance();
        for (int i = 0; i < classModel.getFields().length; i++) {
            Field field = classModel.getFields()[i];
            DataField anno = field.getAnnotation(DataField.class);
            if (anno != null && !anno.fieldName().isEmpty()) {
                ClassMapperFieldInfo response = new ClassMapperFieldInfo();
                response.anno = anno;
                if (field.getType().getName().equals(String.class.getName())) {
                    response.isString = true;
                    field.set(clazz, onMap(_dataSource, response));
                } else if (field.getType().toString().equals(Integer.class.toString()) || field.getType().toString().equals(Integer.TYPE.toString())) {
                    response.isInteger = true;
                    field.set(clazz, onMap(_dataSource, response));
                } else if (field.getType().toString().equals(Double.class.toString()) || field.getType().toString().equals(Double.TYPE.toString())) {
                    response.isDouble = true;
                    field.set(clazz, onMap(_dataSource, response));
                } else if (field.getType().isAssignableFrom(Date.class)) {
                    response.isDate = true;
                    field.set(clazz, onMap(_dataSource, response));
                } else if (field.getType().toString().equals(Boolean.class.toString()) || field.getType().toString().equals(Boolean.TYPE.toString())) {
                    response.isBoolean = true;
                    field.set(clazz, Boolean.valueOf(onMap(_dataSource, response).toString()));
                }
                //
               //if (Integer.class.isAssignableFrom(classModel) && Integer.TYPE.isAssignableFrom(classModel)) {
               //} else if (Boolean.class.isAssignableFrom(classModel) && Boolean.TYPE.isAssignableFrom(classModel)) {
               //} else if (Double.class.isAssignableFrom(classModel) && Double.TYPE.isAssignableFrom(classModel)) {
               //} else if (Long.class.isAssignableFrom(classModel) && Long.TYPE.isAssignableFrom(classModel)) {
               //} else if (Float.class.isAssignableFrom(classModel) && Float.TYPE.isAssignableFrom(classModel)) {
               //} else if (Short.class.isAssignableFrom(classModel) && Short.TYPE.isAssignableFrom(classModel)) {
               //} else if (Byte.class.isAssignableFrom(classModel) && Byte.TYPE.isAssignableFrom(classModel)) {
               //} else if (Character.class.isAssignableFrom(classModel) && Character.TYPE.isAssignableFrom(classModel)) {
               //    throw new IllegalArgumentException("Unknown primitive type: " + classModel);
               //} else {

               //}
                //
            }
        }
        return clazz;
    }

    public List<TModel> create(TDataSourceType dataSource)  {
        //TODO listeyi döndürmek yerine ResponseModel gibi birşey döndür. map sırasında hatalar olabiliyor.(doğal olarak bunu kullanan classlar da ResponseModel döndürmeli)
        _dataSource = dataSource;
       return getList(_dataSource);
    }

    public class ClassMapperFieldInfo {
        public DataField anno;
        public boolean isBoolean;
        public boolean isString;
        public boolean isDate;
        public boolean isDouble;
        public boolean isInteger;
    }

}
