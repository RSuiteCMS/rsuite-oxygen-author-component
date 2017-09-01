package com.rsicms.rsuite.editors.oxygen.launcher.common;


public class OxygenApplicationException extends java.lang.Exception 
{
    /** Serial uid. */
	private static final long serialVersionUID = 5969142184323355051L;

	/**
     * Constructs an TmException with the specified detail message.
     * @param msg the detail message.
     */
    public OxygenApplicationException (String msg) 
    {
        super(msg);
    }
    
    /**
     * Constructs an instance of TmException wrapping the passed Exception.
     * @param err the passed exception.
     */
    public OxygenApplicationException (Throwable err)
    {
         super (err);
    }

    /**
     * Constructs an instance of TmException wrapping the passed Exception.
     * @param msg the detail message.
     * @param err the passed exception.
     */
    public OxygenApplicationException (String msg, Throwable err)
    {
         super (msg, err);
    }
}
