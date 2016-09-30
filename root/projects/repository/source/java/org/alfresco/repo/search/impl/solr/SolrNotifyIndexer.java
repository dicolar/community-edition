package org.alfresco.repo.search.impl.solr;

import org.alfresco.repo.search.Indexer;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchService;

import java.util.Collection;

/**
 * @Author : Val
 * @Created : 2016-09-26
 * @Version : 1.0
 * ©Shulidata
 */
public class SolrNotifyIndexer implements Indexer {
	
	ServiceRegistry serviceRegistry;
	SolrQueryHTTPClient solrQueryHTTPCLient;
	
	public void setSolrQueryHTTPCLient(SolrQueryHTTPClient solrQueryHTTPCLient) {
		this.solrQueryHTTPCLient = solrQueryHTTPCLient;
	}
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	public SolrNotifyIndexer() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>new solr notify indexer");
	}
	
	@Override
	public void createNode(ChildAssociationRef relationshipRef) {
		
	}
	
	@Override
	public void updateNode(NodeRef nodeRef) {
		System.out.println("update node...." + nodeRef);
	}
	
	@Override
	public void deleteNode(ChildAssociationRef relationshipRef) {
		
	}
	
	@Override
	public void createChildRelationship(ChildAssociationRef relationshipRef) {
		
	}
	
	@Override
	public void updateChildRelationship(ChildAssociationRef relationshipBeforeRef, ChildAssociationRef relationshipAfterRef) {
		
	}
	
	@Override
	public void deleteChildRelationship(ChildAssociationRef relationshipRef) {
		
	}
	
	@Override
	public void detectNodeChanges(NodeRef nodeRef, SearchService searcher, Collection<ChildAssociationRef> addedParents, Collection<ChildAssociationRef> deletedParents, Collection<ChildAssociationRef> createdNodes, Collection<NodeRef> updatedNodes) {
		
	}
	
	@Override
	public void deleteIndex(StoreRef storeRef) {
		
	}
	
	@Override
	public void flushPending() {
		
	}
	
	@Override
	public void setReadThrough(boolean isReadThrough) {
		
	}
}
