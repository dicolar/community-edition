/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.repo.security.authority;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.domain.node.NodeDAO;
import org.alfresco.repo.domain.qname.QNameDAO;
import org.alfresco.repo.tenant.TenantService;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.Pair;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andy
 */
public abstract class AbstractAuthorityBridgeDAO implements AuthorityBridgeDAO, ApplicationContextAware {
    
    private NodeDAO nodeDAO;
    
    private QNameDAO qnameDAO;
    
    private TenantService tenantService;
    
    private ApplicationContext applicationContext;
    
    private DictionaryService dictionaryService;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.dictionaryService = applicationContext.getBean("DictionaryService", DictionaryService.class);
    }
    
    /**
     * @param nodeDAO the nodeDAO to set
     */
    public void setNodeDAO(NodeDAO nodeDAO) {
        this.nodeDAO = nodeDAO;
    }
    
    /**
     * @param qnameDAO the qnameDAO to set
     */
    public void setQnameDAO(QNameDAO qnameDAO) {
        this.qnameDAO = qnameDAO;
    }
    
    /**
     * @param tenantService the tenantService to set
     */
    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }
    
    /*
	 * (non-Javadoc)
	 * @see org.alfresco.repo.security.authority.AuthorityBridgeDAO#getAuthorityBridgeLinks()
	 */
    @Override
    public List<AuthorityBridgeLink> getAuthorityBridgeLinks() {
        Long memberAssocQNameId = Long.MIN_VALUE;
        Pair<Long, QName> memberAssocQNamePair = qnameDAO.getQName(ContentModel.ASSOC_MEMBER);
        if (memberAssocQNamePair != null) {
            memberAssocQNameId = memberAssocQNamePair.getFirst();
        }
        
        Long authorityNameQNameId = Long.MIN_VALUE;
        Pair<Long, QName> authorityNameQNamePair = qnameDAO.getQName(ContentModel.PROP_AUTHORITY_NAME);
        if (authorityNameQNamePair != null) {
            authorityNameQNameId = authorityNameQNamePair.getFirst();
        }
        
        // Get tenenat specifc store id
        StoreRef tenantSpecificStoreRef = tenantService.getName(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
        Long storeId = Long.MIN_VALUE;
        if (tenantSpecificStoreRef != null) {
            Pair<Long, StoreRef> storePair = nodeDAO.getStore(tenantSpecificStoreRef);
            if (storePair != null) {
                storeId = storePair.getFirst();
            }
        }
        
        //TODO get sub types' names...
        Set<QName> set = new HashSet<>();
        set.add(ContentModel.TYPE_AUTHORITY_CONTAINER);
        set.addAll(dictionaryService.getSubTypes(ContentModel.TYPE_AUTHORITY_CONTAINER, true));
        
        List<AuthorityBridgeLink> result = new ArrayList<>();
        for (QName type : set) {
            Long authorityContainerTypeQNameId = Long.MIN_VALUE;
            Pair<Long, QName> authorityContainerTypeQNamePair = qnameDAO.getQName(type);
            
            if (authorityContainerTypeQNamePair != null) {
                authorityContainerTypeQNameId = authorityContainerTypeQNamePair.getFirst();
            }
            
            if (authorityContainerTypeQNameId == null) continue;
            
            result.addAll(selectAuthorityBridgeLinks(authorityContainerTypeQNameId, memberAssocQNameId, authorityNameQNameId, storeId));
        }
        
        return result;
        
    }
    
    
    /* (non-Javadoc)
	 * @see org.alfresco.repo.security.authority.AuthorityBridgeDAO#getDirectAuthoritiesForUser(java.lang.String)
	 */
    @Override
    public List<AuthorityBridgeLink> getDirectAuthoritiesForUser(NodeRef authRef) {
        Long memberAssocQNameId = Long.MIN_VALUE;
        Pair<Long, QName> memberAssocQNamePair = qnameDAO.getQName(ContentModel.ASSOC_MEMBER);
        if (memberAssocQNamePair != null) {
            memberAssocQNameId = memberAssocQNamePair.getFirst();
        }
        
        Long authorityNameQNameId = Long.MIN_VALUE;
        Pair<Long, QName> authorityNameQNamePair = qnameDAO.getQName(ContentModel.PROP_AUTHORITY_NAME);
        if (authorityNameQNamePair != null) {
            authorityNameQNameId = authorityNameQNamePair.getFirst();
        }
        
        // Get tenenat specifc store id
        StoreRef tenantSpecificStoreRef = tenantService.getName(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
        Long storeId = Long.MIN_VALUE;
        if (tenantSpecificStoreRef != null) {
            Pair<Long, StoreRef> storePair = nodeDAO.getStore(tenantSpecificStoreRef);
            if (storePair != null) {
                storeId = storePair.getFirst();
            }
        }
        
        Pair<Long, NodeRef> pair = (authRef == null) ? null : nodeDAO.getNodePair(tenantService.getName(authRef));
        
        //TODO get sub types' names...
        Set<QName> set = new HashSet<>();
        set.add(ContentModel.TYPE_AUTHORITY_CONTAINER);
        set.addAll(dictionaryService.getSubTypes(ContentModel.TYPE_AUTHORITY_CONTAINER, true));
        
        List<AuthorityBridgeLink> result = new ArrayList<>();
        for (QName type : set) {
            Long authorityContainerTypeQNameId = Long.MIN_VALUE;
            Pair<Long, QName> authorityContainerTypeQNamePair = qnameDAO.getQName(type);
            
            if (authorityContainerTypeQNamePair != null) {
                authorityContainerTypeQNameId = authorityContainerTypeQNamePair.getFirst();
            }
            
            if (authorityContainerTypeQNameId == null) continue;
            
            result.addAll(selectDirectAuthoritiesForUser(authorityContainerTypeQNameId, memberAssocQNameId, authorityNameQNameId, storeId, (pair == null) ? -1L : pair.getFirst()));
        }
        
        return result;
    }
    
    /**
     * @param authorityContainerTypeQNameId
     * @param memberAssocQNameId
     * @param authorityNameQNameId
     * @param storeId
     * @return
     */
    protected abstract List<AuthorityBridgeLink> selectDirectAuthoritiesForUser(Long authorityContainerTypeQNameId, Long memberAssocQNameId, Long authorityNameQNameId, Long storeId,
                                                                                Long nodeId);
    
    /**
     * @param authorityContainerTypeQNameId
     * @param memberAssocQNameId
     * @param authorityNameQNameId
     * @param storeId
     * @return
     */
    protected abstract List<AuthorityBridgeLink> selectAuthorityBridgeLinks(Long authorityContainerTypeQNameId, Long memberAssocQNameId, Long authorityNameQNameId, Long storeId);
    
}
