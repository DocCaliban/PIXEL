package org.onebeartoe.web.enabled.pixel.controllers;

import com.sun.net.httpserver.HttpExchange;
import ioio.lib.api.exception.ConnectionLostException;
import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
import java.net.URI;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.network.TextHttpHandler;
import org.onebeartoe.pixel.LogMe;
import org.onebeartoe.web.enabled.pixel.WebEnabledPixel;

/**
 * @author Roberto Marquez
 */
public abstract class ImageResourceHttpHandler extends TextHttpHandler
{
    protected String basePath;
    protected String defaultImageClassPath;
    protected String modeName;
    protected WebEnabledPixel application;
    protected Logger logger;
    LogMe logMe = null;
        
    public ImageResourceHttpHandler(WebEnabledPixel application)
    {
        String name = getClass().getName();
        logger = Logger.getLogger(name);
        
        this.application = application;
        
        //logMe = LogMe.getInstance();
        LogMe logMe = LogMe.getInstance();
    }
    
    @Override
    protected String getHttpText(HttpExchange exchange)
    {        
        String imageClassPath;
        
       
        try
        {
            URI requestURI = exchange.getRequestURI();
            
            String path = requestURI.getPath();
            
            int i = path.lastIndexOf("/") + 1;
            String name = path.substring(i);
            
            if(name.equals(modeName))
            {
                // this is just a request change to still image mode
                imageClassPath = defaultImageClassPath;
            }
             else if( path.contains("/animations/"))
            {
                imageClassPath = requestURI.toString(); //this returns /arcade/stream/mame/pacman?t=1?c=2?r=5
            }
            else if( path.contains("/save/"))
            {
                imageClassPath = path;
            }
            else if( path.contains("/console/"))
            { 
                imageClassPath = requestURI.toString(); //this returns /arcade/stream/mame/pacman?t=1?c=2?r=5
            }
            else if( path.contains("/arcade/"))
            {
                imageClassPath = requestURI.toString(); //this returns /arcade/stream/mame/pacman?t=1?c=2?r=5
            }
             else if( path.contains("/localplayback"))
            {
                imageClassPath = requestURI.toString(); 
            }
            else
            {
                imageClassPath = basePath + name;
                
                //to do need to add text here too
            }
        }
        catch(Exception e)
        {
            imageClassPath = defaultImageClassPath;
            
            String message = "An error occurred while determining the image from the request.  " +
                             "The default is used now.";
            
            logger.log(Level.SEVERE, message, e);
        }

        try
        {
            //System.out.println("loading " + modeName + " image");

            try
            {
                //System.out.println("writing image resource to the Pixel");
                writeImageResource(imageClassPath);
                
                //System.out.println(modeName + " image resource was written to the Pixel");
            } 
            catch (ConnectionLostException ex)
            {
                String message = "connection lost";
                logger.log(Level.SEVERE, message, ex);
            }
        }
        catch (IOException ex)
        {
            String message = "error with image resource";
            logger.log(Level.SEVERE, message, ex);
        }
        finally
        {
            return "REST call received for " + imageClassPath;
        }
    }
    
  
    
    protected abstract void writeImageResource(String imageClassPath) throws IOException, ConnectionLostException;
            
    }
