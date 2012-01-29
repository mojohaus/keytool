package org.codehaus.mojo.keytool.requests;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

/**
 * Request to print the content of a certificate using the KeyTool tool.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolPrintCertificateRequest
    extends AbstractKeyToolRequest
{

    /**
     * Output in RFC style.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean rfc;

    /**
     * Input file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private File file;

    /**
     * SSL server host and port.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String sslserver;

    /**
     * Signed jar file.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private File jarfile;

    /**
     * Gets the value of the {@link #rfc} field.
     *
     * @return the value of the {@link #rfc} field
     */
    public boolean isRfc()
    {
        return rfc;
    }

    /**
     * @param rfc value of the field {@link #rfc} to set
     */
    public void setRfc( boolean rfc )
    {
        this.rfc = rfc;
    }

    /**
     * Gets the value of the {@link #file} field.
     *
     * @return the value of the {@link #file} field
     */
    public File getFile()
    {
        return file;
    }

    /**
     * @param file value of the field {@link #file} to set
     */
    public void setFile( File file )
    {
        this.file = file;
    }

    /**
     * Gets the value of the {@link #sslserver} field.
     *
     * @return the value of the {@link #sslserver} field
     */
    public String getSslserver()
    {
        return sslserver;
    }

    /**
     * @param sslserver value of the field {@link #sslserver} to set
     */
    public void setSslserver( String sslserver )
    {
        this.sslserver = sslserver;
    }

    /**
     * Gets the value of the {@link #jarfile} field.
     *
     * @return the value of the {@link #jarfile} field
     */
    public File getJarfile()
    {
        return jarfile;
    }

    /**
     * @param jarfile value of the field {@link #jarfile} to set
     */
    public void setJarfile( File jarfile )
    {
        this.jarfile = jarfile;
    }

}
