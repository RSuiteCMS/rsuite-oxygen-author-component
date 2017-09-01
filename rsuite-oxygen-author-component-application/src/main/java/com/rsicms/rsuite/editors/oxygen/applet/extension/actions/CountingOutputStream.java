package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class CountingOutputStream extends FilterOutputStream {

	 private final SaveProgressListener listener;

     private long transferred;

     public CountingOutputStream(final OutputStream out,
             final SaveProgressListener listener) {
         super(out);
         this.listener = listener;
         this.transferred = 0;
     }

     public void write(byte[] b, int off, int len) throws IOException {
         out.write(b, off, len);
         this.transferred += len;
         this.listener.updateTransferStatus(this.transferred);
     }

     public void write(int b) throws IOException {
         out.write(b);
         this.transferred++;
         this.listener.updateTransferStatus(this.transferred);
     }
 }
