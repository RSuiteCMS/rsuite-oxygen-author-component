package com.rsicms.rsuite.editors.oxygen.applet.common;

public class OxygenIntegrationException extends java.lang.Exception 
{
    /** Serial uid. */
	private static final long serialVersionUID = 5969142184323355051L;

	/**
     * Constructs an TmException with the specified detail message.
     * @param msg the detail message.
     */
    public OxygenIntegrationException (String msg) 
    {
        super(msg);
    }
    
    /**
     * Constructs an instance of TmException wrapping the passed Exception.
     * @param err the passed exception.
     */
    public OxygenIntegrationException (Throwable err)
    {
         super (err);
    }

    /**
     * Constructs an instance of TmException wrapping the passed Exception.
     * @param msg the detail message.
     * @param err the passed exception.
     */
    public OxygenIntegrationException (String msg, Throwable err)
    {
         super (msg, err);
    }
}
