/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.comment.business;

import fr.paris.lutece.portal.service.rbac.RBACResource;

/**
 *
 * CommentValue
 *
 */
public class CommentValue implements RBACResource
{
    public static final String COMMENT_RESOURCE_TYPE = "COMMENT";
    private static final String SEPARATOR = "-";
    private int _nIdResourceHistory;
    private int _nIdTask;
    private String _strValue;

    /**
     *
     * @return the coment value id
     */
    public int getIdResourceHistory( )
    {
        return _nIdResourceHistory;
    }

    /**
     * the comment value id
     * 
     * @param id
     *            the comment value id
     */
    public void setIdResourceHistory( int id )
    {
        _nIdResourceHistory = id;
    }

    /**
     *
     * @return the task id
     */
    public int getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * the task id
     * 
     * @param idTask
     *            the task id
     */
    public void setIdTask( int idTask )
    {
        _nIdTask = idTask;
    }

    /**
     * return the value
     * 
     * @return the value
     */
    public String getValue( )
    {
        return _strValue;
    }

    /**
     * set the value
     * 
     * @param value
     *            the value
     */
    public void setValue( String value )
    {
        _strValue = value;
    }

    @Override
    public String getResourceId( )
    {
        StringBuilder sb = new StringBuilder( _nIdResourceHistory );
        sb.append( SEPARATOR ).append( _nIdTask );

        return sb.toString( );
    }

    @Override
    public String getResourceTypeCode( )
    {
        return COMMENT_RESOURCE_TYPE;
    }
}
