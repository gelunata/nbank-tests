package api.dao.comparison;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class DaoComparator {
    private final DaoComparisonConfigLoader configLoader;

    public DaoComparator() {
        this.configLoader = new DaoComparisonConfigLoader("dao-comparison.properties");
    }

    public void compare(Object apiResponse, Object dao) {
        DaoComparisonConfigLoader.DaoComparisonRule rule = configLoader.getRuleFor(apiResponse.getClass());

        if (rule == null) {
            throw new RuntimeException("No comparison rule found for " + apiResponse.getClass().getSimpleName());
        }

        Map<String, String> fieldMappings = rule.getFieldMappings();

        for (Map.Entry<String, String> mapping : fieldMappings.entrySet()) {
            String apiFieldName = mapping.getKey();
            String daoFieldName = mapping.getValue();

            Object apiValue = getFieldValue(apiResponse, apiFieldName);
            Object daoValue = getFieldValue(dao, daoFieldName);

            if (!Objects.equals(apiValue, daoValue)) {
                throw new AssertionError(String.format(
                        "Field mismatch for %s: API=%s, DAO=%s",
                        apiFieldName, apiValue, daoValue));
            }
        }
    }

    private Object getFieldValue(Object obj, String fieldName) {
        // Разделяем на части по точке
        String[] parts = fieldName.split("\\.");
        Object current = obj;

        for (String part : parts) {
            if (current == null) return null;

            // Если часть содержит индекс [n]
            if (part.contains("[")) {
                current = getIndexedValue(current, part);
            } else {
                current = getDirectFieldValue(current, part);
            }
        }

        return current;
    }

    private static Object getIndexedValue(Object obj, String fieldWithIndex) {
        // Извлекаем имя поля и индекс
        int bracketIndex = fieldWithIndex.indexOf('[');
        String fieldName = fieldWithIndex.substring(0, bracketIndex);
        int index = Integer.parseInt(
                fieldWithIndex.substring(bracketIndex + 1, fieldWithIndex.indexOf(']'))
        );

        // Получаем коллекцию/массив
        Object collection = getDirectFieldValue(obj, fieldName);
        if (collection == null) return null;

        // Обрабатываем список
        if (collection instanceof List) {
            List<?> list = (List<?>) collection;
            return index >= 0 && index < list.size() ? list.get(index) : null;
        }

        // Обрабатываем массив
        if (collection.getClass().isArray()) {
            Object[] array = (Object[]) collection;
            return index >= 0 && index < array.length ? array[index] : null;
        }

        throw new RuntimeException("Field '" + fieldName + "' is not a collection or array");
    }

    private static Object getDirectFieldValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }

        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access field: " + fieldName, e);
            }
        }
        throw new RuntimeException("Field not found: " + fieldName + " in class " + obj.getClass().getName());
    }

    private static class Objects {
        public static boolean equals(Object a, Object b) {
            return (a == b) || (a != null && a.equals(b));
        }
    }
}
