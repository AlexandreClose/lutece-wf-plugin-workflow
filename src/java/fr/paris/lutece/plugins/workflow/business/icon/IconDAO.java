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
package fr.paris.lutece.plugins.workflow.business.icon;

import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflowcore.business.icon.IIconDAO;
import fr.paris.lutece.plugins.workflowcore.business.icon.Icon;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * IconDAO
 *
 */
public class IconDAO implements IIconDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_icon ) FROM workflow_icon";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_icon,name,mime_type,file_value,width,height" +
        " FROM workflow_icon WHERE id_icon=?";
    private static final String SQL_QUERY_SELECT_ICON = "SELECT id_icon,name,mime_type,width,height" +
        " FROM workflow_icon ORDER BY name DESC  ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO  workflow_icon " +
        "(id_icon,name,mime_type,file_value,width,height)VALUES(?,?,?,?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_icon  SET id_icon=?,name=?,mime_type=?,file_value=?,width=?,height=?" +
        " WHERE id_icon=?";
    private static final String SQL_QUERY_UPDATE_METADATA = "UPDATE workflow_icon  SET id_icon=?,name=?,width=?,height=?" +
        " WHERE id_icon=?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_icon  WHERE id_icon=? ";

    /**
     * Generates a new primary key
     * @param plugin the plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( Icon icon )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, WorkflowUtils.getPlugin(  ) );

        int nPos = 0;
        icon.setId( newPrimaryKey( WorkflowUtils.getPlugin(  ) ) );

        daoUtil.setInt( ++nPos, icon.getId(  ) );
        daoUtil.setString( ++nPos, icon.getName(  ) );
        daoUtil.setString( ++nPos, icon.getMimeType(  ) );
        daoUtil.setBytes( ++nPos, icon.getValue(  ) );
        daoUtil.setInt( ++nPos, icon.getWidth(  ) );
        daoUtil.setInt( ++nPos, icon.getHeight(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Icon icon )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, WorkflowUtils.getPlugin(  ) );

        int nPos = 0;

        daoUtil.setInt( ++nPos, icon.getId(  ) );
        daoUtil.setString( ++nPos, icon.getName(  ) );
        daoUtil.setString( ++nPos, icon.getMimeType(  ) );
        daoUtil.setBytes( ++nPos, icon.getValue(  ) );
        daoUtil.setInt( ++nPos, icon.getWidth(  ) );
        daoUtil.setInt( ++nPos, icon.getHeight(  ) );

        daoUtil.setInt( ++nPos, icon.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeMetadata( Icon icon )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_METADATA, WorkflowUtils.getPlugin(  ) );

        int nPos = 0;

        daoUtil.setInt( ++nPos, icon.getId(  ) );
        daoUtil.setString( ++nPos, icon.getName(  ) );
        daoUtil.setInt( ++nPos, icon.getWidth(  ) );
        daoUtil.setInt( ++nPos, icon.getHeight(  ) );
        daoUtil.setInt( ++nPos, icon.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon load( int nIdIcon )
    {
        Icon icon = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, WorkflowUtils.getPlugin(  ) );

        daoUtil.setInt( 1, nIdIcon );

        daoUtil.executeQuery(  );

        int nPos = 0;

        if ( daoUtil.next(  ) )
        {
            icon = new Icon(  );
            icon.setId( daoUtil.getInt( ++nPos ) );
            icon.setName( daoUtil.getString( ++nPos ) );
            icon.setMimeType( daoUtil.getString( ++nPos ) );
            icon.setValue( daoUtil.getBytes( ++nPos ) );
            icon.setWidth( daoUtil.getInt( ++nPos ) );
            icon.setHeight( daoUtil.getInt( ++nPos ) );
        }

        daoUtil.free(  );

        return icon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdIcon )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, WorkflowUtils.getPlugin(  ) );

        daoUtil.setInt( 1, nIdIcon );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Icon> selectAll(  )
    {
        Icon icon = null;
        List<Icon> listIcon = new ArrayList<Icon>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ICON, WorkflowUtils.getPlugin(  ) );
        daoUtil.executeQuery(  );

        int nPos;

        while ( daoUtil.next(  ) )
        {
            nPos = 0;
            icon = new Icon(  );
            icon.setId( daoUtil.getInt( ++nPos ) );
            icon.setName( daoUtil.getString( ++nPos ) );
            icon.setMimeType( daoUtil.getString( ++nPos ) );
            icon.setWidth( daoUtil.getInt( ++nPos ) );
            icon.setHeight( daoUtil.getInt( ++nPos ) );

            listIcon.add( icon );
        }

        daoUtil.free(  );

        return listIcon;
    }
}
