package util;

/**
 * Utility class for referring to API Urls for all Mendeley API method calls.
 * 
 * Note that this code is based upon mendeley-java-sdk written by Nabeel Mukhtar (2011).
 * 
 * @author A,M,M
 *
 */
public final class MendeleyAPIUrls {
    
    /**
     * The Interface OAuthUrls.
     */
    public static interface OAuthUrls {
    	
	    /** The Constant AUTHORIZE_URL. */
	    public static final String AUTHORIZE_URL = "com.mendeley.oapi.services.oauthService.authorize";
    	
	    /** The Constant REQUEST_TOKEN_URL. */
	    public static final String REQUEST_TOKEN_URL = "com.mendeley.oapi.services.oauthService.requestToken";
	    
	    /** The Constant ACCESS_TOKEN_URL. */
	    public static final String ACCESS_TOKEN_URL = "com.mendeley.oapi.services.oauthService.accessToken";
    }
    
    /**
     * The Interface ProfileApiUrls.
     */
    public static interface ProfileApiUrls {
    	
	    /** The Constant GET_CONTACTS_URL. */
	    public static final String GET_CONTACTS_URL = "com.mendeley.oapi.services.profileService.getContacts";
	    
    	/** The Constant ADD_CONTACT_URL. */
    	public static final String ADD_CONTACT_URL = "com.mendeley.oapi.services.profileService.addContact";

    	/** The Constant GET_PROFILE_URL. */
    	public static final String GET_PROFILE_URL = "com.mendeley.oapi.services.profileService.getProfile";
    }

    /**
     * The Interface CollectionApiUrls.
     */
    public static interface CollectionApiUrls {
    	
	    /** The Constant GET_COLLECTIONS_URL. */
	    public static final String GET_COLLECTIONS_URL = "com.mendeley.oapi.services.collectionService.getCollections";
    	
	    /** The Constant GET_COLLECTION_DOCUMENTS_URL. */
	    public static final String GET_COLLECTION_DOCUMENTS_URL = "com.mendeley.oapi.services.collectionService.getCollectionDocuments";
    	
	    /** The Constant ADD_DOCUMENT_TO_COLLECTION_URL. */
	    public static final String ADD_DOCUMENT_TO_COLLECTION_URL = "com.mendeley.oapi.services.collectionService.addDocumentToCollection";
    	
	    /** The Constant CREATE_COLLECTION_URL. */
	    public static final String CREATE_COLLECTION_URL = "com.mendeley.oapi.services.collectionService.createCollection";
    	
	    /** The Constant REMOVE_COLLECTION_URL. */
	    public static final String REMOVE_COLLECTION_URL = "com.mendeley.oapi.services.collectionService.removeCollection";
    	
	    /** The Constant REMOVE_DOCUMENT_FROM_COLLECTION_URL. */
	    public static final String REMOVE_DOCUMENT_FROM_COLLECTION_URL = "com.mendeley.oapi.services.collectionService.removeDocumentFromCollection";
    }
    
    /**
     * The Interface FolderApiUrls.
     */
    public static interface FolderApiUrls {
    	
	    /** The Constant GET_FOLDERS_URL. */
	    public static final String GET_FOLDERS_URL = "com.mendeley.oapi.services.folderService.getFolders";
    	
	    /** The Constant GET_FOLDER_DOCUMENTS_URL. */
	    public static final String GET_FOLDER_DOCUMENTS_URL = "com.mendeley.oapi.services.folderService.getFolderDocuments";
    	
	    /** The Constant ADD_DOCUMENT_TO_FOLDER_URL. */
	    public static final String ADD_DOCUMENT_TO_FOLDER_URL = "com.mendeley.oapi.services.folderService.addDocumentToFolder";
    	
	    /** The Constant CREATE_FOLDER_URL. */
	    public static final String CREATE_FOLDER_URL = "com.mendeley.oapi.services.folderService.createFolder";
    	
	    /** The Constant REMOVE_FOLDER_URL. */
	    public static final String REMOVE_FOLDER_URL = "com.mendeley.oapi.services.folderService.removeFolder";
    	
	    /** The Constant REMOVE_DOCUMENT_FROM_FOLDER_URL. */
	    public static final String REMOVE_DOCUMENT_FROM_FOLDER_URL = "com.mendeley.oapi.services.folderService.removeDocumentFromFolder";
    }
    
    /**
     * The Interface PrivateGroupApiUrls.
     */
    public static interface PrivateGroupApiUrls {
    	
	    /** The Constant GET_GROUPS_URL. */
	    public static final String GET_GROUPS_URL = "com.mendeley.oapi.services.privateGroupService.getGroups";
    	
	    /** The Constant GET_GROUP_DETAILS_URL. */
	    public static final String GET_GROUP_DETAILS_URL = "com.mendeley.oapi.services.privateGroupService.getGroupDetails";
    	
	    /** The Constant GET_GROUP_PEOPLE_URL. */
	    public static final String GET_GROUP_PEOPLE_URL = "com.mendeley.oapi.services.privateGroupService.getGroupPeople";
	    
    	/** The Constant CREATE_GROUP_URL. */
    	public static final String CREATE_GROUP_URL = "com.mendeley.oapi.services.privateGroupService.createGroup";
	    
    	/** The Constant DELETE_GROUP_URL. */
    	public static final String DELETE_GROUP_URL = "com.mendeley.oapi.services.privateGroupService.deleteGroup";
	    
    	/** The Constant LEAVE_GROUP_URL. */
    	public static final String LEAVE_GROUP_URL = "com.mendeley.oapi.services.privateGroupService.leaveGroup";
	    
    	/** The Constant UNFOLLOW_GROUP_URL. */
    	public static final String UNFOLLOW_GROUP_URL = "com.mendeley.oapi.services.privateGroupService.unfollowGroup";
    }
    
    /**
     * The Interface SharedCollectionApiUrls.
     */
    public static interface SharedCollectionApiUrls {
    	
	    /** The Constant GET_COLLECTIONS_URL. */
	    public static final String GET_COLLECTIONS_URL = "com.mendeley.oapi.services.sharedCollectionService.getCollections";
    	
	    /** The Constant GET_COLLECTION_DOCUMENTS_URL. */
	    public static final String GET_COLLECTION_DOCUMENTS_URL = "com.mendeley.oapi.services.sharedCollectionService.getCollectionDocuments";
    	
	    /** The Constant ADD_DOCUMENT_TO_COLLECTION_URL. */
	    public static final String ADD_DOCUMENT_TO_COLLECTION_URL = "com.mendeley.oapi.services.sharedCollectionService.addDocumentToCollection";
    	
	    /** The Constant CREATE_COLLECTION_URL. */
	    public static final String CREATE_COLLECTION_URL = "com.mendeley.oapi.services.sharedCollectionService.createCollection";
    	
	    /** The Constant REMOVE_COLLECTION_URL. */
	    public static final String REMOVE_COLLECTION_URL = "com.mendeley.oapi.services.sharedCollectionService.removeCollection";
    	
	    /** The Constant REMOVE_DOCUMENT_FROM_COLLECTION_URL. */
	    public static final String REMOVE_DOCUMENT_FROM_COLLECTION_URL = "com.mendeley.oapi.services.sharedCollectionService.removeDocumentFromCollection";
    }
    
    /**
     * The Interface PublicGroupApiUrls.
     */
    public static interface PublicGroupApiUrls {
    	
	    /** The Constant GET_GROUPS_URL. */
	    public static final String GET_GROUPS_URL = "com.mendeley.oapi.services.publicGroupService.getGroups";
    	
	    /** The Constant GET_GROUP_DETAILS_URL. */
	    public static final String GET_GROUP_DETAILS_URL = "com.mendeley.oapi.services.publicGroupService.getGroupDetails";
    	
	    /** The Constant GET_GROUP_PEOPLE_URL. */
	    public static final String GET_GROUP_PEOPLE_URL = "com.mendeley.oapi.services.publicGroupService.getGroupPeople";
	    
	    /** The Constant GET_GROUP_DOCUMENTS_URL. */
    	public static final String GET_GROUP_DOCUMENTS_URL = "com.mendeley.oapi.services.publicGroupService.getGroupDocuments";
    	
    }
    
    /**
     * The Interface PrivateStatsApiUrls.
     */
    public static interface PrivateStatsApiUrls {
    	
	    /** The Constant GET_AUTHORS_URL. */
	    public static final String GET_AUTHORS_URL = "com.mendeley.oapi.services.privateStatsService.getAuthors";
    	
	    /** The Constant GET_PUBLICATIONS_URL. */
	    public static final String GET_PUBLICATIONS_URL = "com.mendeley.oapi.services.privateStatsService.getPublications";
    	
	    /** The Constant GET_TAGS_URL. */
	    public static final String GET_TAGS_URL = "com.mendeley.oapi.services.privateStatsService.getTags";
    }
    
    /**
     * The Interface PublicStatsApiUrls.
     */
    public static interface PublicStatsApiUrls {
    	
	    /** The Constant GET_AUTHORS_URL. */
	    public static final String GET_AUTHORS_URL = "com.mendeley.oapi.services.publicStatsService.getAuthors";
    	
	    /** The Constant GET_PUBLICATIONS_URL. */
	    public static final String GET_PUBLICATIONS_URL = "com.mendeley.oapi.services.publicStatsService.getPublications";
	    
	    /** The Constant GET_PAPERS_URL. */
    	public static final String GET_PAPERS_URL = "com.mendeley.oapi.services.publicStatsService.getPapers";
    	
	    /** The Constant GET_TAGS_URL. */
	    public static final String GET_TAGS_URL = "com.mendeley.oapi.services.publicStatsService.getTags";
    }
    
    /**
     * The Interface SearchApiUrls.
     */
    public static interface SearchApiUrls {
    	
	    /** The Constant SEARCH_URL. */
	    public static final String SEARCH_URL = "com.mendeley.oapi.services.searchService.search";
    	
	    /** The Constant GET_DOCUMENT_DETAILS_URL. */
	    public static final String GET_DOCUMENT_DETAILS_URL = "com.mendeley.oapi.services.searchService.getDocumentDetails";
    	
	    /** The Constant GET_RELATED_DOCUMENTS_URL. */
	    public static final String GET_RELATED_DOCUMENTS_URL = "com.mendeley.oapi.services.searchService.getRelatedDocuments";
	    
	    /** The Constant GET_DOCUMENTS_BY_AUTHOR_URL. */
    	public static final String GET_DOCUMENTS_BY_AUTHOR_URL = "com.mendeley.oapi.services.searchService.getDocumentsByAuthor";
	    
    	/** The Constant GET_DOCUMENTS_BY_TAG_URL. */
    	public static final String GET_DOCUMENTS_BY_TAG_URL = "com.mendeley.oapi.services.searchService.getDocumentsByTag";
	    
    	/** The Constant GET_CATEGORIES_URL. */
    	public static final String GET_CATEGORIES_URL = "com.mendeley.oapi.services.searchService.getCategories";
	    
    	/** The Constant GET_SUB_CATEGORIES_URL. */
    	public static final String GET_SUB_CATEGORIES_URL = "com.mendeley.oapi.services.searchService.getSubCategories";
    }
    
    
    /**
     * The Interface DocumentApiUrls.
     */
    public static interface DocumentApiUrls {
	    
    	/** The Constant GET_DOCUMENT_IDS_URL. */
	    public static final String GET_DOCUMENT_IDS_URL = "com.mendeley.oapi.services.documentService.getDocumentIds";

	    /** The Constant GET_AUTHORED_PUBLICATIONS_URL. */
	    public static final String GET_AUTHORED_PUBLICATIONS_URL = "com.mendeley.oapi.services.documentService.getAuthoredPublications";

	    /** The Constant GET_DOCUMENT_DETAILS_URL. */
	    public static final String GET_DOCUMENT_DETAILS_URL = "com.mendeley.oapi.services.documentService.getDocumentDetails";

	    /** The Constant CREATE_DOCUMENT_URL. */
	    public static final String CREATE_DOCUMENT_URL = "com.mendeley.oapi.services.documentService.createDocument";

	    /** The Constant REMOVE_DOCUMENT_URL. */
	    public static final String REMOVE_DOCUMENT_URL = "com.mendeley.oapi.services.documentService.removeDocument";
	    
	    /** The Constant UPLOAD_FILE_URL. */
    	public static final String UPLOAD_FILE_URL = "com.mendeley.oapi.services.documentService.uploadFile";

	    /** The Constant DOWNLOAD_FILE_URL. */
    	public static final String DOWNLOAD_FILE_URL = "com.mendeley.oapi.services.documentService.downloadFile";
    }
    

}
