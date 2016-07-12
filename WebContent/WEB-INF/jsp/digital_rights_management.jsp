<script>
	function onCopyRightHolderChange(cb){
		if (cb.checked){
			cb.value = "true";
			$("#copyRightHolderFinderInfo_div").show();
			$("#idDivCopyRightHolderExpander").hide();
			$(".copyRightHolderData").hide();
			resetValues('copyRightHolder');
			document.getElementById("copyRightHolderApproved").value = "";
			
		}else{
			cb.value = "false";
			$("#copyRightHolderFinderInfo_div").hide();
			document.getElementById("copyRightHolderFinderInfo").value = "";
			$("#idDivCopyRightHolderExpander").show();
			$(".copyRightHolderData").show();
		}
	}
	
	function onStoryProvidedChange(cb){
		if (cb.checked){
			cb.value = "true";
			$(".storyClass").show();
		}else{
			cb.value = "false";
			document.getElementById("storyId").value = "";
			document.getElementById("storyContext").value = "";
			resetValues('storyProvider');
			$(".storyClass").hide();
		}
	}
	
	function resetValues(field){
		document.getElementById(field + 'Id').value = "";
		document.getElementById(field + 'Email').value = "";
		document.getElementById(field + 'CellPhone').value = "";
		document.getElementById(field + 'OfficePhone').value = "";
		document.getElementById(field + 'Fax').value = "";
		document.getElementById(field + 'StreetAddress').value = "";
		document.getElementById(field + 'OtherAddress').value = "";
		document.getElementById(field + 'City').value = "";
		document.getElementById(field + 'State').value = "";
		document.getElementById(field + 'ZipCode').value = "";
	}
	
	function copyValues(cb,from, to){
		if(cb.checked){
			document.getElementById(to + 'Id').value = document.getElementById(from + 'Id').value;
			document.getElementById(to + 'Email').value = document.getElementById(from + 'Email').value;
			document.getElementById(to + 'CellPhone').value = document.getElementById(from + 'CellPhone').value;
			document.getElementById(to + 'OfficePhone').value = document.getElementById(from + 'OfficePhone').value;
			document.getElementById(to + 'Fax').value = document.getElementById(from + 'Fax').value;
			document.getElementById(to + 'StreetAddress').value = document.getElementById(from + 'StreetAddress').value;
			document.getElementById(to + 'OtherAddress').value = document.getElementById(from + 'OtherAddress').value;
			document.getElementById(to + 'City').value = document.getElementById(from + 'City').value;
			document.getElementById(to + 'State').value = document.getElementById(from + 'State').value;
			document.getElementById(to + 'ZipCode').value = document.getElementById(from + 'ZipCode').value;
		}
	}
	
	function expandCollapse(showHide) {
		var hideShowDiv = document.getElementById(showHide);
	    var label = document.getElementById("expand");

	    if (hideShowDiv.style.display == 'none') {
	    	label.innerHTML = label.innerHTML.replace("[+]", "[-]");
	        hideShowDiv.style.display = 'block';            
	    } else {
	        label.innerHTML = label.innerHTML.replace("[-]", "[+]");
	        hideShowDiv.style.display = 'none';
		}
	}
</script>


<table style="width: 100%">

	<tr>
		<td nowrap="nowrap" style="width: 40%"><b>Copyright Holder
				Information</b></td>
	</tr>
	<tr>
		<td nowrap="nowrap"><c:choose>
				<c:when
					test="${digitalRightsManagementBean.copyRightHolderNotAvailable == 'true'}">
					<input type="checkbox" id="copyRightHolderCheckbox"
						name="digitalRightsManagementBean.copyRightHolderNotAvailable"
						checked="checked" onchange="onCopyRightHolderChange(this)" />
				</c:when>
				<c:otherwise>
					<input type="checkbox" id="copyRightHolderCheckbox"
						name="digitalRightsManagementBean.copyRightHolderNotAvailable"
						onchange="onCopyRightHolderChange(this)" />
				</c:otherwise>
			</c:choose> Copyright holder information not available</td>
	</tr>
	<tr class="copyRightHolderData">

		<td nowrap="nowrap">Copyright Holder Name</td>
		<td><div
				title="${'An entity which is the copyright holder of the resource e.g. author of the article, creator of artifact, photographer of a picture or artist of a structure'}">
				<input type="text" size="50" id="copyRightHolderId"
					name="digitalRightsManagementBean.copyRightHolderId"
					value="${digitalRightsManagementBean.copyRightHolderId}" /> ?
			</div></td>
	</tr>
	<tr id="copyRightHolderFinderInfo_div" style="display: none;">
		<td>Potential copy right holder information*</td>
		<td><div
				title="${'Information about the potential copy right holder or any information that can help finding the copy right holder.'}">
				<input type="text" id="copyRightHolderFinderInfo"
					name="digitalRightsManagementBean.copyRightHolderFinderInfo"
					value="${digitalRightsManagementBean.copyRightHolderFinderInfo}"
					size="50" /> ?
			</div></td>
	</tr>
</table>
<div id="idDivCopyRightHolderExpander">
	<table style="width: 100%">
		<tr>
			<td onclick="expandCollapse('idDivCopyRightHolder');" id="expand"><font
				color="blue">[+] Expand to enter contact information</font></td>
		</tr>
	</table>
</div>
<div id="idDivCopyRightHolder" style="display: none;">
	<table style="width: 100%">
		<tr class="copyRightHolderData">
			<td>Email</td>
			<td><input type="text" id="copyRightHolderEmail"
				name="digitalRightsManagementBean.copyRightHolderEmail"
				value="${digitalRightsManagementBean.copyRightHolderEmail}"
				size="40" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>Cell Phone</td>
			<td><input type="text" id="copyRightHolderCellPhone"
				name="digitalRightsManagementBean.copyRightHolderCellPhone"
				value="${digitalRightsManagementBean.copyRightHolderCellPhone}"
				size="30" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>Office Phone</td>
			<td><input type="text" id="copyRightHolderOfficePhone"
				name="digitalRightsManagementBean.copyRightHolderOfficePhone"
				value="${digitalRightsManagementBean.copyRightHolderOfficePhone}"
				size="30" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>Fax</td>
			<td><input type="text" id="copyRightHolderFax"
				name="digitalRightsManagementBean.copyRightHolderFax"
				value="${digitalRightsManagementBean.copyRightHolderFax}" size="30" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>Street Address</td>
			<td><input type="text" id="copyRightHolderStreetAddress"
				name="digitalRightsManagementBean.copyRightHolderStreetAddress"
				value="${digitalRightsManagementBean.copyRightHolderStreetAddress}"
				size="50" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>Apt/Suite/Other</td>
			<td><input type="text" id="copyRightHolderOtherAddress"
				name="digitalRightsManagementBean.copyRightHolderOtherAddress"
				value="${digitalRightsManagementBean.copyRightHolderOtherAddress}"
				size="30" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>City</td>
			<td><input type="text" id="copyRightHolderCity"
				name="digitalRightsManagementBean.copyRightHolderCity"
				value="${digitalRightsManagementBean.copyRightHolderCity}" size="30" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>State</td>
			<td><input type="text" id="copyRightHolderState"
				name="digitalRightsManagementBean.copyRightHolderState"
				value="${digitalRightsManagementBean.copyRightHolderState}"
				size="30" /></td>
		</tr>
		<tr class="copyRightHolderData">
			<td>Zip Code</td>
			<td><input type="text" id="copyRightHolderZipCode"
				name="digitalRightsManagementBean.copyRightHolderZipCode"
				value="${digitalRightsManagementBean.copyRightHolderZipCode}"
				size="30" /></td>
		</tr>
	</table>
</div>

<table style="width: 100%">
	<tr class="copyRightHolderData">
		<td nowrap="nowrap"><input type="checkbox"
			name="digitalRightsManagementBean.copyRightHolderApproved"
			value="${digitalRightsManagementBean.copyRightHolderApproved}" />
			Copyright Holder approved for use</td>

	</tr>

	<tr>
		<td><br>
		<br></td>
	</tr>
	<tr>
		<td><b>Publisher Information</b></td>
	</tr>
	<tr>
		<td nowrap="nowrap"><input type="checkbox" name="publisherSameAs"
			value="copyRightHolder"
			onclick="copyValues(this,'copyRightHolder','publisher');" /> Copy
			values from Copy Right Holder</td>
	</tr>
	<tr>
		<td nowrap="nowrap">Publisher Name</td>
		<td><div
				title="${'The entity holding publishing rights on the artifact e.g. publisher of book to which the given artifact belongs '}">
				<input type="text" id="publisherId"
					name="digitalRightsManagementBean.publisher"
					value="${digitalRightsManagementBean.publisher}" size="50" /> ?
			</div></td>
	</tr>
</table>
<div>
	<table style="width: 100%">
		<tr>
			<td onclick="expandCollapse('idDivPublisher');" id="expand"><font
				color="blue">[+] Expand to enter contact information</font></td>
		</tr>
	</table>
</div>
<div id="idDivPublisher" style="display: none;">
	<table style="width: 100%">
		<tr>
			<td>Email</td>
			<td><input type="text" id="publisherEmail"
				name="digitalRightsManagementBean.publisherEmail"
				value="${digitalRightsManagementBean.publisherEmail}" size="40" /></td>
		</tr>
		<tr>
			<td>Cell Phone</td>
			<td><input type="text" id="publisherCellPhone"
				name="digitalRightsManagementBean.publisherCellPhone"
				value="${digitalRightsManagementBean.publisherCellPhone}" size="30" /></td>
		</tr>
		<tr>
			<td>Office Phone</td>
			<td><input type="text" id="publisherOfficePhone"
				name="digitalRightsManagementBean.publisherOfficePhone"
				value="${digitalRightsManagementBean.publisherOfficePhone}"
				size="30" /></td>
		</tr>
		<tr>
			<td>Fax</td>
			<td><input type="text" id="publisherFax"
				name="digitalRightsManagementBean.publisherFax"
				value="${digitalRightsManagementBean.publisherFax}" size="30" /></td>
		</tr>
		<tr>
			<td>Street Address</td>
			<td><input type="text" id="publisherStreetAddress"
				name="digitalRightsManagementBean.publisherStreetAddress"
				value="${digitalRightsManagementBean.publisherStreetAddress}"
				size="50" /></td>
		</tr>
		<tr>
			<td>Apt/Suite/Other</td>
			<td><input type="text" id="publisherOtherAddress"
				name="digitalRightsManagementBean.publisherOtherAddress"
				value="${digitalRightsManagementBean.publisherOtherAddress}"
				size="30" /></td>
		</tr>
		<tr>
			<td>City</td>
			<td><input type="text" id="publisherCity"
				name="digitalRightsManagementBean.publisherCity"
				value="${digitalRightsManagementBean.publisherCity}" size="30" /></td>
		</tr>
		<tr>
			<td>State</td>
			<td><input type="text" id="publisherState"
				name="digitalRightsManagementBean.publisherState"
				value="${digitalRightsManagementBean.publisherState}" size="30" /></td>
		</tr>
		<tr>
			<td>Zip Code</td>
			<td><input type="text" id="publisherZipCode"
				name="digitalRightsManagementBean.publisherZipCode"
				value="${digitalRightsManagementBean.publisherZipCode}" size="30" /></td>
		</tr>
	</table>
</div>

<table style="width: 100%">
	<tr>
		<td><c:choose>
				<c:when
					test="${not empty digitalRightsManagementBean.publisherApproved}">
					<input type="checkbox"
						name="digitalRightsManagementBean.publisherApproved"
						value="${digitalRightsManagementBean.publisherApproved}" />
				</c:when>
				<c:otherwise>
					<input type="checkbox"
						name="digitalRightsManagementBean.publisherApproved" value="true" />
				</c:otherwise>
			</c:choose> Publisher approved</td>
	</tr>

	<tr>
		<td><br>
		<br></td>
	</tr>
	<tr>
		<td><b>Contributor Information</b></td>
	</tr>
	<tr>
		<td nowrap="nowrap">Copy values from <input type="radio"
			name="contributorSameAs" value="copyRightHolder"
			onclick="copyValues(this,'copyRightHolder','contributor');" /> Copy
			Right Holder <input type="radio" name="contributorSameAs"
			value="publisher"
			onclick="copyValues(this,'publisher','contributor');" /> Publisher
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap">Contributor Name</td>
		<td nowrap="nowrap"><div
				title="${'An entity responsible for providing or handing over the artifact for use in TEN.'}">
				<input type="text" id="contributorId"
					name="digitalRightsManagementBean.contributor"
					value="${digitalRightsManagementBean.contributor}" size="50" /> ?
			</div></td>
	</tr>
</table>
<div>
	<table style="width: 100%">
		<tr>
			<td onclick="expandCollapse('idDivContributor');" id="expand"><font
				color="blue">[+] Expand to enter contact information</font></td>
		</tr>
	</table>
</div>
<div id="idDivContributor" style="display: none;">
	<table style="width: 100%">

		<tr>
			<td>Email</td>
			<td><input type="text" id="contributorEmail"
				name="digitalRightsManagementBean.contributorEmail"
				value="${digitalRightsManagementBean.contributorEmail}" size="40" /></td>
		</tr>
		<tr>
			<td>Cell Phone</td>
			<td><input type="text" id="contributorCellPhone"
				name="digitalRightsManagementBean.contributorCellPhone"
				value="${digitalRightsManagementBean.contributorCellPhone}"
				size="30" /></td>
		</tr>
		<tr>
			<td>Office Phone</td>
			<td><input type="text" id="contributorOfficePhone"
				name="digitalRightsManagementBean.contributorOfficePhone"
				value="${digitalRightsManagementBean.contributorOfficePhone}"
				size="30" /></td>
		</tr>
		<tr>
			<td>Fax</td>
			<td><input type="text" id="contributorFax"
				name="digitalRightsManagementBean.contributorFax"
				value="${digitalRightsManagementBean.contributorFax}" size="30" /></td>
		</tr>
		<tr>
			<td>Street Address</td>
			<td><input type="text" id="contributorStreetAddress"
				name="digitalRightsManagementBean.contributorStreetAddress"
				value="${digitalRightsManagementBean.contributorStreetAddress}"
				size="50" /></td>
		</tr>
		<tr>
			<td>Apt/Suite/Other</td>
			<td><input type="text" id="contributorOtherAddress"
				name="digitalRightsManagementBean.contributorOtherAddress"
				value="${digitalRightsManagementBean.contributorOtherAddress}"
				size="30" /></td>
		</tr>
		<tr>
			<td>City</td>
			<td><input type="text" id="contributorCity"
				name="digitalRightsManagementBean.contributorCity"
				value="${digitalRightsManagementBean.contributorCity}" size="30" /></td>
		</tr>
		<tr>
			<td>State</td>
			<td><input type="text" id="contributorState"
				name="digitalRightsManagementBean.contributorState"
				value="${digitalRightsManagementBean.contributorState}" size="30" /></td>
		</tr>
		<tr>
			<td>Zip Code</td>
			<td><input type="text" id="contributorZipCode"
				name="digitalRightsManagementBean.contributorZipCode"
				value="${digitalRightsManagementBean.contributorZipCode}" size="30" /></td>
		</tr>

		<tr>
			<td>Tribal Affiliation</td>
			<td><input type="text" id="contributorTribalAffiliation"
				name="digitalRightsManagementBean.contributorTribalAffiliation"
				value="${digitalRightsManagementBean.contributorTribalAffiliation}"
				size="40" /></td>
		</tr>
	</table>
</div>

<table style="width: 100%">
	<tr>
		<td><br>
		<br></td>
	</tr>

	<tr>
		<td nowrap="nowrap">Select Tribe</td>
		<td><div
				title="${'Tribe associated with the cultural learning object'}">
				<select name="digitalRightsManagementBean.tribe">
					<option value="UNK"
						<c:if test="${digitalRightsManagementBean.tribe == 'UNK'}">selected</c:if>>Unknown</option>
					<option value="CHE"
						<c:if test="${digitalRightsManagementBean.tribe == 'CHE'}">selected</c:if>>CHEHALIS
						CONFEDERATED TRIBES (CHE)</option>
					<option value="COL"
						<c:if test="${digitalRightsManagementBean.tribe == 'COL'}">selected</c:if>>COLVILLE
						CONFEDERATED TRIBES (COL)</option>
					<option value="COW"
						<c:if test="${digitalRightsManagementBean.tribe == 'COW'}">selected</c:if>>COWLITZ
						INDIAN TRIBE (COW)</option>
					<option value="HOH"
						<c:if test="${digitalRightsManagementBean.tribe == 'HOH'}">selected</c:if>>HOH
						TRIBE (HOH)</option>
					<option value="JAM"
						<c:if test="${digitalRightsManagementBean.tribe == 'JAM'}">selected</c:if>>JAMESTOWN
						SKLALLAM TRIBE (JAM)</option>
					<option value="KAL"
						<c:if test="${digitalRightsManagementBean.tribe == 'KAL'}">selected</c:if>>KALISPEL
						TRIBE (KAL)</option>
					<option value="LOW"
						<c:if test="${digitalRightsManagementBean.tribe == 'LOW'}">selected</c:if>>LOWER
						ELWHA KLALLAM TRIBE (LOW)</option>
					<option value="LUM"
						<c:if test="${digitalRightsManagementBean.tribe == 'LUM'}">selected</c:if>>LUMMI
						NATION (LUM)</option>
					<option value="MAK"
						<c:if test="${digitalRightsManagementBean.tribe == 'MAK'}">selected</c:if>>MAKAH
						TRIBE (MAK)</option>
					<option value="MUC"
						<c:if test="${digitalRightsManagementBean.tribe == 'MUC'}">selected</c:if>>MUCKLESHOOT
						TRIBE (MUC)</option>
					<option value="NIS"
						<c:if test="${digitalRightsManagementBean.tribe == 'NIS'}">selected</c:if>>NISQUALLY
						TRIBE (NIS)</option>
					<option value="NOO"
						<c:if test="${digitalRightsManagementBean.tribe == 'NOO'}">selected</c:if>>NOOKSACK
						TRIBE (NOO)</option>
					<option value="PGS"
						<c:if test="${digitalRightsManagementBean.tribe == 'PGS'}">selected</c:if>>PORT
						GAMBLE SKLALLAM TRIBE (PGS)</option>
					<option value="PUY"
						<c:if test="${digitalRightsManagementBean.tribe == 'PUY'}">selected</c:if>>PUYALLUP
						TRIBE (PUY)</option>
					<option value="QUI"
						<c:if test="${digitalRightsManagementBean.tribe == 'QUI'}">selected</c:if>>QUILEUTE
						TRIBE (QUI)</option>
					<option value="QUN"
						<c:if test="${digitalRightsManagementBean.tribe == 'QUN'}">selected</c:if>>QUINAULT
						NATION (QUN)</option>
					<option value="SAM"
						<c:if test="${digitalRightsManagementBean.tribe == 'SAM'}">selected</c:if>>SAMISH
						NATION (SAM)</option>
					<option value="SAU"
						<c:if test="${digitalRightsManagementBean.tribe == 'SAU'}">selected</c:if>>SAUK-SUIATTLE
						TRIBE (SAU)</option>
					<option value="SHO"
						<c:if test="${digitalRightsManagementBean.tribe == 'SHO'}">selected</c:if>>SHOALWATER
						BAY TRIBE (SHO)</option>
					<option value="SKO"
						<c:if test="${digitalRightsManagementBean.tribe == 'SKO'}">selected</c:if>>SKOKOMISH
						TRIBE (SKO)</option>
					<option value="SNO"
						<c:if test="${digitalRightsManagementBean.tribe == 'SNO'}">selected</c:if>>SNOQUALMIE
						TRIBE (SNO)</option>
					<option value="SPO"
						<c:if test="${digitalRightsManagementBean.tribe == 'SPO'}">selected</c:if>>SPOKANE
						TRIBE (SPO)</option>
					<option value="SQU"
						<c:if test="${digitalRightsManagementBean.tribe == 'SQU'}">selected</c:if>>SQUAXIN
						ISLAND TRIBE (SQu)</option>
					<option value="STI"
						<c:if test="${digitalRightsManagementBean.tribe == 'STI'}">selected</c:if>>STILLAGUAMISH
						TRIBE (STI)</option>
					<option value="SUQ"
						<c:if test="${digitalRightsManagementBean.tribe == 'SUQ'}">selected</c:if>>SUQUAMISH
						TRIBE (SUQ)</option>
					<option value="SWI"
						<c:if test="${digitalRightsManagementBean.tribe == 'SWI'}">selected</c:if>>SWINOMISH
						TRIBE (SWI)</option>
					<option value="TUL"
						<c:if test="${digitalRightsManagementBean.tribe == 'TUL'}">selected</c:if>>TULALIP
						TRIBES (TUL)</option>
					<option value="UPS"
						<c:if test="${digitalRightsManagementBean.tribe == 'UPS'}">selected</c:if>>UPPER
						SKAGIT TRIBE (UPS)</option>
					<option value="YAK"
						<c:if test="${digitalRightsManagementBean.tribe == 'YAK'}">selected</c:if>>YAKAMA
						NATION (YAK)</option>
				</select> ?
			</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Physical Description</td>
		<td><div
				title="${'The description of the artifact e.g. the format in which the artifact was provided such as hard copy, compact disc'}">
				<input type="text" id="physicalDescriptionId"
					name="digitalRightsManagementBean.physicalDescription"
					value="${digitalRightsManagementBean.physicalDescription}"
					size="50" /> ?
			</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Loan Period End Date</td>
		<td><div
				title="${'End date for the loan of artifact in yyyy-MM-dd format. Blank indicates unrestricted use of artifact.'}">
				<input type="text" id="loadPeriodId"
					name="digitalRightsManagementBean.loanPeriod"
					value="${digitalRightsManagementBean.loanPeriod}" size="30" /> ?
			</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Source Identifier</td>
		<td><div
				title="${'The identifier of the resource to which this artifact belongs. E.g. for an article from a book, ISBN (International Standard Book Number) of book.'}">
				<input type="text" id="identifierId"
					name="digitalRightsManagementBean.identifier"
					value="${digitalRightsManagementBean.identifier}" size="40" /> ?
			</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Source Description</td>
		<td><div
				title="${'The description of the source to which this artifact belongs e.g. for an article from book, description of book'}">
				<textarea id="identifierDescId"
					name="digitalRightsManagementBean.identifierDescription" cols="40"
					rows="2" />
				${digitalRightsManagementBean.identifierDescription}
				</textarea>  ?</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Handling instructions</td>
		<td><div
				title="${'Instructions on how the resource should be maintained in case of rare image or book e.g an image might be required to be in dry weather'}">
				<textarea id="handlingInstructionsId"
					name="digitalRightsManagementBean.handlingInstructions" cols="40"
					rows="2" />
				${digitalRightsManagementBean.handlingInstructions}
				</textarea>  ?</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Rights</td>
		<td><div
				title="${'The Rights element may be used for either a textual statement or a URL pointing to a rights statement, or a combination'}">
				<textarea id="rightsId" name="digitalRightsManagementBean.rights"
					cols="40" rows="2" />
				${digitalRightsManagementBean.rights}
				</textarea>  ?</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Intaker</td>
		<td><div
				title="${'The entity responsible for uploading the content to elearning system'}">
				<c:choose>
					<c:when test="${not empty digitalRightsManagementBean.intaker}">
						<input type="text" id="intakerId"
							name="digitalRightsManagementBean.intaker" readonly
							value="${digitalRightsManagementBean.intaker}" size="50" />
					</c:when>
					<c:otherwise>
						<input type="text" id="intakerId"
							name="digitalRightsManagementBean.intaker" readonly
							value="${sessionScope.user_details.user_name}" size="50" />
					</c:otherwise>
				</c:choose>
				?
			</div></td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td>Date of upload</td>
		<td><div
				title="${'Date of creation of resource in YYYY-MM-DD format'}">
				<c:choose>
					<c:when
						test="${not empty digitalRightsManagementBean.dateOfUpload}">
						<input type="text" id="dateId"
							name="digitalRightsManagementBean.dateOfUpload"
							value="${digitalRightsManagementBean.dateOfUpload}" />
					</c:when>
					<c:otherwise>
						<jsp:useBean id="today" class="java.util.Date" scope="page" />
						<input type="text" id="dateId"
							name="digitalRightsManagementBean.dateOfUpload"
							value='<fmt:formatDate pattern="yyyy-MM-dd" value="${today}" />' />
					</c:otherwise>
				</c:choose>
				?
			</div></td>
	</tr>
	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td nowrap="nowrap"><c:choose>
				<c:when
					test="${digitalRightsManagementBean.storyProvided == 'true'}">
					<input type="checkbox" id="storyProvidedCheckbox"
						name="digitalRightsManagementBean.storyProvided"
						value="${digitalRightsManagementBean.storyProvided}"
						checked="checked" onchange="onStoryProvidedChange(this)" />
				</c:when>
				<c:otherwise>
					<input type="checkbox" id="storyProvidedCheckbox"
						name="digitalRightsManagementBean.storyProvided" value="true"
						onchange="onStoryProvidedChange(this)" />
				</c:otherwise>
			</c:choose> Story provided with the artifact</td>
	</tr>
	<tr class="storyClass" style="display: none;">
		<td nowrap="nowrap">Story</td>
		<td nowrap="nowrap"><div
				title="${'Story provided by tribe with the artifact that will be used by annotators to tag the artifact'}">
				<textarea id="storyId" name="digitalRightsManagementBean.story"
					cols="40" rows="3">${digitalRightsManagementBean.story}</textarea>
				?
			</div></td>
	</tr>
	<tr class="storyClass" style="display: none;">
		<td nowrap="nowrap">Story context</td>
		<td>
			<div
				title="${'Information about context of story provided by tribe such as the format it was provided in e.g. as a recording, on paper, audio'}">
				<input type="text" id="storyContext"
					name="digitalRightsManagementBean.storyContext"
					value="${digitalRightsManagementBean.storyContext}" size="50" /> ?
			</div>
		</td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr class="storyClass" style="display: none;">
		<td nowrap="nowrap"><b>Story Provider Information</b></td>
		<td></td>
	<tr class="storyClass" style="display: none;">
		<td><input type="checkbox" name="storyProviderSameAs"
			value="contributor"
			onclick="copyValues(this,'contributor','storyProvider');" /> Copy
			values from Contributor</td>

	</tr>
	<tr class="storyClass" style="display: none;">
		<td nowrap="nowrap">Story Provider Name</td>
		<td><div
				title="${'An entity responsible for providing the story associated with the artifact'}">
				<input type="text" id="storyProviderId"
					name="digitalRightsManagementBean.storyProvider"
					value="${digitalRightsManagementBean.storyProvider}" size="50" /> ?
			</div></td>
	</tr>
</table>
<div>
	<table style="width: 100%">
		<tr class="storyClass" style="display: none;">
			<td onclick="expandCollapse('idDivStoryProvider');" id="expand"><font
				color="blue">[+] Expand to enter contact information</font></td>
		</tr>
	</table>
</div>

<div id="idDivStoryProvider" style="display: none;">
	<table style="width: 100%">
		<tr class="storyClass" style="display: none;">
			<td>Email</td>
			<td><input type="text" id="storyProviderEmail"
				name="digitalRightsManagementBean.storyProviderEmail"
				value="${digitalRightsManagementBean.storyProviderEmail}" size="40" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>Cell Phone</td>
			<td><input type="text" id="storyProviderCellPhone"
				name="digitalRightsManagementBean.storyProviderCellPhone"
				value="${digitalRightsManagementBean.storyProviderCellPhone}"
				size="30" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>Office Phone</td>
			<td><input type="text" id="storyProviderOfficePhone"
				name="digitalRightsManagementBean.storyProviderOfficePhone"
				value="${digitalRightsManagementBean.storyProviderOfficePhone}"
				size="30" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>Fax</td>
			<td><input type="text" id="storyProviderFax"
				name="digitalRightsManagementBean.storyProviderFax"
				value="${digitalRightsManagementBean.storyProviderFax}" size="30" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>Street Address</td>
			<td><input type="text" id="storyProviderStreetAddress"
				name="digitalRightsManagementBean.storyProviderStreetAddress"
				value="${digitalRightsManagementBean.storyProviderStreetAddress}"
				size="50" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>Apt/Suite/Other</td>
			<td><input type="text" id="storyProviderOtherAddress"
				name="digitalRightsManagementBean.storyProviderOtherAddress"
				value="${digitalRightsManagementBean.storyProviderOtherAddress}"
				size="30" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>City</td>
			<td><input type="text" id="storyProviderCity"
				name="digitalRightsManagementBean.storyProviderCity"
				value="${digitalRightsManagementBean.storyProviderCity}" size="30" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>State</td>
			<td><input type="text" id="storyProviderState"
				name="digitalRightsManagementBean.storyProviderState"
				value="${digitalRightsManagementBean.storyProviderState}" size="30" /></td>
		</tr>
		<tr class="storyClass" style="display: none;">
			<td>Zip Code</td>
			<td><input type="text" id="storyProviderZipCode"
				name="digitalRightsManagementBean.storyProviderZipCode"
				value="${digitalRightsManagementBean.storyProviderZipCode}"
				size="30" /></td>
		</tr>
	</table>
</div>
<br>
<br>