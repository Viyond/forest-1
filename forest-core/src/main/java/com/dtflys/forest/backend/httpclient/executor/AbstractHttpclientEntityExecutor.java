package com.dtflys.forest.backend.httpclient.executor;

import com.dtflys.forest.backend.httpclient.body.HttpclientBodyBuilder;
import com.dtflys.forest.http.ForestRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import com.dtflys.forest.backend.httpclient.request.HttpclientRequestSender;
import com.dtflys.forest.backend.httpclient.response.HttpclientResponseHandler;

import java.io.*;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2017-04-20 13:22
 */
public abstract class AbstractHttpclientEntityExecutor<T extends HttpEntityEnclosingRequestBase> extends AbstractHttpclientExecutor<T> {

    private static Log log = LogFactory.getLog(HttpclientPostExecutor.class);

    public AbstractHttpclientEntityExecutor(ForestRequest request, HttpclientResponseHandler httpclientResponseHandler, HttpclientRequestSender requestSender) {
        super(request, httpclientResponseHandler, requestSender);
    }

    protected void prepareBodyBuilder() {
        bodyBuilder = new HttpclientBodyBuilder<>();
    }

    @Override
    protected String getLogContentForBody(T httpReq) {
        try {
            InputStream in = httpReq.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line;
            String body;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + " ");
            }
            body = buffer.toString();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
