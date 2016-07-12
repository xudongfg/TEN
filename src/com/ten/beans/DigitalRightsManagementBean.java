package com.ten.beans;

/**
 * @author Nita Karande
 * This is the bean used data transfer between jsp and action classes
 * This class will fields for dublin core annotations
 */
public class DigitalRightsManagementBean {
	//AdminstrativeTags
	String physicalDescription;
	String loanPeriod;
	String identifier;
	String identifierDescription;
	String handlingInstructions;	
	String rights;	
	String intaker;
	String dateOfUpload; //date of upload
	String storyProvided;
	String story;
	String storyContext;
	String tribe;
	


	//copy right holder related fields
	String copyRightHolderId;
	String copyRightHolderNotAvailable;
	String copyRightHolderFinderInfo;
	String copyRightHolderApproved;	
	String copyRightHolderCellPhone;
	String copyRightHolderEmail;
	String copyRightHolderOfficePhone;
	String copyRightHolderFax;
	String copyRightHolderStreetAddress;
	String copyRightHolderOtherAddress;
	String copyRightHolderCity;
	String copyRightHolderState;
	String copyRightHolderZipCode;	
	
	//creator related fields
	String creator;
	String creatorApproved;
	String creatorCellPhone;
	String creatorEmail;
	String creatorOfficePhone;
	String creatorFax;
	String creatorStreetAddress;
	String creatorOtherAddress;
	String creatorCity;
	String creatorState;
	String creatorZipCode;
	
	//publisher related fields
	String publisher;
	String publisherApproved;
	String publisherEmail;
	String publisherCellPhone;
	String publisherOfficePhone;
	String publisherFax;
	String publisherStreetAddress;
	String publisherOtherAddress;
	String publisherCity;
	String publisherState;
	String publisherZipCode;
	
	//contributor related fields
	String contributor;
	String contributorApproved;
	String contributorEmail;
	String contributorCellPhone;
	String contributorOfficePhone;
	String contributorFax;
	String contributorStreetAddress;
	String contributorOtherAddress;
	String contributorCity;
	String contributorState;
	String contributorZipCode;
	String contributorTribalAffiliation;
	
	//story provider fields
	String storyProvider;
	String storyProviderEmail;
	String storyProviderCellPhone;
	String storyProviderOfficePhone;
	String storyProviderFax;
	String storyProviderStreetAddress;
	String storyProviderOtherAddress;
	String storyProviderCity;
	String storyProviderState;
	String storyProviderZipCode;
	
	public String getPhysicalDescription() {
		return physicalDescription;
	}

	public void setPhysicalDescription(String physicalDescription) {
		this.physicalDescription = physicalDescription;
	}

	public String getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(String loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifierDescription() {
		return identifierDescription;
	}

	public void setIdentifierDescription(String identifierDescription) {
		this.identifierDescription = identifierDescription;
	}

	public String getHandlingInstructions() {
		return handlingInstructions;
	}

	public String getPublisherEmail() {
		return publisherEmail;
	}

	public void setPublisherEmail(String publisherEmail) {
		this.publisherEmail = publisherEmail;
	}

	public String getContributorEmail() {
		return contributorEmail;
	}

	public void setContributorEmail(String contributorEmail) {
		this.contributorEmail = contributorEmail;
	}

	public void setHandlingInstructions(String handlingInstructions) {
		this.handlingInstructions = handlingInstructions;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getIntaker() {
		return intaker;
	}

	public void setIntaker(String intaker) {
		this.intaker = intaker;
	}

	public String getDateOfUpload() {
		return dateOfUpload;
	}

	public void setDateOfUpload(String dateOfUpload) {
		this.dateOfUpload = dateOfUpload;
	}

	public String getStoryProvided() {
		return storyProvided;
	}

	public void setStoryProvided(String storyProvided) {
		this.storyProvided = storyProvided;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublisherApproved() {
		return publisherApproved;
	}

	public void setPublisherApproved(String publisherApproved) {
		this.publisherApproved = publisherApproved;
	}

	public String getPublisherCellPhone() {
		return publisherCellPhone;
	}

	public void setPublisherCellPhone(String publisherCellPhone) {
		this.publisherCellPhone = publisherCellPhone;
	}

	public String getPublisherOfficePhone() {
		return publisherOfficePhone;
	}

	public void setPublisherOfficePhone(String publisherOfficePhone) {
		this.publisherOfficePhone = publisherOfficePhone;
	}

	public String getPublisherFax() {
		return publisherFax;
	}

	public void setPublisherFax(String publisherFax) {
		this.publisherFax = publisherFax;
	}

	public String getPublisherStreetAddress() {
		return publisherStreetAddress;
	}

	public void setPublisherStreetAddress(String publisherStreetAddress) {
		this.publisherStreetAddress = publisherStreetAddress;
	}

	public String getPublisherOtherAddress() {
		return publisherOtherAddress;
	}

	public void setPublisherOtherAddress(String publisherOtherAddress) {
		this.publisherOtherAddress = publisherOtherAddress;
	}

	public String getPublisherCity() {
		return publisherCity;
	}

	public void setPublisherCity(String publisherCity) {
		this.publisherCity = publisherCity;
	}

	public String getPublisherState() {
		return publisherState;
	}

	public void setPublisherState(String publisherState) {
		this.publisherState = publisherState;
	}

	public String getPublisherZipCode() {
		return publisherZipCode;
	}

	public void setPublisherZipCode(String publisherZipCode) {
		this.publisherZipCode = publisherZipCode;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public String getContributorApproved() {
		return contributorApproved;
	}

	public void setContributorApproved(String contributorApproved) {
		this.contributorApproved = contributorApproved;
	}

	public String getContributorCellPhone() {
		return contributorCellPhone;
	}

	public void setContributorCellPhone(String contributorCellPhone) {
		this.contributorCellPhone = contributorCellPhone;
	}

	public String getContributorOfficePhone() {
		return contributorOfficePhone;
	}

	public void setContributorOfficePhone(String contributorOfficePhone) {
		this.contributorOfficePhone = contributorOfficePhone;
	}

	public String getContributorFax() {
		return contributorFax;
	}

	public void setContributorFax(String contributorFax) {
		this.contributorFax = contributorFax;
	}

	public String getContributorStreetAddress() {
		return contributorStreetAddress;
	}

	public void setContributorStreetAddress(String contributorStreetAddress) {
		this.contributorStreetAddress = contributorStreetAddress;
	}

	public String getContributorOtherAddress() {
		return contributorOtherAddress;
	}

	public void setContributorOtherAddress(String contributorOtherAddress) {
		this.contributorOtherAddress = contributorOtherAddress;
	}

	public String getContributorCity() {
		return contributorCity;
	}

	public void setContributorCity(String contributorCity) {
		this.contributorCity = contributorCity;
	}

	public String getContributorState() {
		return contributorState;
	}

	public void setContributorState(String contributorState) {
		this.contributorState = contributorState;
	}

	public String getContributorZipCode() {
		return contributorZipCode;
	}

	public void setContributorZipCode(String contributorZipCode) {
		this.contributorZipCode = contributorZipCode;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getStoryProvider() {
		return storyProvider;
	}

	public void setStoryProvider(String storyProvider) {
		this.storyProvider = storyProvider;
	}

	public String getStoryProviderEmail() {
		return storyProviderEmail;
	}

	public void setStoryProviderEmail(String storyProviderEmail) {
		this.storyProviderEmail = storyProviderEmail;
	}

	public String getStoryProviderCellPhone() {
		return storyProviderCellPhone;
	}

	public void setStoryProviderCellPhone(String storyProviderCellPhone) {
		this.storyProviderCellPhone = storyProviderCellPhone;
	}

	public String getStoryProviderOfficePhone() {
		return storyProviderOfficePhone;
	}

	public void setStoryProviderOfficePhone(String storyProviderOfficePhone) {
		this.storyProviderOfficePhone = storyProviderOfficePhone;
	}

	public String getStoryProviderFax() {
		return storyProviderFax;
	}

	public void setStoryProviderFax(String storyProviderFax) {
		this.storyProviderFax = storyProviderFax;
	}

	public String getStoryProviderStreetAddress() {
		return storyProviderStreetAddress;
	}

	public void setStoryProviderStreetAddress(String storyProviderStreetAddress) {
		this.storyProviderStreetAddress = storyProviderStreetAddress;
	}

	public String getStoryProviderOtherAddress() {
		return storyProviderOtherAddress;
	}

	public void setStoryProviderOtherAddress(String storyProviderOtherAddress) {
		this.storyProviderOtherAddress = storyProviderOtherAddress;
	}

	public String getStoryProviderCity() {
		return storyProviderCity;
	}

	public void setStoryProviderCity(String storyProviderCity) {
		this.storyProviderCity = storyProviderCity;
	}

	public String getStoryProviderState() {
		return storyProviderState;
	}

	public void setStoryProviderState(String storyProviderState) {
		this.storyProviderState = storyProviderState;
	}

	public String getStoryProviderZipCode() {
		return storyProviderZipCode;
	}

	public void setStoryProviderZipCode(String storyProviderZipCode) {
		this.storyProviderZipCode = storyProviderZipCode;
	}

	public String getStoryContext() {
		return storyContext;
	}

	public void setStoryContext(String storyContext) {
		this.storyContext = storyContext;
	}

	public String getContributorTribalAffiliation() {
		return contributorTribalAffiliation;
	}

	public void setContributorTribalAffiliation(String contributorTribalAffiliation) {
		this.contributorTribalAffiliation = contributorTribalAffiliation;
	}

	public String getCopyRightHolderId() {
		return copyRightHolderId;
	}

	public void setCopyRightHolderId(String copyRightHolderId) {
		this.copyRightHolderId = copyRightHolderId;
	}

	public String getCopyRightHolderNotAvailable() {
		return copyRightHolderNotAvailable;
	}

	public void setCopyRightHolderNotAvailable(String copyRightHolderNotAvailable) {
		this.copyRightHolderNotAvailable = copyRightHolderNotAvailable;
	}

	public String getCopyRightHolderFinderInfo() {
		return copyRightHolderFinderInfo;
	}

	public void setCopyRightHolderFinderInfo(String copyRightHolderFinderInfo) {
		this.copyRightHolderFinderInfo = copyRightHolderFinderInfo;
	}

	public String getCopyRightHolderApproved() {
		return copyRightHolderApproved;
	}

	public void setCopyRightHolderApproved(String copyRightHolderApproved) {
		this.copyRightHolderApproved = copyRightHolderApproved;
	}

	public String getCopyRightHolderCellPhone() {
		return copyRightHolderCellPhone;
	}

	public void setCopyRightHolderCellPhone(String copyRightHolderCellPhone) {
		this.copyRightHolderCellPhone = copyRightHolderCellPhone;
	}

	public String getCopyRightHolderOfficePhone() {
		return copyRightHolderOfficePhone;
	}

	public void setCopyRightHolderOfficePhone(String copyRightHolderOfficePhone) {
		this.copyRightHolderOfficePhone = copyRightHolderOfficePhone;
	}

	public String getCopyRightHolderFax() {
		return copyRightHolderFax;
	}

	public void setCopyRightHolderFax(String copyRightHolderFax) {
		this.copyRightHolderFax = copyRightHolderFax;
	}

	public String getCopyRightHolderStreetAddress() {
		return copyRightHolderStreetAddress;
	}

	public void setCopyRightHolderStreetAddress(String copyRightHolderStreetAddress) {
		this.copyRightHolderStreetAddress = copyRightHolderStreetAddress;
	}

	public String getCopyRightHolderOtherAddress() {
		return copyRightHolderOtherAddress;
	}

	public void setCopyRightHolderOtherAddress(String copyRightHolderOtherAddress) {
		this.copyRightHolderOtherAddress = copyRightHolderOtherAddress;
	}

	public String getCopyRightHolderCity() {
		return copyRightHolderCity;
	}

	public void setCopyRightHolderCity(String copyRightHolderCity) {
		this.copyRightHolderCity = copyRightHolderCity;
	}

	public String getCopyRightHolderState() {
		return copyRightHolderState;
	}

	public void setCopyRightHolderState(String copyRightHolderState) {
		this.copyRightHolderState = copyRightHolderState;
	}

	public String getCopyRightHolderZipCode() {
		return copyRightHolderZipCode;
	}

	public void setCopyRightHolderZipCode(String copyRightHolderZipCode) {
		this.copyRightHolderZipCode = copyRightHolderZipCode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorApproved() {
		return creatorApproved;
	}

	public void setCreatorApproved(String creatorApproved) {
		this.creatorApproved = creatorApproved;
	}

	public String getCreatorCellPhone() {
		return creatorCellPhone;
	}

	public void setCreatorCellPhone(String creatorCellPhone) {
		this.creatorCellPhone = creatorCellPhone;
	}

	public String getCreatorOfficePhone() {
		return creatorOfficePhone;
	}

	public void setCreatorOfficePhone(String creatorOfficePhone) {
		this.creatorOfficePhone = creatorOfficePhone;
	}

	public String getCreatorFax() {
		return creatorFax;
	}

	public void setCreatorFax(String creatorFax) {
		this.creatorFax = creatorFax;
	}

	public String getCreatorStreetAddress() {
		return creatorStreetAddress;
	}

	public void setCreatorStreetAddress(String creatorStreetAddress) {
		this.creatorStreetAddress = creatorStreetAddress;
	}

	public String getCreatorOtherAddress() {
		return creatorOtherAddress;
	}

	public void setCreatorOtherAddress(String creatorOtherAddress) {
		this.creatorOtherAddress = creatorOtherAddress;
	}

	public String getCreatorCity() {
		return creatorCity;
	}

	public void setCreatorCity(String creatorCity) {
		this.creatorCity = creatorCity;
	}

	public String getCreatorState() {
		return creatorState;
	}

	public void setCreatorState(String creatorState) {
		this.creatorState = creatorState;
	}

	public String getCreatorZipCode() {
		return creatorZipCode;
	}

	public void setCreatorZipCode(String creatorZipCode) {
		this.creatorZipCode = creatorZipCode;
	}

	public String getCopyRightHolderEmail() {
		return copyRightHolderEmail;
	}

	public void setCopyRightHolderEmail(String copyRightHolderEmail) {
		this.copyRightHolderEmail = copyRightHolderEmail;
	}
	
	public String getTribe() {
		return tribe;
	}

	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	public DigitalRightsManagementBean(){
		super();
	
		tribe = "";
		physicalDescription = "";
		loanPeriod = "";
		identifier = "";
		identifierDescription = "";
		handlingInstructions = "";	
		rights = "";	
		intaker = "";
		dateOfUpload = ""; //date of upload
		storyProvided = "";
		story = "";
		storyContext="";

		//copy right holder related fields
		 copyRightHolderId = "";
		 copyRightHolderNotAvailable = "";
		 copyRightHolderFinderInfo = "";
		 copyRightHolderApproved = "";	
		 copyRightHolderCellPhone = "";
		 copyRightHolderOfficePhone = "";
		 copyRightHolderFax = "";
		 copyRightHolderStreetAddress = "";
		 copyRightHolderOtherAddress = "";
		 copyRightHolderCity = "";
		 copyRightHolderState = "";
		 copyRightHolderZipCode = "";	
		 
		 //creator related fields
		 creator = "";
		 creatorApproved = "";
		 creatorCellPhone = "";
		 creatorOfficePhone = "";
		 creatorFax = "";
		 creatorStreetAddress = "";
		 creatorOtherAddress = "";
		 creatorCity = "";
		 creatorState = "";
		 creatorZipCode = "";	
		
		 publisher = "";
		 publisherApproved = "";
		 publisherCellPhone = "";
		 publisherOfficePhone = "";
		 publisherFax = "";
		 publisherStreetAddress = "";
		 publisherOtherAddress = "";
		 publisherCity = "";
		 publisherState = "";
		 publisherZipCode = "";
		
		 contributor = "";
		 contributorApproved = "";
		 contributorCellPhone = "";
		 contributorOfficePhone = "";
		 contributorFax = "";
		 contributorStreetAddress = "";
		 contributorOtherAddress = "";
		 contributorCity = "";
		 contributorState = "";
		 contributorZipCode = "";
		 contributorTribalAffiliation = "";
		 
		 storyProvider = "";
		 storyProviderCellPhone = "";
		 storyProviderOfficePhone = "";
		 storyProviderFax = "";
		 storyProviderStreetAddress = "";
		 storyProviderOtherAddress = "";
		 storyProviderCity = "";
		 storyProviderState = "";
		 storyProviderZipCode = "";
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}
}
