package org.codehaus.mojo.shared.keytool.requests;

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

/**
 * Request to import all entries of a keystore to another keystore using the KeyTool tool.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolImportKeystoreRequest
    extends AbstractKeyToolRequest
{

    /**
     * Source keystore name.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srckeystore;

    /**
     * Destination keystore name.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destkeystore;

    /**
     * Source keystore type.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcstoretype;

    /**
     * Destination keystore type.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String deststoretype;

    /**
     * Source keystore password.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcstorepass;

    /**
     * Destination keystore password.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String deststorepass;

    /**
     * Source keystore password protected.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean srcprotected;

    /**
     * Source keystore provider name.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcprovidername;

    /**
     * Destination keystore provider name.
     * <p/>
     * See <a hresf="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destprovidername;

    /**
     * Source alias.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcalias;

    /**
     * Destination alias.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destalias;

    /**
     * Source key password.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srckeypass;

    /**
     * Destination key password.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destkeypass;

    /**
     * Do not prompt.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean noprompt;

    /**
     * Provider class name.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerclass;

    /**
     * Provider argument.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerarg;

    /**
     * Provider classpath.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerpath;

    public KeyToolImportKeystoreRequest()
    {
        super( "-importkeystore" );
    }

    /**
     * Gets the value of the {@link #srckeystore} field.
     *
     * @return the value of the {@link #srckeystore} field
     */
    public String getSrckeystore()
    {
        return srckeystore;
    }

    /**
     * @param srckeystore value of the field {@link #srckeystore} to set
     */
    public void setSrckeystore( String srckeystore )
    {
        this.srckeystore = srckeystore;
    }

    /**
     * Gets the value of the {@link #destkeystore} field.
     *
     * @return the value of the {@link #destkeystore} field
     */
    public String getDestkeystore()
    {
        return destkeystore;
    }

    /**
     * @param destkeystore value of the field {@link #destkeystore} to set
     */
    public void setDestkeystore( String destkeystore )
    {
        this.destkeystore = destkeystore;
    }

    /**
     * Gets the value of the {@link #srcstoretype} field.
     *
     * @return the value of the {@link #srcstoretype} field
     */
    public String getSrcstoretype()
    {
        return srcstoretype;
    }

    /**
     * @param srcstoretype value of the field {@link #srcstoretype} to set
     */
    public void setSrcstoretype( String srcstoretype )
    {
        this.srcstoretype = srcstoretype;
    }

    /**
     * Gets the value of the {@link #deststoretype} field.
     *
     * @return the value of the {@link #deststoretype} field
     */
    public String getDeststoretype()
    {
        return deststoretype;
    }

    /**
     * @param deststoretype value of the field {@link #deststoretype} to set
     */
    public void setDeststoretype( String deststoretype )
    {
        this.deststoretype = deststoretype;
    }

    /**
     * Gets the value of the {@link #srcstorepass} field.
     *
     * @return the value of the {@link #srcstorepass} field
     */
    public String getSrcstorepass()
    {
        return srcstorepass;
    }

    /**
     * @param srcstorepass value of the field {@link #srcstorepass} to set
     */
    public void setSrcstorepass( String srcstorepass )
    {
        this.srcstorepass = srcstorepass;
    }

    /**
     * Gets the value of the {@link #deststorepass} field.
     *
     * @return the value of the {@link #deststorepass} field
     */
    public String getDeststorepass()
    {
        return deststorepass;
    }

    /**
     * @param deststorepass value of the field {@link #deststorepass} to set
     */
    public void setDeststorepass( String deststorepass )
    {
        this.deststorepass = deststorepass;
    }

    /**
     * Gets the value of the {@link #srcprotected} field.
     *
     * @return the value of the {@link #srcprotected} field
     */
    public boolean isSrcprotected()
    {
        return srcprotected;
    }

    /**
     * @param srcprotected value of the field {@link #srcprotected} to set
     */
    public void setSrcprotected( boolean srcprotected )
    {
        this.srcprotected = srcprotected;
    }

    /**
     * Gets the value of the {@link #srcprovidername} field.
     *
     * @return the value of the {@link #srcprovidername} field
     */
    public String getSrcprovidername()
    {
        return srcprovidername;
    }

    /**
     * @param srcprovidername value of the field {@link #srcprovidername} to set
     */
    public void setSrcprovidername( String srcprovidername )
    {
        this.srcprovidername = srcprovidername;
    }

    /**
     * Gets the value of the {@link #destprovidername} field.
     *
     * @return the value of the {@link #destprovidername} field
     */
    public String getDestprovidername()
    {
        return destprovidername;
    }

    /**
     * @param destprovidername value of the field {@link #destprovidername} to set
     */
    public void setDestprovidername( String destprovidername )
    {
        this.destprovidername = destprovidername;
    }

    /**
     * Gets the value of the {@link #srcalias} field.
     *
     * @return the value of the {@link #srcalias} field
     */
    public String getSrcalias()
    {
        return srcalias;
    }

    /**
     * @param srcalias value of the field {@link #srcalias} to set
     */
    public void setSrcalias( String srcalias )
    {
        this.srcalias = srcalias;
    }

    /**
     * Gets the value of the {@link #destalias} field.
     *
     * @return the value of the {@link #destalias} field
     */
    public String getDestalias()
    {
        return destalias;
    }

    /**
     * @param destalias value of the field {@link #destalias} to set
     */
    public void setDestalias( String destalias )
    {
        this.destalias = destalias;
    }

    /**
     * Gets the value of the {@link #srckeypass} field.
     *
     * @return the value of the {@link #srckeypass} field
     */
    public String getSrckeypass()
    {
        return srckeypass;
    }

    /**
     * @param srckeypass value of the field {@link #srckeypass} to set
     */
    public void setSrckeypass( String srckeypass )
    {
        this.srckeypass = srckeypass;
    }

    /**
     * Gets the value of the {@link #destkeypass} field.
     *
     * @return the value of the {@link #destkeypass} field
     */
    public String getDestkeypass()
    {
        return destkeypass;
    }

    /**
     * @param destkeypass value of the field {@link #destkeypass} to set
     */
    public void setDestkeypass( String destkeypass )
    {
        this.destkeypass = destkeypass;
    }

    /**
     * Gets the value of the {@link #noprompt} field.
     *
     * @return the value of the {@link #noprompt} field
     */
    public boolean isNoprompt()
    {
        return noprompt;
    }

    /**
     * @param noprompt value of the field {@link #noprompt} to set
     */
    public void setNoprompt( boolean noprompt )
    {
        this.noprompt = noprompt;
    }


    /**
     * Gets the value of the {@link #providerclass} field.
     *
     * @return the value of the {@link #providerclass} field
     */
    public String getProviderclass()
    {
        return providerclass;
    }

    /**
     * @param providerclass value of the field {@link #providerclass} to set
     */
    public void setProviderclass( String providerclass )
    {
        this.providerclass = providerclass;
    }

    /**
     * Gets the value of the {@link #providerarg} field.
     *
     * @return the value of the {@link #providerarg} field
     */
    public String getProviderarg()
    {
        return providerarg;
    }

    /**
     * @param providerarg value of the field {@link #providerarg} to set
     */
    public void setProviderarg( String providerarg )
    {
        this.providerarg = providerarg;
    }

    /**
     * Gets the value of the {@link #providerpath} field.
     *
     * @return the value of the {@link #providerpath} field
     */
    public String getProviderpath()
    {
        return providerpath;
    }

    /**
     * @param providerpath value of the field {@link #providerpath} to set
     */
    public void setProviderpath( String providerpath )
    {
        this.providerpath = providerpath;
    }

}
