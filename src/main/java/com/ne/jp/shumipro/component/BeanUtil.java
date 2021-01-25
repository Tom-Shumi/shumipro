package com.ne.jp.shumipro.component;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class BeanUtil extends BeanUtils {

    private BeanUtil() {
    }

    public static void copyProperties(Object dest, Object orig) throws InvocationTargetException, IllegalAccessException {
        if (dest != null && orig != null) {
            ConvertUtils.register(new DateConverter(null), Date.class);
            ConvertUtils.register(null, Timestamp.class);
            ConvertUtils.register(new BooleanConverter(null), Boolean.class);
            ConvertUtils.register(new CharacterConverter(null), Character.class);
            ConvertUtils.register(new ByteConverter(null), Byte.class);
            ConvertUtils.register(new ShortConverter(null), Short.class);
            ConvertUtils.register(new IntegerConverter(null), Integer.class);
            ConvertUtils.register(new LongConverter(null), Long.class);
            ConvertUtils.register(new FloatConverter(null), Float.class);
            ConvertUtils.register(new DoubleConverter(null), Double.class);
            ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
            BeanUtils.copyProperties(dest, orig);
        }
    }
}
