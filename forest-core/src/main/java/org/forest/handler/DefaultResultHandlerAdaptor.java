package org.forest.handler;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.forest.converter.ForestConverter;
import org.forest.exceptions.ForestHandlerException;
import org.forest.utils.ForestDataType;
import org.forest.http.ForestRequest;
import org.forest.http.ForestResponse;

import java.io.*;
import java.lang.reflect.Type;

/**
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2016-05-25
 */
public class DefaultResultHandlerAdaptor extends ResultHandler {

    public Object getResult(ForestRequest request, ForestResponse response, Type resultType, Class resultClass) {

        HttpEntity entity = response.getHttpResponse().getEntity();
        if (entity != null) {
            try {
                if (void.class.isAssignableFrom(resultClass)) {
                    return null;
                }
                if (ForestResponse.class.isAssignableFrom(resultClass)) {
                    return response;
                }
                if (boolean.class.isAssignableFrom(resultClass) || Boolean.class.isAssignableFrom(resultClass)) {
                    return response.isSuccess();
                }
                if (resultClass.isArray()) {
                    if (byte[].class.isAssignableFrom(resultClass)) {
                        return EntityUtils.toByteArray(entity);
                    }
                }
                String responseText = response.getContent();
                response.setContent(responseText);
                if (CharSequence.class.isAssignableFrom(resultClass)) {
                    return responseText;
                }
                if (InputStream.class.isAssignableFrom(resultClass)) {
                    return entity.getContent();
                }

                ForestDataType dataType = request.getDataType();
                ForestConverter converter = request.getConfiguration().getConverter(dataType);
                return converter.convertToJavaObject(responseText, resultType);

            } catch (IOException e) {
                throw new ForestHandlerException(e, request, response);
            }
        }
        else if (ForestResponse.class.isAssignableFrom(resultClass)) {
            return response;
        }
        return null;
    }
}