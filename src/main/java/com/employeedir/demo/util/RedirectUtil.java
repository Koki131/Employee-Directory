package com.employeedir.demo.util;

import java.net.URI;
import java.net.URISyntaxException;

public class RedirectUtil {

    public static String extractEndpoint(String url) {
        try {
            URI uri = new URI(url);

            return url.contains("?") ? uri.getPath() + "?" + uri.getQuery() : uri.getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
