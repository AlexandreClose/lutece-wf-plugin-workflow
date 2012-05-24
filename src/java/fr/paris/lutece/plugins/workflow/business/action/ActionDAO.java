/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.business.action;

import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.action.ActionFilter;
import fr.paris.lutece.plugins.workflowcore.business.action.IActionDAO;
import fr.paris.lutece.plugins.workflowcore.business.icon.Icon;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.business.workflow.Workflow;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * ActionDAO
 *
 */
public class ActionDAO implements IActionDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_action ) FROM workflow_action";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_action,name,description,id_workflow," +
        "id_state_before,id_state_after,id_icon,is_automatic,is_mass_action FROM workflow_action WHERE id_action=?";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY_WITH_ICON = "SELECT a.id_action,a.name,a.description,a.id_workflow,a.id_state_before, " +
        " a.id_state_after,a.id_icon,a.is_automatic,a.is_mass_action,i.name,i.mime_type,i.file_value,i.width,i.height " +
        " FROM workflow_action a LEFT JOIN workflow_icon i ON (a.id_icon = i.id_icon) WHERE a.id_action=?";
    private static final String SQL_QUERY_SELECT_ACTION_BY_FILTER = "SELECT a.id_action,a.name,a.description,a.id_workflow,a.id_state_before, " +
        " a.id_state_after,a.id_icon,a.is_automatic,a.is_mass_action,i.name,i.mime_type,i.file_value,i.width,i.height " +
        " FROM workflow_action a LEFT JOIN workflow_icon i ON (a.id_icon = i.id_icon) ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_action " +
        "(id_action,name,description,id_workflow,id_state_before,id_state_after,id_icon,is_automatic,is_mass_action)" +
        " VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_action  SET id_action=?,name=?,description=?," +
        "id_workflow=?,id_state_before=?,id_state_after=?,id_icon=?,is_automatic=?,is_mass_action=? " +
        " WHERE id_action=?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_action  WHERE id_action=? ";
    private static final String SQL_FILTER_ID_WORKFLOW = " id_workflow = ? ";
    private static final String SQL_FILTER_ID_STATE_BEFORE = " id_state_before= ? ";
    private static final String SQL_FILTER_ID_STATE_AFTER = " id_state_after = ? ";
    private static final String SQL_FILTER_ID_ICON = " a.id_icon = ? ";
    private static final String SQL_FILTER_IS_AUTOMATIC = " is_automatic = ? ";
    private static final String SQL_FILTER_IS_MASS_ACTION = " is_mass_action = ? ";
    private static final String SQL_ORDER_BY_ID_ACTION = " ORDER BY id_action";

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
    public synchronized void insert( Action action )
    {
        int nPos = 0;
        action.setId( newPrimaryKey( WorkflowUtils.getPlugin(  ) ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, WorkflowUtils.getPlugin(  ) );
        daoUtil.setInt( ++nPos, action.getId(  ) );
        daoUtil.setString( ++nPos, action.getName(  ) );
        daoUtil.setString( ++nPos, action.getDescription(  ) );
        daoUtil.setInt( ++nPos, action.getWorkflow(  ).getId(  ) );
        daoUtil.setInt( ++nPos, action.getStateBefore(  ).getId(  ) );
        daoUtil.setInt( ++nPos, action.getStateAfter(  ).getId(  ) );
        daoUtil.setInt( ++nPos, action.getIcon(  ).getId(  ) );
        daoUtil.setBoolean( ++nPos, action.isAutomaticState(  ) );
        daoUtil.setBoolean( ++nPos, action.isMassAction(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Action action )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, WorkflowUtils.getPlugin(  ) );

        int nPos = 0;

        daoUtil.setInt( ++nPos, action.getId(  ) );
        daoUtil.setString( ++nPos, action.getName(  ) );
        daoUtil.setString( ++nPos, action.getDescription(  ) );
        daoUtil.setInt( ++nPos, action.getWorkflow(  ).getId(  ) );
        daoUtil.setInt( ++nPos, action.getStateBefore(  ).getId(  ) );
        daoUtil.setInt( ++nPos, action.getStateAfter(  ).getId(  ) );
        daoUtil.setInt( ++nPos, action.getIcon(  ).getId(  ) );
        daoUtil.setBoolean( ++nPos, action.isAutomaticState(  ) );
        daoUtil.setBoolean( ++nPos, action.isMassAction(  ) );

        daoUtil.setInt( ++nPos, action.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Action load( int nIdAction )
    {
        Action action = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, WorkflowUtils.getPlugin(  ) );
        daoUtil.setInt( 1, nIdAction );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            int nPos = 0;
            action = new Action(  );
            action.setId( daoUtil.getInt( ++nPos ) );
            action.setName( daoUtil.getString( ++nPos ) );
            action.setDescription( daoUtil.getString( ++nPos ) );

            Workflow workflow = new Workflow(  );
            workflow.setId( daoUtil.getInt( ++nPos ) );
            action.setWorkflow( workflow );

            State stateBefore = new State(  );
            stateBefore.setId( daoUtil.getInt( ++nPos ) );
            action.setStateBefore( stateBefore );

            State stateAfter = new State(  );
            stateAfter.setId( daoUtil.getInt( ++nPos ) );
            action.setStateAfter( stateAfter );

            Icon icon = new Icon(  );
            icon.setId( daoUtil.getInt( ++nPos ) );
            action.setIcon( icon );

            action.setAutomaticState( daoUtil.getBoolean( ++nPos ) );
            action.setMassAction( daoUtil.getBoolean( ++nPos ) );
        }

        daoUtil.free(  );

        return action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Action loadWithIcon( int nIdAction )
    {
        Action action = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY_WITH_ICON, WorkflowUtils.getPlugin(  ) );
        daoUtil.setInt( 1, nIdAction );
        daoUtil.executeQuery(  );

        int nPos = 0;

        if ( daoUtil.next(  ) )
        {
            action = new Action(  );
            action.setId( daoUtil.getInt( ++nPos ) );
            action.setName( daoUtil.getString( ++nPos ) );
            action.setDescription( daoUtil.getString( ++nPos ) );

            Workflow workflow = new Workflow(  );
            workflow.setId( daoUtil.getInt( ++nPos ) );
            action.setWorkflow( workflow );

            State stateBefore = new State(  );
            stateBefore.setId( daoUtil.getInt( ++nPos ) );
            action.setStateBefore( stateBefore );

            State stateAfter = new State(  );
            stateAfter.setId( daoUtil.getInt( ++nPos ) );
            action.setStateAfter( stateAfter );

            Icon icon = new Icon(  );
            icon.setId( daoUtil.getInt( ++nPos ) );

            action.setAutomaticState( daoUtil.getBoolean( ++nPos ) );
            action.setMassAction( daoUtil.getBoolean( ++nPos ) );
            icon.setName( daoUtil.getString( ++nPos ) );
            icon.setMimeType( daoUtil.getString( ++nPos ) );
            icon.setValue( daoUtil.getBytes( ++nPos ) );
            icon.setWidth( daoUtil.getInt( ++nPos ) );
            icon.setHeight( daoUtil.getInt( ++nPos ) );

            action.setIcon( icon );
        }

        daoUtil.free(  );

        return action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdAction )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, WorkflowUtils.getPlugin(  ) );

        daoUtil.setInt( 1, nIdAction );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Action> selectActionsByFilter( ActionFilter filter )
    {
        List<Action> listAction = new ArrayList<Action>(  );
        Action action = null;
        int nPos = 0;
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( filter.containsIdWorkflow(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_WORKFLOW );
        }

        if ( filter.containsIdStateBefore(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_STATE_BEFORE );
        }

        if ( filter.containsIdStateAfter(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_STATE_AFTER );
        }

        if ( filter.containsIdIcon(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_ICON );
        }

        if ( filter.containsIsAutomaticState(  ) )
        {
            listStrFilter.add( SQL_FILTER_IS_AUTOMATIC );
        }

        if ( filter.containsIsMassAction(  ) )
        {
            listStrFilter.add( SQL_FILTER_IS_MASS_ACTION );
        }

        String strSQL = WorkflowUtils.buildRequestWithFilter( SQL_QUERY_SELECT_ACTION_BY_FILTER, listStrFilter,
                SQL_ORDER_BY_ID_ACTION );

        DAOUtil daoUtil = new DAOUtil( strSQL, WorkflowUtils.getPlugin(  ) );

        if ( filter.containsIdWorkflow(  ) )
        {
            daoUtil.setInt( ++nPos, filter.getIdWorkflow(  ) );
        }

        if ( filter.containsIdStateBefore(  ) )
        {
            daoUtil.setInt( ++nPos, filter.getIdStateBefore(  ) );
        }

        if ( filter.containsIdStateAfter(  ) )
        {
            daoUtil.setInt( ++nPos, filter.getIdStateAfter(  ) );
        }

        if ( filter.containsIdIcon(  ) )
        {
            daoUtil.setInt( ++nPos, filter.getIdIcon(  ) );
        }

        if ( filter.containsIsAutomaticState(  ) )
        {
            daoUtil.setInt( ++nPos, filter.getIsAutomaticState(  ) );
        }

        if ( filter.containsIsMassAction(  ) )
        {
            daoUtil.setInt( ++nPos, filter.getIsMassAction(  ) );
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nPos = 0;
            action = new Action(  );
            action.setId( daoUtil.getInt( ++nPos ) );
            action.setName( daoUtil.getString( ++nPos ) );
            action.setDescription( daoUtil.getString( ++nPos ) );

            Workflow workflow = new Workflow(  );
            workflow.setId( daoUtil.getInt( ++nPos ) );
            action.setWorkflow( workflow );

            State stateBefore = new State(  );
            stateBefore.setId( daoUtil.getInt( ++nPos ) );
            action.setStateBefore( stateBefore );

            State stateAfter = new State(  );
            stateAfter.setId( daoUtil.getInt( ++nPos ) );
            action.setStateAfter( stateAfter );

            Icon icon = new Icon(  );
            icon.setId( daoUtil.getInt( ++nPos ) );

            action.setAutomaticState( daoUtil.getBoolean( ++nPos ) );
            action.setMassAction( daoUtil.getBoolean( ++nPos ) );

            icon.setName( daoUtil.getString( ++nPos ) );
            icon.setMimeType( daoUtil.getString( ++nPos ) );
            icon.setValue( daoUtil.getBytes( ++nPos ) );
            icon.setWidth( daoUtil.getInt( ++nPos ) );
            icon.setHeight( daoUtil.getInt( ++nPos ) );

            action.setIcon( icon );

            listAction.add( action );
        }

        daoUtil.free(  );

        return listAction;
    }
}