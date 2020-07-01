
package org.onebeartoe.web.enabled.pixel.controllers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

import org.onebeartoe.network.TextHttpHandler;
import org.onebeartoe.pixel.LogMe;
import org.onebeartoe.pixel.hardware.Pixel;
import org.onebeartoe.web.enabled.pixel.CliPixel;
import org.onebeartoe.web.enabled.pixel.WebEnabledPixel;

/**
 * @author Roberto Marquez
 */
public class ScrollingTextScrollSmoothHttpHandler extends TextHttpHandler
{
    protected WebEnabledPixel application;
    
    public ScrollingTextScrollSmoothHttpHandler(WebEnabledPixel application)
    {
        this.application = application;
    }

    @Override
    protected String getHttpText(HttpExchange exchange)
    {
        LogMe logMe = LogMe.getInstance();
        
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        int i = path.lastIndexOf("/") + 1;
        String s = path.substring(i);

        try {
            if (InetAddress.getByName("pixelcadedx.local").isReachable(5000)){

                System.out.println("Requested: " + requestURI.getPath());
                URL url = new URL("http://pixelcadedx.local:8080" + requestURI.getPath());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.getResponseCode();
                con.disconnect();
            }
        }catch (  Exception e){}
        
        int scrollsmooth = Integer.valueOf(s);
        
        Pixel pixel = application.getPixel();
        pixel.setScrollSmooth(scrollsmooth);
        
        if (!CliPixel.getSilentMode()) {
            System.out.println("scrolling smooth factor received:" + scrollsmooth);
            logMe.aLogger.info("scrolling smooth factor received:" + scrollsmooth);
         }
        
        return "scrolling smooth factor received:" + scrollsmooth;
    }

}


