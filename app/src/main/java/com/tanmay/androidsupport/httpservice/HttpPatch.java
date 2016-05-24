package com.tanmay.androidsupport.httpservice;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * Created by TaNMay on 3/3/2016.
 */
public class HttpPatch extends HttpEntityEnclosingRequestBase {

    public final static String METHOD_NAME = "PATCH";

    public HttpPatch() {
        super();
    }

    public HttpPatch(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpPatch(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}
